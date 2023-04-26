package com.dl630.isocalc.controller;

import com.dl630.isocalc.scene.SceneController;
import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.element.ElementCallback;
import com.dl630.isocalc.element.ElementHandler;
import com.dl630.isocalc.guielement.PeriodicButton;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class PeriodicPickerController extends SceneController implements Initializable {
    private final Boolean showAllElements; // false = show only elements with supported isotopes
    private final ElementCallback callback;

    public PeriodicPickerController(String fxmlPath, boolean showAll, ElementCallback callback) {
        super(fxmlPath);
        this.showAllElements = showAll;
        this.callback = callback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Element[] elements = ElementHandler.getAllElements();
        Map<Element, ArrayList<Integer>> isotopeMap = ElementHandler.getAllIsotopes();

        int spacing = 0;
        for (int i = 0; i < elements.length; i++) {
            switch (spacing) {
                case 1 -> spacing += 16;
                case 20 -> spacing += 10;
                case 38 -> spacing += 10;
            }
            if (i == 56 || i == 103) {
                spacing -= 14;
            }
            Element element = elements[i];
            PeriodicButton button = new PeriodicButton(element.getProtonCount(), element.getName(), element.getFullName());
            button.setId(element.getName());

            // Do some funky spacing shenanigans to align the columns of the periodic table
            if (i > 55 && i < 71) {
                button.setPrefSize(50, 50);
                button.setLayoutX(56 * (spacing - 3) + 20 - ((4) * 56 * 18));
                button.setLayoutY(48 + (7 * 56));
            } else if (i > 87 && i < 103) {
                button.setPrefSize(50, 50);
                button.setLayoutX(56 * (spacing + 1) + 20 - (6 * 56 * 18));
                button.setLayoutY(48 + (8 * 56));
            } else {
                button.setPrefSize(50, 50);
                button.setLayoutX(56.0 * spacing + 20.0 - ((spacing / 18.0) * 56.0 * 18.0));
                button.setLayoutY(20.0 + (spacing / 18.0) * 56.0);
            }

            // Determine if an element should be clickable
            if (isotopeMap.containsKey(element) || showAllElements) {
                button.setElementStyle(element.getType());
            }
            else {
                button.setDisable(true);
            }

            button.setOnAction(e -> callback.onCallback(element));

            root.getChildren().add(button);
            spacing++;
        }
    }
}
