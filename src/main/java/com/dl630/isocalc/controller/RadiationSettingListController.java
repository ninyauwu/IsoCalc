package com.dl630.isocalc.controller;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.controller.component.RadiationSettingItemController;
import com.dl630.isocalc.controller.component.TopButtonPaneController;
import com.dl630.isocalc.core.storage.DataMemory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

// Manages a list of "items", that is anchor panes with controllers corresponding to radiation settings
public class RadiationSettingListController extends SelectableItemList<RadiationSettingItemController> implements Initializable {
    private Stage settingPopup;

    @FXML
    public TopButtonPaneController topButtonPaneController;

    @FXML
    public HBox contentHBox;

    @FXML
    public VBox contentVBox;

    @FXML
    public Button addButton;

    public RadiationSettingListController() {
        super("radiation_setting_list.fxml");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add return button
        topButtonPaneController.initializeReturnButton("Masses", e -> {
            if (isPopupOpen()) {
                annoyUser();
                return;
            }
            MaterialMakeupListController materialMakeup = new MaterialMakeupListController();
            Main.setScene(materialMakeup);
        });

        // Add continue button
        topButtonPaneController.initializeContinueButton("Finish", e -> {
            if (isPopupOpen()) {
                annoyUser();
                return;
            }
            FinalDataController finalDataController = new FinalDataController();
            Main.setScene(finalDataController);
        });
        updateContinueButton();

        for (String key : DataMemory.getSavedRadiationSettingListDescending().keySet()) {
            addListItem(key);
        }

        addButton.setOnAction(actionEvent -> openPopup(null));
    }

    @Override
    public String getSelectedKey() {
        return DataMemory.getSelectedRadiationSettingKey();
    }

    /**
     * Tries to remove an existing item and replace it with a new version, otherwise just adds the new one outright
     * if it doesn't exist already.
     * @param key    - key/title of the existing setting
     * @param newKey - key/title of the new setting to be added
     */
    public void reintroduceItem(String key, String newKey) {
        Platform.runLater(() -> {
            // Remove the item if it exists in the GUI already
            for (RadiationSettingItemController c : new ArrayList<>(items)) {
                if (Objects.equals(c.titleLabel.getText(), key)) {
                    items.remove(c);
                    removeListItem((AnchorPane) c.root);
                }
            }

            // Get the index of the new list item
            int keyIndex = -1;
            for (String k : DataMemory.getSavedRadiationSettingList().keySet()) {
                keyIndex++;
                System.out.println(k + " " + newKey);
                if (k.equals(newKey)) break;
            }

            // Add the item
            addListItem(newKey, keyIndex);
        });
    }

    /**
     * Refer to below
     * @param key
     */
    public void addListItem(String key) {
        addListItem(key, 0);
    }

    /**
     * Adds a new setting item to the list
     * @param key   - key/title of the item
     * @param index - index in the content's children that the item should be inserted at
     */
    public void addListItem(String key, int index) {
        if (DataMemory.getRadiationSettingFromMap(key) == null) return;

        // Create the controller
        RadiationSettingItemController radiationItem = new RadiationSettingItemController(key);

        // Add a callback for when the item is clicked
        radiationItem.setOnClick((root, event) -> {
            deselectAll();                                   // Deselect all items
            DataMemory.setSelectedRadiationSettingKey(key);              // Save the selected key
            root.getStyleClass().add("anchorpane-selected"); // Visually change the style to be selected
            updateContinueButton();
        });

        // Add a callback for when the edit button is clicked
        radiationItem.setOnEdit((root, event) -> {
            openPopup(key);
        });

        // Add a callback for when the X button is clicked
        radiationItem.setOnRemove((root, event) -> {
            items.remove(radiationItem);
            removeListItem((AnchorPane) radiationItem.root);
            DataMemory.removeRadiationSettingFromMap(key);
            DataMemory.saveRadiationSettingMap();
            if (Objects.equals(DataMemory.getSelectedRadiationSettingKey(), key)) DataMemory.setSelectedRadiationSettingKey(null);
            updateContinueButton();
        });

        // Bind the width to 2/3 the width of the window
        AnchorPane itemRoot = (AnchorPane) radiationItem.getScene().getRoot();
        itemRoot.prefWidthProperty().bind(root.widthProperty().multiply(0.66));

        // Add the item to the parent
        contentVBox.getChildren().add(index, itemRoot);
        items.add(radiationItem);

        // Set the style to selected if this setting has been selected previously
        if (Objects.equals(key, DataMemory.getSelectedRadiationSettingKey())) itemRoot.getStyleClass().add("anchorpane-selected");
    }

    /**
     * Opens the popup for editing/creating a radiation setting
     * @param key - key/title of the setting to be edited, null if a new one is created
     */
    public void openPopup(String key) {
        if (isPopupOpen()) settingPopup.close();
        settingPopup = new Stage();
        settingPopup.setResizable(false);
        RadiationSettingController settingPopupController = new RadiationSettingController(key, (newKey) -> {
            // If the name/title/key was unchanged
            if (Objects.equals(key, newKey)) {
                // Update the flux labels on the item
                for (RadiationSettingItemController c : items) {
                    if (Objects.equals(c.titleLabel.getText(), key)) {
                        c.updateLabels();
                    }
                }
            } else {
                // Remove the selected item key from memory if it was selected
                if (Objects.equals(DataMemory.getSelectedRadiationSettingKey(), key)) DataMemory.setSelectedRadiationSettingKey(null);
                updateContinueButton();

                // Remove and re-add the item to the GUI with a new title
                reintroduceItem(key, newKey);
            }

            // Close the popup
            settingPopup.close();
        });

        settingPopup.setScene(settingPopupController.getScene());
        settingPopup.show();
        settingPopup.setAlwaysOnTop(true);
    }

    public boolean isPopupOpen() {
        if (settingPopup != null) {
            return settingPopup.isShowing();
        }
        return false;
    }

    /**
     * Bring the popup into focus and play an alert sound
     */
    public void annoyUser() {
        settingPopup.setIconified(false);
        settingPopup.toFront();
        java.awt.Toolkit.getDefaultToolkit().beep();
    }
}
