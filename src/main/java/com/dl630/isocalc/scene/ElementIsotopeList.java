package com.dl630.isocalc.scene;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.element.ElementHandler;
import com.dl630.isocalc.guielement.ButtonFactory;
import com.dl630.isocalc.scene.transition.HorizontalSwipeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ElementIsotopeList extends IsotopeList {
    private final Element element;
    private final ArrayList<Integer> isotopes;

    public ElementIsotopeList(Element element) {
        this.element = element;
        this.isotopes = ElementHandler.getAllIsotopes().get(element);
    }
    @Override
    public Scene initScene(Stage root) {
        return new Scene(initSceneUnwrapped(root));
    }

    @Override
    public Pane initSceneUnwrapped(Stage root) {
        VBox content = new VBox(5);
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        AnchorPane buttonPane = createTopPane("Elements", e -> {
            SceneFactory factory = new SceneFactory();
            HorizontalSwipeTransition transition = new HorizontalSwipeTransition(this, factory.create("PeriodicPicker"), HorizontalSwipeTransition.HSwipeDirection.RIGHT);
            Main.setScene(transition);
        });

        // Add continue button
        updateContinueButton();

        // Isotope entry loop
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

        // Stuff everything into the final border pane
        BorderPane output = new BorderPane(scrollPane, buttonPane, null, null, null);
        output.setPrefSize(root.getWidth() - 16, root.getHeight()); // Where is this value of 16 coming from???
        return output;
    }

    public void updateContinueButton() {
        if (ElementHandler.getSelectedIsotopes() == null) return;
        if (ElementHandler.getSelectedIsotopes().isEmpty()) return;
        if (topPane.getChildren().size() > 1) return;
        Button continueButton = ButtonFactory.createContinueButton("Continue");
        continueButton.setFont(new Font(16));
        HBox continueBox = createButtonHBox();
        continueBox.getChildren().add(continueButton);
        continueBox.setMinWidth(132);
        topPane.getChildren().add(continueBox);
        topPane.setRightAnchor(continueBox, 0.0);
        continueButton.setOnAction(e -> {
            SceneFactory factory = new SceneFactory();
            HorizontalSwipeTransition swipeTransition = new HorizontalSwipeTransition(this,
                    factory.create("MaterialMakeup"),
                    HorizontalSwipeTransition.HSwipeDirection.LEFT);
            Main.setScene(swipeTransition);
        });
    }

    public void createIsotopeEntry(VBox content, Element element, Integer isotope) {
        AnchorPane anchorPane = new AnchorPane();
        Label label;
        if (isotope == 0) { label = new Label(element.getName() + " (Natural)"); }
        else { label = new Label(element.getName() + "-" + isotope); }
        label.setFont(new Font(16));
        AnchorPane.setLeftAnchor(label, 5.0);
        AnchorPane.setTopAnchor(label, 5.0);
        Button button = new Button("Add Isotope");
        button.setFont(new Font(14));

        button.setOnAction(evt -> {
            content.getChildren().remove(anchorPane);
            ElementHandler.addSelectedIsotope(element, isotope);
            updateContinueButton();

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
