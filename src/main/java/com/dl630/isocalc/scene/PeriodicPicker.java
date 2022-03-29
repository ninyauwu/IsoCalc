package com.dl630.isocalc.scene;

import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.Main;
import com.dl630.isocalc.element.ElementHandler;
import com.dl630.isocalc.guielement.PeriodicButton;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class PeriodicPicker implements SceneInterface {
    @Override
    public Scene initScene(Stage root) {
        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button.css").toExternalForm());

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
                button.setLayoutX(56 * spacing + 20 - ((spacing / 18) * 56 * 18));
                button.setLayoutY(20 + (spacing / 18) * 56);
            }

            if (isotopeMap.containsKey(element)) {
                setElementStyle(button, element.getType());
            }
            else {
                button.setDisable(true);
            }

            button.setOnAction(e -> {
                IsotopeList isotopeList = new IsotopeList(element);
                root.setScene(isotopeList.initScene(root));
            });

            pane.getChildren().add(button);
            spacing++;
        }
        return scene;
    }

    private void setElementStyle(Button button, Element.ElementType type) {
        // This can be replaced with type.toString()
        switch (type) {
            case ALKALI:
                button.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_alkali.css").toExternalForm());
                break;
            case ALKALINE_EARTH:
                button.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_alkaline_earth.css").toExternalForm());
                break;
            case TRANSITION_METAL:
                button.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_transition_metal.css").toExternalForm());
                break;
            case BASIC_METAL:
                button.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_basic_metal.css").toExternalForm());
                break;
            case METALLOID:
                button.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_metalloid.css").toExternalForm());
                break;
            case NONMETAL:
                button.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_nonmetal.css").toExternalForm());
                break;
            case HALOGEN:
                button.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_halogen.css").toExternalForm());
                break;
            case NOBLE_GAS:
                button.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_noble_gas.css").toExternalForm());
                break;
            case LANTHANIDE:
                button.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_lanthanide.css").toExternalForm());
                break;
            case ACTINIDE:
                button.getStylesheets().add(this.getClass().getResource(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_actinide.css").toExternalForm());
                break;
        }
    }
}
