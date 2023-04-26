package com.dl630.isocalc.controller;

import com.dl630.isocalc.core.beract.BeractOutputReader;
import com.dl630.isocalc.Main;
import com.dl630.isocalc.OutputDataModel;
import com.dl630.isocalc.controller.component.ResultTableController;
import com.dl630.isocalc.controller.component.TopButtonPaneController;
import com.dl630.isocalc.scene.SceneController;
import com.dl630.isocalc.core.storage.DataMemory;
import com.dl630.isocalc.core.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultController extends SceneController implements Initializable {
    @FXML
    public VBox resultBox;
    @FXML
    public TopButtonPaneController topButtonPaneController;

    public ResultController() {
        super("result.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topButtonPaneController.initializeReturnButton("Back", actionEvent -> {
            Main.setScene(new FinalDataController());
        });

        ObservableList<OutputDataModel> irradiationResults = FXCollections.observableArrayList();
        ObservableList<OutputDataModel> decayResults = FXCollections.observableArrayList();

        ResultTableController irradiationViewController = new ResultTableController();
        ResultTableController decayViewController = new ResultTableController();
        resultBox.getChildren().addAll(irradiationViewController.getScene().getRoot(),
                                       decayViewController.getScene().getRoot());
        ArrayList<String> result = BeractOutputReader.readResult();

        // Split results in two tables
        boolean decay = false;
        int ln = 0;
        for (String line : result) {
            switch (line) {
                case "target; product; Act (Bq); halflife; h10 at 1 m; h07; h10 shielded at 1 m; h10 at container surface":
                    continue;
                case "AFTER IRRAD":
                    continue;
                case "AFTER DECAY": {
                    decay = true;
                    continue;
                }
                default: {
                    Pattern pattern = Pattern.compile("Can't find.*");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) continue;

                    String[] data = line.split("; ");
                    for (int i = 0; i < data.length; i++) {
                        data[i] = data[i].replaceAll("^\\s*", "");
                        data[i] = data[i].replaceAll("\\s*$", "");
                    }

                    try {
                        if (decay) {
                            System.out.println("adding to decayResults: " + data[5]);
                            decayResults.add(new OutputDataModel(data[0], data[1], data[2], data[3], data[4], data[5], data[6]));
                        } else {
                            System.out.println("adding to irradResults: " + data[5]);
                            irradiationResults.add(new OutputDataModel(data[0], data[1], data[2], data[3], data[4], data[5], data[6]));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Exception caught at line " + ln);
                        e.printStackTrace();
                    }
                }
            }

            ln++;
        }

        irradiationViewController.setItems(irradiationResults);
        decayViewController.setItems(decayResults);

        irradiationViewController.setOnExport(actionEvent ->
                saveFile("irradiation.csv", Util.condenseStringList(splitResult(result)[0], true), true));
        decayViewController.setOnExport(actionEvent ->
                saveFile("decay.csv", Util.condenseStringList(splitResult(result)[0], true), false));
    }

    /**
     * Write to the file after it's been created
     * @param fileName - default name of the file
     * @param content - content to write to the file
     */
    private static void saveFile(String fileName, String content, boolean irradiation) {
        File file = openFileSaver(fileName, irradiation);
        if (file == null) return;
        try {
            // Create file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
            }
            String path = file.getPath();

            // Save the path for future reference
            int index = path.lastIndexOf('\\');
            String substring = path.substring(0, index);
            if (irradiation) {
                DataMemory.setSavedIrradiationPath(substring);
            } else {
                DataMemory.setSavedDecayPath(substring);
            }
            DataMemory.savePaths();

            // Write
            FileWriter writer = new FileWriter(path);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialogue box for saving the csv, doesn't write to the file after it's been created
     * @param name - default name of the file
     * @return the file after it's been saved
     */
    private static File openFileSaver(String name, boolean irradiation) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(name);
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("Comma separated value file", "*.csv");
        fileChooser.getExtensionFilters().add(csvFilter);
        fileChooser.setSelectedExtensionFilter(csvFilter);
        fileChooser.setTitle("Save Table");

        if (irradiation && DataMemory.getSavedIrradiationPath() != null) {
            fileChooser.setInitialDirectory(new File(DataMemory.getSavedIrradiationPath()));
        } else if (DataMemory.getSavedDecayPath() != null) {
            fileChooser.setInitialDirectory(new File(DataMemory.getSavedDecayPath()));
        }

        return fileChooser.showSaveDialog(Main.currentStage);
    }

    /**
     * Splits the result of the beract software into an irradiation and decay table
     * @param result - A list of the lines taken from the beract output
     * @return an array of two strings, the first being the irradiation results
     */
    private static ArrayList<String>[] splitResult(ArrayList<String> result) {
        int index = 0;

        ArrayList[] output = new ArrayList[2];
        output[0] = new ArrayList<>();
        output[1] = new ArrayList<>();

        for (String s : result) {
            if (index < 2) { index++; continue; }

            Pattern pattern = Pattern.compile("Can't find.*");
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) { index++; continue; }

            if (s.equals("AFTER DECAY")) { index++; break; }

            output[0].add(s);
            index++;
        }
        for (int i = index; i < result.size(); i++) {
            Pattern pattern = Pattern.compile("Can't find.*");
            Matcher matcher = pattern.matcher(result.get(i));
            if (matcher.find()) { index++; continue; }
            output[1].add(result.get(i));
        }

        return output;
    }
}
