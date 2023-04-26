package com.dl630.isocalc.controller.component;

import com.dl630.isocalc.core.callbacks.ListIsotopeEntryCallback;
import com.dl630.isocalc.scene.SceneController;
import com.dl630.isocalc.element.Element;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ListIsotopeEntryController extends SceneController implements Initializable {
    @FXML
    public Button isotopeButton;

    @FXML
    public Label isotopeLabel;

    private final Element element;
    private final int isotope;
    private final ListIsotopeEntryCallback callback;

    public ListIsotopeEntryController(Element element, int isotope, ListIsotopeEntryCallback callback) {
        super("list_isotope_entry.fxml");
        this.element = element;
        this.isotope = isotope;
        this.callback = callback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isotopeButton.setText("Add Isotope");
        isotopeLabel.setText(element.getName() + (isotope == 0 ? " (Natural)" : "-" + isotope));
        isotopeButton.setOnAction(e -> callback.callback(e, this));
    }

    public Element getElement() {
        return element;
    }

    public int getIsotope() {
        return isotope;
    }
}
