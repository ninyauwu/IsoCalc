package com.dl630.isocalc.scene;

import com.dl630.isocalc.BeractAdapter;
import com.dl630.isocalc.DecimalTextFormatter;
import com.dl630.isocalc.Main;
import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.element.ElementHandler;
import com.dl630.isocalc.guielement.ButtonFactory;
import com.dl630.isocalc.scene.transition.HorizontalSwipeTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MaterialMakeup extends IsotopeList {

    @Override
    public Scene initScene(Stage root) {
        return new Scene(initSceneUnwrapped(root));
    }

    @Override
    public Pane initSceneUnwrapped(Stage root) {
        VBox content = new VBox(5);
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        AnchorPane buttonPane = createTopPane("Back", e -> {
            SceneFactory factory = new SceneFactory();
            HorizontalSwipeTransition transition = new HorizontalSwipeTransition(this, factory.create("PeriodicPicker"), HorizontalSwipeTransition.HSwipeDirection.RIGHT);
            Main.setScene(transition);
        });

        // Isotope entry loop
        for (Element element : ElementHandler.getSelectedIsotopes().keySet()) {
            for (Integer isotope : ElementHandler.getSelectedIsotopes().get(element)) {
                createIsotopeEntry(content, element, isotope);
            }
        }


        // Stuff everything into the final border pane
        BorderPane output = new BorderPane(scrollPane, buttonPane, null, null, null);
        output.setPrefSize(root.getWidth() - 16, root.getHeight()); // Where is this value of 16 coming from???
        return output;
    }

    public void createIsotopeEntry(VBox content, Element element, Integer isotope) {
        AnchorPane anchorPane = new AnchorPane();
        Label label;
        if (isotope == 0) { label = new Label(element.getName() + " (Natural)"); }
        else { label = new Label(element.getName() + "-" + isotope); }
        label.setFont(new Font(16));
        AnchorPane.setLeftAnchor(label, 5.0);
        AnchorPane.setTopAnchor(label, 5.0);

        TextField percentageField = new TextField();
        percentageField.setPrefWidth(80);
        percentageField.setTextFormatter(new DecimalTextFormatter(0, 99, false));
        percentageField.setFont(new Font(14));

        Label percentageSign = new Label("%");
        percentageSign.setFont(new Font(16));

        Button removeButton = new Button("Remove Isotope");
        removeButton.setFont(new Font(14));
        removeButton.setOnAction(e -> {
            ElementHandler.removeSelectedIsotope(element, isotope);
            for (int i = 0; i < content.getChildren().size(); i++) {
                if (anchorPane.equals(content.getChildren().get(i))) {
                    content.getChildren().remove(i);
                }
            }
        });
        HBox removeButtonBox = new HBox(removeButton);
        removeButtonBox.setAlignment(Pos.CENTER);
        removeButtonBox.setMinWidth(144.0);

        HBox rightBox = new HBox();
        rightBox.setSpacing(10.0);
        rightBox.setAlignment(Pos.CENTER_LEFT);
        rightBox.getChildren().add(removeButtonBox);
        rightBox.getChildren().add(percentageField);
        rightBox.getChildren().add(percentageSign);


//        button.setOnAction(evt -> {
//            content.getChildren().remove(anchorPane);
//            ElementHandler.addSelectedIsotope(element, isotope);
//            updateContinueButton();
//
//            // Debug print to test current selected elements
//            System.out.println("\nCurrently selected isotopes:");
//            for (Element isotopeElement : ElementHandler.getSelectedIsotopes().keySet()) {
//                for (Integer selectedIsotope : ElementHandler.getSelectedIsotopes().get(isotopeElement)) {
//                    System.out.println(isotopeElement.getName() + "-" + selectedIsotope);
//                }
//            }
//        });
        AnchorPane.setRightAnchor(rightBox, 12.0);
        AnchorPane.setTopAnchor(rightBox, 5.0);
        AnchorPane.setBottomAnchor(rightBox, 5.0);
        anchorPane.getChildren().addAll(rightBox, label);
        content.getChildren().add(anchorPane);
    }
}
