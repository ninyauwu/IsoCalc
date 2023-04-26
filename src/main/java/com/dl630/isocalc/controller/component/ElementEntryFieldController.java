package com.dl630.isocalc.controller.component;

import com.dl630.isocalc.scene.SceneController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ElementEntryFieldController extends SceneController implements Initializable {
    @FXML
    public Label titleLabel;

    @FXML
    public TextField textField;

    @FXML
    public Label unitLabel;

    private final String title;
    private final String unit;

    public ElementEntryFieldController(String title, String unit) {
        super("component/element_entry_field.fxml");
        this.title = title;
        this.unit = unit;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLabel.setText(title);
        unitLabel.setText(unit);
    }
}
