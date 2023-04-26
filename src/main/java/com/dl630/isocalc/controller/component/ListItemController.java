package com.dl630.isocalc.controller.component;

import com.dl630.isocalc.core.callbacks.ActionEventRootCallback;
import com.dl630.isocalc.core.callbacks.MouseEventRootCallback;
import com.dl630.isocalc.scene.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ListItemController extends SceneController implements Initializable {
    @FXML
    protected HBox titleBox;

    @FXML
    protected Label titleLabel;

    protected TextField titleField;

    @FXML
    protected Button renameButton;

    @FXML
    protected Pane valuePane;

    @FXML
    protected Button removeButton;

    @FXML
    protected Button editButton;


    protected MouseEventRootCallback onClick;

    protected ActionEventRootCallback onRemove;

    protected ActionEventRootCallback onEdit;


    protected String settingKey;

    private boolean isEditing;

    protected ListItemController(String fxmlPath, String key) {
        super(fxmlPath);
        this.settingKey = key;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        valuePane.minWidthProperty().bind(root.widthProperty().multiply(0.52));

        root.setOnMouseClicked(mouseEvent -> onClick.callback((AnchorPane) root, mouseEvent));

        removeButton.setOnAction(actionEvent -> onRemove.callback((AnchorPane) root, actionEvent));

        editButton.setOnAction(actionEvent -> onEdit.callback((AnchorPane) root, actionEvent));

        renameButton.setOnAction(actionEvent -> onRename());
    }

    public void setTitle(String title) {
        if (isEditing) {
            titleField.setText(title);
        } else {
            titleLabel.setText(title);
        }
    }

    public String getTitle() {
        return isEditing ? titleField.getText() : titleLabel.getText();
    }

    public void onRename() {
        if (isEditing) {
            stopEditing();
            return;
        }
        setEditing();
    }

    public void setEditing() {
        isEditing = true;
        String title = titleLabel.getText();
        titleBox.getChildren().remove(titleLabel);

        titleField = new TextField(title);
        titleBox.getChildren().add(0, titleField);

        titleField.requestFocus();
        titleField.selectAll();
        titleField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                stopEditing();
            }
        });
        titleField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && isEditing) {
                stopEditingDoNotSave();
            }
        });
    }

    public void stopEditing() {
        isEditing = false;
        String title = titleField.getText();
        titleBox.getChildren().remove(titleField);

        titleLabel.setText(title);
        titleBox.getChildren().add(0, titleLabel);
        saveNewSettingKey();
    }

    public void stopEditingDoNotSave() {
        isEditing = false;
        titleBox.getChildren().remove(titleField);

        titleLabel.setText(settingKey);
        titleBox.getChildren().add(0, titleLabel);
    }

    public void setOnClick(MouseEventRootCallback onClick) {
        this.onClick = onClick;
    }

    public void setOnEdit(ActionEventRootCallback onEdit) {
        this.onEdit = onEdit;
    }

    public void setOnRemove(ActionEventRootCallback onRemove) {
        this.onRemove = onRemove;
    }

    public void updateLabels() {
        setTitle(settingKey);
    }

    public abstract void saveNewSettingKey();
}
