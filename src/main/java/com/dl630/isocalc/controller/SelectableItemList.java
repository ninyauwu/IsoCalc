package com.dl630.isocalc.controller;

import com.dl630.isocalc.controller.component.TopButtonPaneController;
import com.dl630.isocalc.scene.SceneController;
import com.dl630.isocalc.core.storage.DataMemory;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public abstract class SelectableItemList<T> extends SceneController {
    @FXML
    public HBox contentHBox;

    @FXML
    public VBox contentVBox;

    @FXML
    public TopButtonPaneController topButtonPaneController;

    protected ArrayList<T> items;

    protected SelectableItemList(String fxmlPath) {
        super(fxmlPath);
        items = new ArrayList<>();
    }

    protected void addListItem(String key) {
        addListItem(key, 0);
    }
    abstract void addListItem(String key, int index);
    /**
     * Remove an item from both the GUI and the data memory
     * @param itemParent - root pane of the item to be removed
     */
    public void removeListItem(AnchorPane itemParent) {
        contentVBox.getChildren().remove(itemParent);
        DataMemory.setSelectedRadiationSettingKey(null);
        DataMemory.saveRadiationSettingMap();
    }

    /**
     * Change the styles of all items to be deselected
     */
    public void deselectAll() {
        for (Node n : contentVBox.getChildren()) {
            if (n instanceof AnchorPane) {
                n.getStyleClass().remove(0, n.getStyleClass().size());
                n.getStyleClass().add("anchorpane");
            }
        }
        updateContinueButton();
    }


    /**
     * Disable the continue button, or don't
     */
    public void updateContinueButton() {
        topButtonPaneController.continueButton.setDisable(getSelectedKey() == null);
    }

    public abstract String getSelectedKey();
}
