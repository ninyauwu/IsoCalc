package com.dl630.isocalc.scene;

import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.Main;
import com.dl630.isocalc.element.ElementHandler;
import com.dl630.isocalc.guielement.PeriodicButton;
import com.dl630.isocalc.scene.transition.ZoomAndFadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class PeriodicPicker implements SceneInterface {
    @Override
    public Scene initScene(Stage root) {
        return new Scene(initSceneUnwrapped(root));
    }

    @Override
    public Pane initSceneUnwrapped(Stage root) {
        Pane pane = new Pane();
        pane.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button.css").toExternalForm());

        Element[] elements = ElementHandler.getAllElements();
        Map<Element, ArrayList<Integer>> isotopeMap = ElementHandler.getAllIsotopes();

        int spacing = 0;
        for (int i = 0; i < elements.length; i++) {
            switch(spacing) {
                case 1:
                    spacing += 16;
                    break;
                case 20:
                    spacing += 10;
                    break;
                case 38:
                    spacing += 10;
                    break;
            }
            if (i == 56 || i == 103) {
                spacing -= 14;
            }
            Element element = elements[i];
            PeriodicButton button = new PeriodicButton(element.getProtonCount(), element.getName(), element.getFullName());
            button.setId(element.getName());

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
                button.setLayoutX(56 * spacing + 20 - ((spacing / 18.0F) * 56 * 18));
                button.setLayoutY(20 + (spacing / 18.0F) * 56);
            }

            if (isotopeMap.containsKey(element)) {
                setElementStyle(button, element.getType());
            }
            else {
                button.setDisable(true);
            }

            button.setOnAction(e -> {
                ElementIsotopeList isotopeList = new ElementIsotopeList(element);
                ZoomAndFadeTransition zoomAndFadeTransition = new ZoomAndFadeTransition(this, isotopeList, ZoomAndFadeTransition.ZoomDirection.IN);
                Main.setScene(zoomAndFadeTransition);
            });

            pane.getChildren().add(button);
            spacing++;
        }
        return pane;
    }

    private void setElementStyle(Button button, Element.ElementType type) {
        // This can be replaced with type.toString()
        String styleSheet = Main.RESOURCE_ROOT +
                "style/elementbuttons/element_button_" +
                type.toString().toLowerCase() +
                ".css";
        button.getStylesheets().add(this.getClass().getResource(styleSheet).toExternalForm());
    }
}
