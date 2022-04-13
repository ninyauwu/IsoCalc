package com.dl630.isocalc.scene;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.element.ElementHandler;
import com.dl630.isocalc.guielement.ButtonFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
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

        Button returnButton = ButtonFactory.createImageButton("Return", "");
        returnButton.setPrefSize(48.0, 48.0);
        returnButton.setOnAction(e -> {
            Main.setScene("PeriodicPicker");
        });

        Button addButton = new Button("Add");
        addButton.setPrefSize(60.0, 48.0);
        addButton.setOnAction(e -> {
            AnchorPane anchorPane = new AnchorPane();
            Label label = new Label("Pane " + (content.getChildren().size() + 1));
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

        GridPane bottomPane = new GridPane();
        bottomPane.setHgap(12);
        bottomPane.setVgap(8);
        bottomPane.setMinHeight(64);

        bottomPane.add(returnButton, 1, 1);
        bottomPane.add(addButton, 2, 1);

        for (int i = -1; i < isotopes.size(); i++) {
            // Test if any of the isotopes have already been selected, if so skip adding the button for that isotope
            boolean skip = false;
            if (ElementHandler.getSelectedIsotopes() != null) {
                if (ElementHandler.getSelectedIsotopes().containsKey(element)) {
                    for (int j = 0; j < ElementHandler.getSelectedIsotopes().get(element).size(); j++) {
                        if (i == -1) {
                            if (ElementHandler.getSelectedIsotopes().get(element).get(j) == 0) {
                                skip = true;
                                break;
                            }
                        } else {
                            if (ElementHandler.getSelectedIsotopes().get(element).get(j).equals(isotopes.get(i))) {
                                skip = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (skip) continue;

            // Add entries
            if (i == -1) {
                createIsotopeEntry(content, element, 0);
            }
            else {
                createIsotopeEntry(content, element, isotopes.get(i));
            }
        }

        return new Scene(new BorderPane(scrollPane, bottomPane, null, null, null), 400, 400);
    }

    public static void createIsotopeEntry(VBox content, Element element, Integer isotope) {
        AnchorPane anchorPane = new AnchorPane();
        Label label;
        if (isotope == 0) { label = new Label(element.getName() + " (Natural)"); }
        else { label = new Label(element.getName() + "-" + isotope); }
        AnchorPane.setLeftAnchor(label, 5.0);
        AnchorPane.setTopAnchor(label, 5.0);
        Button button = new Button("Add Isotope");

        button.setOnAction(evt -> {
            content.getChildren().remove(anchorPane);
            ElementHandler.addSelectedIsotope(element, isotope);

            // Debug print to test current selected elements
            System.out.println("\nCurrently selected isotopes:");
            for (Element isotopeElement : ElementHandler.getSelectedIsotopes().keySet()) {
                for (Integer selectedIsotope : ElementHandler.getSelectedIsotopes().get(isotopeElement)) {
                    System.out.println(isotopeElement.getName() + "-" + selectedIsotope);
                }
            }
        });
        AnchorPane.setRightAnchor(button, 5.0);
        AnchorPane.setTopAnchor(button, 5.0);
        AnchorPane.setBottomAnchor(button, 5.0);
        anchorPane.getChildren().addAll(label, button);
        content.getChildren().add(anchorPane);
    }
}
