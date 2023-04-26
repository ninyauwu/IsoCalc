package com.dl630.isocalc.controller.component;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.core.storage.DataMemory;
import com.dl630.isocalc.core.storage.MaterialMakeup;
import com.dl630.isocalc.element.IsotopeMass;
import com.dl630.isocalc.guielement.PeriodicButton;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MaterialMakeupItemController extends ListItemController {
    @FXML
    public HBox buttonBox;

    @FXML
    public GridPane isotopePane;

    @FXML
    public Label totalMassLabel;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        super.initialize(location, resourceBundle);
        updateLabels();
        int index = 0;
        for (IsotopeMass isotopeMass : DataMemory.getSavedMasses().get(settingKey).getMasses()) {
            // Add a periodic button
            PeriodicButton periodicButton = new PeriodicButton(null,
                                                               isotopeMass.getElement().getName(),
                                                               isotopeMass.getElement().getFullName(),
                                                               isotopeMass.getIsotope() == 0 ? null : isotopeMass.getIsotope());

            // Style the button
            periodicButton.setElementStyle(isotopeMass.getElement().getType());
            periodicButton.setPrefWidth(48.0);
            periodicButton.setPrefHeight(48.0);

            // Disable the button
            periodicButton.setDisable(true);
            periodicButton.getStylesheets().add(Main.RESOURCE_ROOT + "style/disable_full_opacity.css");

            // Add the button to the gridpane
            isotopePane.add(periodicButton, 0, 0);
            isotopePane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            index++;
        }

        isotopePane.maxWidthProperty().bind(root.widthProperty()
                .subtract(titleBox.widthProperty())
                .subtract(totalMassLabel.widthProperty())
                .subtract(buttonBox.widthProperty())
                .subtract(root.getPadding().getLeft())
                .subtract(root.getPadding().getRight())
                .subtract(56.0));
        isotopePane.maxWidthProperty().addListener(change -> {
            ArrayList<Node> children = new ArrayList<>(isotopePane.getChildren());
            isotopePane.getChildren().remove(0, isotopePane.getChildren().size());

            int rowIndex = 0;
            int columnIndex = 0;
            double width = 0.0;
            for (Node n : children) {
                System.out.println(columnIndex + " " + rowIndex);
                PeriodicButton b = (PeriodicButton) n;
                width += b.getWidth();
                if (b.getWidth() * 2 + isotopePane.getHgap() > isotopePane.getMaxWidth()) {
                    rowIndex++;
                    columnIndex = 0;
                    width = 0.0;

                    isotopePane.add(b, columnIndex, rowIndex);
                    columnIndex++;
                    continue;
                }
                if (width > isotopePane.getMaxWidth()) {
                    rowIndex++;
                    columnIndex = 0;
                    width = b.getWidth();
                }

                isotopePane.add(b, columnIndex, rowIndex);
                width += isotopePane.getHgap();
                columnIndex++;
            }
        });
    }

    /**
     * Set label text from data memory
     */
    @Override
    public void updateLabels() {
        super.updateLabels();
        totalMassLabel.setText("Total mass: " + DataMemory.getSavedMasses().get(settingKey).getTotalMass().toString() + "g");
    }

    @Override
    public void saveNewSettingKey() {
        MaterialMakeup material = DataMemory.getSavedMasses().get(settingKey);
        DataMemory.removeMassEntryFromMap(settingKey);
        settingKey = getTitle();
        DataMemory.addMaterial(settingKey, material);
        DataMemory.saveMasses();
    }

    public MaterialMakeupItemController(String key) {
        super("component/material_makeup_item.fxml", key);
    }
}
