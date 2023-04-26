package com.dl630.isocalc.controller.component;

import com.dl630.isocalc.core.storage.RadiationSettingObject;
import com.dl630.isocalc.core.storage.DataMemory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RadiationSettingItemController extends ListItemController implements Initializable {
    @FXML
    public Label titleLabel;

    @FXML
    public Label thermalFluxLabel;

    @FXML
    public Label epithermalFluxLabel;

    @FXML
    public Label fastFluxLabel;

    @FXML
    public Label alphaLabel;

    public RadiationSettingItemController(String key) {
        super("component/radiation_setting_item.fxml", key);
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        super.initialize(location, resourceBundle);
        ((GridPane) valuePane).setHgap(6.0F);
        updateLabels();
    }

    /**
     * Set label text from data memory
     */
    public void updateLabels() {
        titleLabel.setText(settingKey);
        thermalFluxLabel.setText("T. Flux: "
                + DataMemory.getRadiationSettingFromMap(settingKey).getThermalFlux().toString());
        epithermalFluxLabel.setText("E. Flux: "
                + DataMemory.getRadiationSettingFromMap(settingKey).getEpithermalFlux().toString());
        fastFluxLabel.setText("F. Flux: "
                + DataMemory.getRadiationSettingFromMap(settingKey).getFastFlux().toString());
        alphaLabel.setText("Alpha: "
                + DataMemory.getRadiationSettingFromMap(settingKey).getAlpha().toString());
    }

    @Override
    public void saveNewSettingKey() {
        RadiationSettingObject radiationSettingObject = DataMemory.getSavedRadiationSettingList().get(settingKey);
        DataMemory.removeRadiationSettingFromMap(settingKey);
        settingKey = getTitle();
        DataMemory.addRadiationSettingToMap(settingKey, radiationSettingObject);
        DataMemory.saveRadiationSettingMap();
    }
}
