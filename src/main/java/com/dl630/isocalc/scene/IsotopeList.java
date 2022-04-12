package com.dl630.isocalc.scene;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.element.ElementHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class IsotopeList implements SceneInterface {
    private final Element element;
    private final ArrayList<Integer> isotopes;

    public IsotopeList(Element element) {
        this.element = element;
        this.isotopes = ElementHandler.getAllIsotopes().get(element);
    }
    @Override
    public Scene initScene(Stage root) {
        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        VBox content = new VBox(5);
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            AnchorPane anchorPane = new AnchorPane();
            Label label = new Label("Pane "+(content.getChildren().size()+1));
            AnchorPane.setLeftAnchor(label, 5.0);
            AnchorPane.setTopAnchor(label, 5.0);
            Button button = new Button("Remove");
            button.setOnAction(evt -> content.getChildren().remove(anchorPane));
            AnchorPane.setRightAnchor(button, 5.0);
            AnchorPane.setTopAnchor(button, 5.0);
            AnchorPane.setBottomAnchor(button, 5.0);
            anchorPane.getChildren().addAll(label, button);
            content.getChildren().add(anchorPane);
        });
        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> {
            Main.setScene("PeriodicPicker");
        });

        GridPane bottomPane = new GridPane();
        bottomPane.add(addButton, 0, 0);
        bottomPane.add(returnButton, 1, 0);

        for (int i = 0; i < isotopes.size(); i++) {
            // Test if any of the isotopes have already been selected, if so skip adding the button for that isotope
            boolean skip = false;
            if (ElementHandler.getSelectedIsotopes() != null) {
                if (ElementHandler.getSelectedIsotopes().containsKey(element)) {
                    for (int j = 0; j < ElementHandler.getSelectedIsotopes().get(element).size(); j++) {
                        if (ElementHandler.getSelectedIsotopes().get(element).get(j).equals(isotopes.get(i))) {
                            skip = true;
                            break;
                        }
                    }
                }
            }
            if (skip) continue;


            AnchorPane anchorPane = new AnchorPane();
            Label label = new Label(element.getName() + "-" + isotopes.get(i));
            AnchorPane.setLeftAnchor(label, 5.0);
            AnchorPane.setTopAnchor(label, 5.0);
            Button button = new Button("Add Element");

            int finalI = i;
            button.setOnAction(evt -> {
                content.getChildren().remove(anchorPane);
                ElementHandler.addSelectedIsotope(element, isotopes.get(finalI));

                // Debug print to test current selected elements
                System.out.println("\nCurrently selected isotopes:");
                for (Element isotopeElement : ElementHandler.getSelectedIsotopes().keySet()) {
                    for (Integer isotope : ElementHandler.getSelectedIsotopes().get(isotopeElement)) {
                        System.out.println(isotopeElement.getName() + "-" + isotope);
                    }
                }
            });
            AnchorPane.setRightAnchor(button, 5.0);
            AnchorPane.setTopAnchor(button, 5.0);
            AnchorPane.setBottomAnchor(button, 5.0);
            anchorPane.getChildren().addAll(label, button);
            content.getChildren().add(anchorPane);
        }
        return new Scene(new BorderPane(scrollPane, null, null, bottomPane, null), 400, 400);
    }
}
