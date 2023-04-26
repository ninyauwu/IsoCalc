package com.dl630.isocalc.controller;

import com.dl630.isocalc.core.beract.BeractAdapter;
import com.dl630.isocalc.core.beract.BeractOutputReader;
import com.dl630.isocalc.core.DecimalTextFormatter;
import com.dl630.isocalc.Main;
import com.dl630.isocalc.controller.component.ElementEntryController;
import com.dl630.isocalc.controller.component.ElementEntryFieldController;
import com.dl630.isocalc.controller.component.TopButtonPaneController;
import com.dl630.isocalc.core.IntegerTextFormatter;
import com.dl630.isocalc.scene.SceneController;
import com.dl630.isocalc.core.storage.DataMemory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class FinalDataController extends SceneController implements Initializable {
    @FXML
    public TopButtonPaneController topButtonPaneController;


    @FXML
    public HBox timeFieldContainer;

    @FXML
    public HBox cycleCountFieldContainer;

    @FXML
    public HBox waitTimeFieldContainer;


    @FXML
    public TextField timeField;

    @FXML
    public TextField cycleCountField;

    @FXML
    public TextField waitTimeField;


    @FXML
    public AnchorPane anchorPane;

    @FXML
    public VBox contentVBox;

    @FXML
    public Button calcButton;

    private TextField thicknessField;

    public FinalDataController() {
        super("final_data.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        topButtonPaneController.initializeReturnButton("Back", actionEvent -> {
            storeValues();
            RadiationSettingListController radiationSettingListController = new RadiationSettingListController();
            Main.setScene(radiationSettingListController);
        });

        // Prompt text
        timeField.setPromptText("Irradiation time");
        cycleCountField.setPromptText("Number of irradiation cycles");
        waitTimeField.setPromptText("Decay time between cycles/after irradiation");

        // Setting padding manually because if I do it via a stylesheet, contentVBox.getPadding() returns 0,
        // and I need to reference this number down below
        contentVBox.prefWidthProperty().bind(root.widthProperty().multiply(0.54));
        contentVBox.setPadding(new Insets(36));

        // Create some complex width bindings to make sure there's enough space for the unit label at the end
        timeField.prefWidthProperty().bind(contentVBox.widthProperty()
                .subtract(timeField.getFont().getSize())
                .subtract(contentVBox.spacingProperty())
                .subtract(contentVBox.getPadding().getLeft())
                .subtract(contentVBox.getPadding().getRight())
                .subtract(1));
        cycleCountField.prefWidthProperty().bind(contentVBox.widthProperty()
                .subtract(cycleCountField.getFont().getSize())
                .subtract(contentVBox.spacingProperty())
                .subtract(contentVBox.getPadding().getLeft())
                .subtract(contentVBox.getPadding().getRight())
                .subtract(1));
        waitTimeField.prefWidthProperty().bind(contentVBox.widthProperty()
                .subtract(waitTimeField.getFont().getSize())
                .subtract(contentVBox.spacingProperty())
                .subtract(contentVBox.getPadding().getLeft())
                .subtract(contentVBox.getPadding().getRight())
                .subtract(1));

        // Set values from memory
        timeField.setText(DataMemory.getSavedIrradiationTime() == null
                ? ""
                : String.valueOf(DataMemory.getSavedIrradiationTime()));
        cycleCountField.setText(DataMemory.getSavedCycleCount() == null
                ? ""
                : String.valueOf(DataMemory.getSavedCycleCount()));
        waitTimeField.setText(DataMemory.getSavedDecayTime() == null
                ? ""
                : String.valueOf(DataMemory.getSavedDecayTime()));

        // Create an element entry
        ElementEntryController entryController = new ElementEntryController(DataMemory.getSavedContainerElement(), 0);
        entryController.setHideNatural(true);

        // Give the element button an onClick callback
        entryController.setElementButtonEvent(actionEvent -> {
            storeValues();
            PeriodicPickerController periodicPickerController = new PeriodicPickerController("periodic_picker.fxml", true, element -> {
                DataMemory.setSavedContainerElement(element);
                FinalDataController finalDataController = new FinalDataController();
                Main.setScene(finalDataController);
            });
            Main.setScene(periodicPickerController);
        });

        // Add a thickness field
        ElementEntryFieldController thickness = new ElementEntryFieldController("Thickness", "m");
        entryController.addEntryField(thickness, false);


        // Some styling that's not really possible in an FXML file
        AnchorPane entryRoot = (AnchorPane) entryController.getScene().getRoot();
        entryRoot.prefWidthProperty().bind(contentVBox.widthProperty());
        entryRoot.setStyle("-fx-background-color: white;");
        thickness.getScene().getRoot().setStyle("-fx-background-color: white;");

        // Add an X button
        if (DataMemory.getSavedContainerElement() != null) {
            entryController.addXButton(actionEvent -> {
                entryController.setElement(null);

                // Manually disable the field, I should probably make a function for this
                thickness.textField.setDisable(true);
                thickness.textField.setTextFormatter(null);
                thickness.textField.setText("");
                thickness.titleLabel.setDisable(true);
                thickness.unitLabel.setDisable(true);

                entryController.removeXButton();
                DataMemory.setSavedContainerElement(null);
                DataMemory.setSavedContainerThickness(null);
            }, true);
        } else {
            // Disable the field
            thickness.textField.setDisable(true);
            thickness.titleLabel.setDisable(true);
            thickness.unitLabel.setDisable(true);
        }

        // Set minimum width
        contentVBox.minWidthProperty().bind(entryController.leftBox.widthProperty()
                .add(contentVBox.getPadding().getLeft())
                .add(contentVBox.getPadding().getRight()));

        // Add the element entry to the GUI
        contentVBox.getChildren().add(contentVBox.getChildren().size() - 2, entryRoot);
        thicknessField = thickness.textField;

        // Load values from memory
        loadValues();

        // Add text formatters
        addFieldListener(timeField);
        addIntegerFieldListener(cycleCountField);
        addFieldListener(waitTimeField);
        addFieldListener(thickness.textField);

        // Tell the calculate button what to do
        calcButton.setOnAction(event -> {
            storeValues();
            if (Files.exists(Paths.get("beract/test.txt_out.txt"))) {
                try {
                    File output = new File("beract/test.txt_out.txt");
                    output.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            BeractAdapter.calculateResult(DataMemory.getSavedMasses().get(DataMemory.getSelectedMaterialSettingKey()).getMasses(),
                    DataMemory.getRadiationSettingFromMap(DataMemory.getSelectedRadiationSettingKey()));
            BeractOutputReader beractOutputReader = new BeractOutputReader();
            CountDownLatch latch = new CountDownLatch(1);
            beractOutputReader.startRead(latch);
            try {
                latch.await();
                Main.setScene(new ResultController());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Add decimal text formatters to a field when it's focused on
     * @param textField
     */
    private void addFieldListener(TextField textField) {
        textField.selectionProperty().addListener(change -> {
            if (getScene().getFocusOwner() == null) return;
            if (getScene().getFocusOwner().equals(textField)) {
                textField.setTextFormatter(new DecimalTextFormatter(0, 99, false, true));
            }
        });
    }

    /**
     * Add decimal text formatters to a field when it's focused on
     * @param textField
     */
    private void addIntegerFieldListener(TextField textField) {
        textField.selectionProperty().addListener(change -> {
            if (getScene().getFocusOwner() == null) return;
            if (getScene().getFocusOwner().equals(textField)) {
                textField.setTextFormatter(new IntegerTextFormatter(false, true));
            }
        });
    }

    /**
     * Store the field values in DataMemory variables
     */
    private void storeValues() {
        DataMemory.setSavedIrradiationTime(timeField.getText().equals("")
                ? null
                : new BigDecimal(timeField.getText()));
        DataMemory.setSavedCycleCount(cycleCountField.getText().equals("")
                ? null
                : Integer.parseInt(cycleCountField.getText()));
        DataMemory.setSavedDecayTime(waitTimeField.getText().equals("")
                ? null
                : new BigDecimal(waitTimeField.getText()));
        DataMemory.setSavedContainerThickness(thicknessField.getText().equals("")
                ? null
                : new BigDecimal(thicknessField.getText()));
    }

    /**
     * Set field text from memory
     */
    private void loadValues() {
        if (DataMemory.getSavedIrradiationTime() != null) {
            timeField.setText(DataMemory.getSavedIrradiationTime().toString());
        }
        if (DataMemory.getSavedCycleCount() != null) {
            cycleCountField.setText(DataMemory.getSavedCycleCount().toString());
        }
        if (DataMemory.getSavedDecayTime() != null) {
            waitTimeField.setText(DataMemory.getSavedDecayTime().toString());
        }
        if (DataMemory.getSavedContainerThickness() != null) {
            thicknessField.setText(DataMemory.getSavedContainerThickness().toString());
        }
    }
}
