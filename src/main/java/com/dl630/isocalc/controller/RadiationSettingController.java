package com.dl630.isocalc.controller;

import com.dl630.isocalc.core.DecimalTextFormatter;
import com.dl630.isocalc.core.storage.RadiationSettingObject;
import com.dl630.isocalc.core.callbacks.StringCallback;
import com.dl630.isocalc.scene.SceneController;
import com.dl630.isocalc.core.storage.DataMemory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RadiationSettingController extends SceneController implements Initializable {
    @FXML
    public VBox contentVBox;

    @FXML
    public TextField titleField;

    @FXML
    public Label thermalFluxLabel;

    @FXML
    public TextField thermalFluxField;

    @FXML
    public HBox thermalFluxUnit;

    @FXML
    public Label epithermalFluxLabel;

    @FXML
    public TextField epithermalFluxField;

    @FXML
    public HBox epithermalFluxUnit;

    @FXML
    public Label fastFluxLabel;

    @FXML
    public TextField fastFluxField;

    @FXML
    public HBox fastFluxUnit;

    @FXML
    public Label alphaLabel;

    @FXML
    public TextField alphaField;

    @FXML
    public Button saveButton;

    @FXML
    public Label errorLabel;

    String settingKey;
    StringCallback onSave;

    public RadiationSettingController(StringCallback onSave) {
        super("radiation_setting.fxml");
        this.onSave = onSave;
    }

    public RadiationSettingController(String settingKey, StringCallback onSave) {
        this(onSave);
        this.settingKey = settingKey;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        thermalFluxLabel.setText("Thermal Flux");
        epithermalFluxLabel.setText("Epithermal Flux");
        fastFluxLabel.setText("Fast Flux");
        alphaLabel.setText("Alpha (Optional)");
        saveButton.setText("Save");

        titleField.setPromptText("Setup name");
        thermalFluxField.setPromptText("e.g. 4e16");
        epithermalFluxField.setPromptText("e.g. 2e15");
        fastFluxField.setPromptText("e.g. 4e15");
        alphaField.setPromptText("e.g. 0.0");

        addUnit(thermalFluxUnit);
        addUnit(epithermalFluxUnit);
        addUnit(fastFluxUnit);

        saveButton.setOnAction(event -> {
            if (Objects.equals(titleField.getText(), "")) {
                showError("Please choose a name for your settings.");
                return;
            }
            if (Objects.equals(thermalFluxField.getText(), "")) {
                showError("Please enter a thermal flux. e.g. 4e16");
                return;
            }
            if (Objects.equals(epithermalFluxField.getText(), "")) {
                showError("Please enter an epithermal flux. e.g. 2e15");
                return;
            }
            if (Objects.equals(fastFluxField.getText(), "")) {
                showError("Please enter a fast flux. e.g. 4e15");
                return;
            }
            if (settingKey == null && DataMemory.hasRadiationSettingKey(titleField.getText())) {
                showError("That name is already taken.");
                return;
            }
            DataMemory.removeRadiationSettingFromMap(settingKey);
            DataMemory.addRadiationSettingToMap(titleField.getText(), new RadiationSettingObject(
                    new BigDecimal(thermalFluxField.getText()),
                    new BigDecimal(epithermalFluxField.getText()),
                    new BigDecimal(fastFluxField.getText()),
                    Double.parseDouble(alphaField.getText().equals("") ? "0.0" : alphaField.getText())));
            DataMemory.saveRadiationSettingMap();
            onSave.callback(titleField.getText());
        });

        loadSettings();

        addFieldListener(thermalFluxField);
        addFieldListener(epithermalFluxField);
        addFieldListener(fastFluxField);
        addFieldListener(alphaField);
    }

    /**
     * Add m^2/s to the HBox
     * @param unitBox - the box to add the m^2/s to
     */
    public void addUnit(HBox unitBox) {
        float fontSize = 14.0F;
        Text unitText = new Text("  /m");
        unitText.setFont(new Font(fontSize));

        Text superText = new Text("2");
        superText.setTranslateY(fontSize * -0.05);
        superText.setFont(new Font(fontSize * 0.65));

        Text secondUnitText = new Text("/s");
        secondUnitText.setFont(new Font(fontSize));

        unitBox.getChildren().addAll(unitText, superText, secondUnitText);
    }

    /**
     * Add decimal text formatters to each field when it's focused on
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
     * Try loading existing settings from DataMemory
     */
    private void loadSettings() {
        if (settingKey == null) return;
        if (!DataMemory.hasRadiationSettingKey(settingKey)) return;
        RadiationSettingObject setting = DataMemory.getRadiationSettingFromMap(settingKey);

        titleField.setText(settingKey);
        thermalFluxField.setText(setting.getThermalFlux().toString());
        epithermalFluxField.setText(setting.getEpithermalFlux().toString());
        fastFluxField.setText(setting.getFastFlux().toString());
        alphaField.setText(setting.getAlpha().toString());
    }

    private void showError(String text) {
        if (errorLabel == null) {
            errorLabel = new Label();
            errorLabel.setStyle("-fx-text-fill: crimson;");
            contentVBox.getChildren().add(2, errorLabel);
        }
        errorLabel.setText(text);
    }
}
