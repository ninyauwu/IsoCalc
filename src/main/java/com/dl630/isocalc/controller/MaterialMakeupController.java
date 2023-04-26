package com.dl630.isocalc.controller;

import com.dl630.isocalc.core.DecimalTextFormatter;
import com.dl630.isocalc.Main;
import com.dl630.isocalc.controller.component.ElementEntryController;
import com.dl630.isocalc.controller.component.ElementEntryFieldController;
import com.dl630.isocalc.controller.component.TopButtonPaneController;
import com.dl630.isocalc.scene.SceneController;
import com.dl630.isocalc.core.storage.DataMemory;
import com.dl630.isocalc.core.Util;
import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.element.ElementHandler;
import com.dl630.isocalc.element.IsotopeMass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MaterialMakeupController extends SceneController implements Initializable {
    @FXML
    public VBox contentPane;

    @FXML
    private Label totalLabel;

    @FXML
    private TextField totalPercentageField;

    @FXML
    private Label totalPercentageLabel;

    @FXML
    private TextField totalMassField;

    @FXML
    private Label totalMassLabel;

    @FXML
    public AnchorPane totalPane;

    @FXML
    public Button resetTotalMassButton;

    @FXML
    public TopButtonPaneController topButtonPaneController;

    private ArrayList<IsotopeMakeupFieldStructure> fieldList;
    private Tooltip continueBoxTooltip; // Really no clue how to reference this after instantiation, so storing it as an attribute
    private String trueTotalMass;

    private boolean totalMassUnchanged = true;

    public MaterialMakeupController() {
        super("material_makeup.fxml");
        fieldList = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        totalMassLabel.setText("g");
        totalPercentageLabel.setText("%");
        totalLabel.setText("Total");

        // Add return button
        topButtonPaneController.initializeReturnButton("Elements", e -> {
            saveMasses();
            PeriodicPickerController periodicPicker = new PeriodicPickerController("periodic_picker.fxml", false, element -> {
                Main.setScene(new ElementIsotopeListController(element, ElementHandler.getAllIsotopes().get(element)));
            });
            Main.setScene(periodicPicker);
        });

        // Add continue button
        topButtonPaneController.initializeContinueButton("Save", e -> {
            saveMasses();
            DataMemory.saveMasses();
            MaterialMakeupListController materialMakeupListController = new MaterialMakeupListController();
            Main.setScene(materialMakeupListController);
        });
        topButtonPaneController.continueButton.setDisable(true);

        // Isotope entry loop
        for (Element element : DataMemory.getCurrentSelectedIsotopes().keySet()) {
            for (Integer isotope : DataMemory.getCurrentSelectedIsotopes().get(element)) {
                TextField[] fields = createIsotopeEntryNew(element, isotope);
                fieldList.add(new IsotopeMakeupFieldStructure(element, isotope,
                        fields[0] /* mass */,
                        fields[1] /* percentage */));
            }
        }

        // Move the total to be below all the isotope entries
        contentPane.getChildren().remove(totalPane);
        contentPane.getChildren().add(contentPane.getChildren().size(), totalPane);

        // Total mass listener
        totalMassField.focusedProperty().addListener(change -> {
            if (getScene().getFocusOwner() != totalMassField) {
                if (totalMassField.getText().equals("")) resetTotalMass();
            }
        });
        totalMassField.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && totalMassField.getText().equals("")) {
                resetTotalMass();
            } else {
                onTotalMassChange();
            }
        });

        // Reset total mass button
        resetTotalMassButton.setOnAction(actionEvent -> {
            resetTotalMass();
        });

        // Add formatters to totals
        totalMassField.setTextFormatter(new DecimalTextFormatter(0, 99, false, true));
        totalPercentageField.setTextFormatter(new DecimalTextFormatter(0, 99, false));

        loadMasses();
        root.requestFocus();
    }

    public TextField[] createIsotopeEntryNew(Element element, Integer isotope) {
        // Create an element entry controller
        ElementEntryController elementEntryController = new ElementEntryController(element, isotope);
        elementEntryController.setXButtonEvent(e -> {

        });
        elementEntryController.setElementButtonSize(72);

        // Add text fields to the controller
        ElementEntryFieldController massController = new ElementEntryFieldController("mass", "g");
        ElementEntryFieldController percentageController = new ElementEntryFieldController("percentage", "%");
        elementEntryController.addEntryField(massController, true);
        elementEntryController.addEntryField(percentageController, true);

        // Finish adding element entry to the scene
        Separator separator = new Separator();
        elementEntryController.setXButtonEvent(e -> {
            contentPane.getChildren().remove(elementEntryController.getScene().getRoot());
            contentPane.getChildren().remove(separator);
            DataMemory.removeSelectedIsotope(element, isotope);

            for (int i = 0; i < fieldList.size(); i++) {
                if (fieldList.get(i).element == element && fieldList.get(i).isotope == isotope) {
                    fieldList.remove(i);
                }
            }
            if (fieldList.size() < 1) {
                disableTotals();
            } else {
                updateTotalPercentage();
                updateTotalMass();
            }
        });
        contentPane.getChildren().add(elementEntryController.getScene().getRoot());
        contentPane.getChildren().add(separator);

        // Format fields
        massController.textField.setTextFormatter(new DecimalTextFormatter(0, 99, false));
        percentageController.textField.setTextFormatter(new DecimalTextFormatter(0, 99, false));

        // Add field updaters
        percentageController.textField.setOnKeyReleased(e -> {
            updatePercentage(massController.textField, percentageController.textField);
        });
        massController.textField.setOnKeyReleased(e -> {
            updateMass(massController.textField);
        });

        // Return fields
        return new TextField[] { massController.textField, percentageController.textField };
    }

    public void onTotalMassChange() {
        if (getScene().getFocusOwner() != totalMassField) return;
        if (totalMassField.getText().length() < 1) return;
        if (totalMassField.getText().equals("")) {
            resetAllPercentages();
            resetTotalMass();
        } else {
            setTotalMassUnchanged(false);
        }

        trueTotalMass = totalMassField.getText();
        updateAllPercentages();
    }

    public void updateMass(TextField massField) {
        if (getScene().getFocusOwner() != massField) return;
        if (massField.getText().length() < 1) return;
        updateTotalMass();
        updateAllPercentages();
    }

    // For some reason I can't explain, precision list begins with values of 0
    // Essentially, for each item in fieldList, a 0 is added at the start of the list, doubling the total values in the list
    // Guess I'll just start at fieldList.size() for my index
    public void updateTotalMass() {
        BigDecimal mass = new BigDecimal(0.0);
        int minPrecision = 99;
        for (IsotopeMakeupFieldStructure isotopeMakeupFieldStructure : fieldList) {
            String massString = isotopeMakeupFieldStructure.massField.getText();
            if (massString.length() < 1) continue;
            if (!Util.isDecimalValid(massString)) continue;
            mass = mass.add(new BigDecimal(massString));
            if (minPrecision > Util.getChemicalPrecision(massString))
                minPrecision = Util.getChemicalPrecision(massString);
        }
        if (totalMassUnchanged) {
            String roundedString = Util.roundDecimalsTo(mass.toString(), minPrecision);
            System.out.println(roundedString);
            System.out.println(totalMassField);
            totalMassField.setText(roundedString.equals("0E-98") ? "0" : roundedString);
            trueTotalMass = mass.toString();
        } else {
            trueTotalMass = totalMassField.getText();
        }
    }

    public void updatePercentage(TextField massField,  TextField percentageField) {
        if (getScene().getFocusOwner() != percentageField) return;
        if (Double.parseDouble(totalMassField.getText()) <= 0.0) return;
        if (percentageField.getText().length() < 1) return;
        if (Double.parseDouble(percentageField.getText()) > 100.0) percentageField.setText("100.0");
        String newMass = String.valueOf(Double.parseDouble(totalMassField.getText())
                * (Double.parseDouble(percentageField.getText()) / 100.0));
        massField.setText(Util.roundDecimalsTo(newMass, Util.getChemicalPrecision(percentageField.getText())));
        updateTotalPercentage();
        setTotalMassUnchanged(false);
    }

    public void updateAllPercentages() {
        for (IsotopeMakeupFieldStructure isotopeMakeupFieldStructure : fieldList) {
            TextField percentageField = isotopeMakeupFieldStructure.percentageField;
            TextField massField = isotopeMakeupFieldStructure.massField;
            if (totalMassField.getText().length() < 1) return;
            if (massField.getText().length() < 1) return;
            if (Double.parseDouble(totalMassField.getText()) <= 0.0) return;
            percentageField.setText(String.valueOf(100.0
                    * Double.parseDouble(massField.getText())
                    / Double.parseDouble(trueTotalMass)));
            percentageField.setText(Util.roundDecimalsTo(percentageField.getText(), Util.getChemicalPrecision(massField.getText())));
        }
        updateTotalPercentage();
    }

    public void resetAllPercentages() {
        for (IsotopeMakeupFieldStructure isotopeMakeupFieldStructure : fieldList) {
            isotopeMakeupFieldStructure.percentageField.setText("0.0");
        }
        updateTotalPercentage();
    }

    public void resetTotalMass() {
        setTotalMassUnchanged(true);
        DataMemory.getSavedMasses().get(DataMemory.getCurrentMaterialSettingKey()).setTotalMassUnchanged(true);
        updateTotalMass();
        updateAllPercentages();
    }

    public void disableTotals() {
        totalMassField.setText("0");
        totalMassField.setDisable(true);
        totalPercentageField.setText("0.0");
        totalPercentageField.setDisable(true);
        resetTotalMassButton.setDisable(true);
        topButtonPaneController.continueButton.setDisable(true);
    }

    public void updateTotalPercentage() {
        if (fieldList.size() < 1) { totalPercentageField.setText("0.0"); }

        Double percentage = 0.0;
        int minPrecision = 99;
        for (IsotopeMakeupFieldStructure isotopeMakeupFieldStructure : fieldList) {
            String massString = isotopeMakeupFieldStructure.massField.getText();
            String percentageString = isotopeMakeupFieldStructure.percentageField.getText();
            if (massString.length() < 1) continue;
            try {
                percentage += Double.parseDouble(massString) * 100.0;
            } catch (NumberFormatException e) {
                continue;
            }
            if (minPrecision > Util.getChemicalPrecision(percentageString))
                minPrecision = Util.getChemicalPrecision(percentageString);
        }

        if (trueTotalMass == null) return;
        String roundedPercentage = Util.roundDecimalsTo(String.valueOf(percentage / Double.parseDouble(trueTotalMass)), minPrecision);
        if (Double.parseDouble(roundedPercentage) <= 100.0) {
            totalPercentageField.setStyle("-fx-text-fill: black;");
            Tooltip.uninstall(totalPercentageField, totalPercentageField.getTooltip());
            topButtonPaneController.continueButton.setDisable(false);
            Tooltip.uninstall(topButtonPaneController.continueButton, continueBoxTooltip);
        } else {
            totalPercentageField.setStyle("-fx-text-fill: red;");
            addTotalPercentageTooltip();
            topButtonPaneController.continueButton.setDisable(true);
            addContinueButtonTooltip();
        }

        totalPercentageField.setText(roundedPercentage);
    }

    public void addTotalPercentageTooltip() {
        Tooltip totalPercentageTooltip = new Tooltip("Percentages must add up to at most 100");
        totalPercentageTooltip.setShowDelay(new Duration(1));
        totalPercentageTooltip.setHideDelay(new Duration(1));
        totalPercentageTooltip.setFont(new Font(14));
        Tooltip.install(totalPercentageField, totalPercentageTooltip);
    }

    public void addContinueButtonTooltip() {
        Tooltip percentageTooltip = new Tooltip("Cannot continue: Current percentages add up to more than 100");
        percentageTooltip.setShowDelay(new Duration(1));
        percentageTooltip.setHideDelay(new Duration(1));
        percentageTooltip.setFont(new Font(14));
        continueBoxTooltip = percentageTooltip;
        Tooltip.install(topButtonPaneController.continueButton, percentageTooltip);
    }

    private void saveMasses() {
        DataMemory.clearMasses();
        for (IsotopeMakeupFieldStructure structure : fieldList) {
            DataMemory.addMass(new IsotopeMass(
                    structure.element,
                    structure.isotope,
                    new BigDecimal(structure.massField.getText())));
        }
        DataMemory.setTotalMass(DataMemory.getCurrentMaterialSettingKey(), new BigDecimal(totalMassField.getText()));
        DataMemory.getSavedMasses().get(DataMemory.getCurrentMaterialSettingKey()).setTotalMassUnchanged(totalMassUnchanged);
    }

    // Should be executed after list has been filled out with fields
    private void loadMasses() {
        if (DataMemory.getSavedMasses() == null) return;
        if (DataMemory.getTotalMass() != null) {
            totalMassField.setText(DataMemory.getTotalMass().toString());
        }

        setTotalMassUnchanged(DataMemory.getSavedMasses().get(DataMemory.getCurrentMaterialSettingKey()).isTotalMassUnchanged());
        for (IsotopeMakeupFieldStructure structure : fieldList) {
            for (IsotopeMass isotopeMass : DataMemory.getSavedMasses().get(DataMemory.getCurrentMaterialSettingKey()).getMasses()) {
                if (structure.element == isotopeMass.getElement() && structure.isotope.equals(isotopeMass.getIsotope())) {
                    structure.massField.setText(isotopeMass.getMass().toString());
                    updateTotalMass();
                    updateAllPercentages();
                }
            }
        }
    }

    private void setTotalMassUnchanged(boolean totalMassUnchanged) {
        resetTotalMassButton.setVisible(!totalMassUnchanged);
    }
}

class IsotopeMakeupFieldStructure {
    Element element;
    Integer isotope;
    TextField massField;
    TextField percentageField;

    public IsotopeMakeupFieldStructure(Element element, Integer isotope, TextField massField, TextField percentageField) {
        this.element = element;
        this.isotope = isotope;
        this.massField = massField;
        this.percentageField = percentageField;
    }
}
