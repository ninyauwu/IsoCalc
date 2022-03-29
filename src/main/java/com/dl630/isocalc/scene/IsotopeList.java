package com.dl630.isocalc.scene;

import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.element.ElementHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class IsotopeList implements SceneInterface {
    private Element element;
    private ArrayList<Integer> isotopes;

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
        for (int i = 0; i < isotopes.size(); i++) {
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
        return new Scene(new BorderPane(scrollPane, null, null, addButton, null), 400, 400);
    }
}
