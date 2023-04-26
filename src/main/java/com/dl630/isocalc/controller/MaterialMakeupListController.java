package com.dl630.isocalc.controller;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.controller.component.MaterialMakeupItemController;
import com.dl630.isocalc.controller.component.TopButtonPaneController;
import com.dl630.isocalc.core.storage.DataMemory;
import com.dl630.isocalc.element.ElementHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MaterialMakeupListController extends SelectableItemList<MaterialMakeupItemController> implements Initializable {
    @FXML
    public TopButtonPaneController topButtonPaneController;

    @FXML
    public Button addButton;

    @FXML
    public Button importButton;

    public MaterialMakeupListController() {
        super("material_makeup_list.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add continue button
        topButtonPaneController.initializeContinueButton("Continue", e -> {
            RadiationSettingListController radiationSettingListController = new RadiationSettingListController();
            Main.setScene(radiationSettingListController);
        });
        updateContinueButton();

        for (String key : DataMemory.getSavedMasses().keySet()) {
            addListItem(key);
        }

        addButton.setOnAction(actionEvent -> {
            String name = "Unnamed Material";
            int i = 1;
            while (DataMemory.getSavedMasses().get(i == 1 ? name : name + " " + i) != null) {
                i++;
            }
            DataMemory.setCurrentMaterialSettingKey(i == 1 ? name : name + " " + i);
            DataMemory.clearSelectedIsotopes();
            PeriodicPickerController periodicPicker = new PeriodicPickerController("periodic_picker.fxml", false, element -> {
                Main.setScene(new ElementIsotopeListController(element, ElementHandler.getAllIsotopes().get(element)));
            });
            Main.setScene(periodicPicker);
        });
    }

    @Override
    public void addListItem(String key, int index) {
        if (DataMemory.getSavedMasses().get(key) == null) return;

        // Create the controller
        MaterialMakeupItemController materialItem = new MaterialMakeupItemController(key);

        // Add a callback for when the item is clicked
        materialItem.setOnClick((root, event) -> {
            deselectAll();                                   // Deselect all items
            DataMemory.setSelectedMaterialSettingKey(key);              // Save the selected key
            root.getStyleClass().add("anchorpane-selected"); // Visually change the style to be selected
            updateContinueButton();
        });

        // Add a callback for when the edit button is clicked
        materialItem.setOnEdit((root, event) -> {
            DataMemory.setCurrentMaterialSettingKey(key);
            DataMemory.resetCurrentIsotopes();
            PeriodicPickerController periodicPicker = new PeriodicPickerController("periodic_picker.fxml", false, element -> {
                Main.setScene(new ElementIsotopeListController(element, ElementHandler.getAllIsotopes().get(element)));
            });
            Main.setScene(periodicPicker);
        });

        // Add a callback for when the X button is clicked
        materialItem.setOnRemove((root, event) -> {
            items.remove(materialItem);
            removeListItem((AnchorPane) materialItem.root);
            DataMemory.removeMassEntryFromMap(key);
            DataMemory.saveMasses();
            updateContinueButton();
        });

        // Bind the width to 2/3 the width of the window
        AnchorPane itemRoot = (AnchorPane) materialItem.getScene().getRoot();
        itemRoot.prefWidthProperty().bind(root.widthProperty().multiply(0.66));

        // Add the item to the parent
        contentVBox.getChildren().add(index, itemRoot);
        items.add(materialItem);

        // Set the style to selected if this setting has been selected previously
        if (Objects.equals(key, getSelectedKey())) itemRoot.getStyleClass().add("anchorpane-selected");
    }

    @Override
    public String getSelectedKey() {
        return DataMemory.getSelectedMaterialSettingKey();
    }
}
