package com.dl630.isocalc.scene;

import com.dl630.isocalc.DecimalTextFormatter;
import com.dl630.isocalc.Main;
import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.element.ElementHandler;
import com.dl630.isocalc.guielement.ButtonFactory;
import com.dl630.isocalc.scene.transition.HorizontalSwipeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;

public class MaterialMakeup extends IsotopeList {
    private TextField totalPercentageField;
    private ArrayList<IsotopeMakeupFieldStructure> fieldList = new ArrayList<>();
    private HBox continueBox;
    private Tooltip continueBoxTooltip; // Really no clue how to reference this after instantiation, so storing it as an attribute

    @Override
    public Scene initScene(Stage root) {
        return new Scene(initSceneUnwrapped(root));
    }

    @Override
    public Pane initSceneUnwrapped(Stage root) {
        VBox content = new VBox(5);
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        // Create top of borderpane with return button
        AnchorPane buttonPane = createTopPane("Back", e -> {
            SceneFactory factory = new SceneFactory();
            HorizontalSwipeTransition transition = new HorizontalSwipeTransition(this, factory.create("PeriodicPicker"), HorizontalSwipeTransition.HSwipeDirection.RIGHT);
            Main.setScene(transition);
        });

        // Add continue button
        Button continueButton = ButtonFactory.createContinueButton("Continue", e -> {
            SceneFactory factory = new SceneFactory();
            HorizontalSwipeTransition swipeTransition = new HorizontalSwipeTransition(this,
                    factory.create("PeriodicPicker"),
                    HorizontalSwipeTransition.HSwipeDirection.LEFT);
            Main.setScene(swipeTransition);
        });
        continueButton.setFont(new Font(16));

        continueBox = createButtonHBox();
        continueBox.getChildren().add(continueButton);
        continueBox.setMinWidth(132);
        continueButton.setDisable(true);

        // Add tooltip to continue button
        addContinueButtonTooltip();

        buttonPane.getChildren().add(continueBox);
        buttonPane.setRightAnchor(continueBox, 0.0);


        // Isotope entry loop
        for (Element element : ElementHandler.getSelectedIsotopes().keySet()) {
            for (Integer isotope : ElementHandler.getSelectedIsotopes().get(element)) {
                TextField field = createIsotopeEntry(content, element, isotope);
                fieldList.add(new IsotopeMakeupFieldStructure(element, isotope, field));
            }
        }

        // Add percentage total
        AnchorPane totalPane = new AnchorPane();
        Label totalPercentageLabel = new Label("Total");
        totalPercentageLabel.setFont(new Font(16));

        totalPane.getChildren().add(totalPercentageLabel);
        totalPane.setLeftAnchor(totalPercentageLabel, 5.0);
        totalPane.setTopAnchor(totalPercentageLabel, 5.0);

        // Create total percentage field
        totalPercentageField = new TextField("0.0");
        totalPercentageField.setPrefWidth(80);
        totalPercentageField.setFont(new Font(14));
        totalPercentageField.setStyle("-fx-text-fill: red;");
        totalPercentageField.setEditable(false);

        // Add tooltip to total percentage field
        addTotalPercentageTooltip();

        Label percentageSign = new Label("%");
        percentageSign.setFont(new Font(16));

        HBox rightBox = new HBox();
        rightBox.setSpacing(10.0);
        rightBox.setAlignment(Pos.CENTER_LEFT);
        rightBox.getChildren().add(totalPercentageField);
        rightBox.getChildren().add(percentageSign);

        totalPane.setRightAnchor(rightBox, 12.0);
        totalPane.setTopAnchor(rightBox, 5.0);
        totalPane.setBottomAnchor(rightBox, 5.0);
        totalPane.getChildren().addAll(rightBox);
        content.getChildren().add(totalPane);

        // Stuff everything into the final border pane
        BorderPane output = new BorderPane(scrollPane, buttonPane, null, null, null);
        output.setPrefSize(root.getWidth() - 16, root.getHeight()); // Where is this value of 16 coming from???
        return output;
    }

    public TextField createIsotopeEntry(VBox content, Element element, Integer isotope) {
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
        percentageField.setOnKeyReleased(e -> {
            updateTotalField();
        });

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

        return percentageField;
    }

    public void updateTotalField() {
        if (fieldList.size() < 1) { totalPercentageField.setText("0.0"); }

        Float percentage = 0.0F;
        for (int i = 0; i < fieldList.size(); i++) {
            if (fieldList.get(i).field.getText().length() < 1) continue;
            percentage += Float.valueOf(fieldList.get(i).field.getText());
        }

        if (percentage.equals(100.0F)) {
            totalPercentageField.setStyle("-fx-text-fill: black;");
            Tooltip.uninstall(totalPercentageField, totalPercentageField.getTooltip());
            continueBox.getChildren().get(0).setDisable(false);
            Tooltip.uninstall(continueBox, continueBoxTooltip);
        } else {
            totalPercentageField.setStyle("-fx-text-fill: red;");
            addTotalPercentageTooltip();
            continueBox.getChildren().get(0).setDisable(true);
            addContinueButtonTooltip();
        }

        totalPercentageField.setText(percentage.toString());
    }

    public void addTotalPercentageTooltip() {
        Tooltip totalPercentageTooltip = new Tooltip("Percentages must add up to 100");
        totalPercentageTooltip.setShowDelay(new Duration(1));
        totalPercentageTooltip.setHideDelay(new Duration(1));
        totalPercentageTooltip.setFont(new Font(14));
        Tooltip.install(totalPercentageField, totalPercentageTooltip);
    }

    public void addContinueButtonTooltip() {
        Tooltip percentageTooltip = new Tooltip("Cannot continue: Current percentages do not add up to 100");
        percentageTooltip.setShowDelay(new Duration(1));
        percentageTooltip.setHideDelay(new Duration(1));
        percentageTooltip.setFont(new Font(14));
        continueBoxTooltip = percentageTooltip;
        Tooltip.install(continueBox, percentageTooltip);
    }
}

class IsotopeMakeupFieldStructure {
    Element element;
    Integer isotope;
    TextField field;

    public IsotopeMakeupFieldStructure(Element element, Integer isotope, TextField field) {
        this.element = element;
        this.isotope = isotope;
        this.field = field;
    }
}
