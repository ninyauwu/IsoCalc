package com.dl630.isocalc.controller;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.controller.component.ListIsotopeEntryController;
import com.dl630.isocalc.controller.component.TopButtonPaneController;
import com.dl630.isocalc.scene.SceneController;
import com.dl630.isocalc.core.storage.DataMemory;
import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.element.ElementHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ElementIsotopeListController extends SceneController implements Initializable {
    @FXML
    public BorderPane borderPane;
    @FXML
    public VBox contentPane;
    @FXML
    public AnchorPane topPane;
    @FXML
    TopButtonPaneController topButtonPaneController;

    private final Element element;
    private final ArrayList<Integer> isotopes;

    public ElementIsotopeListController(Element element, ArrayList<Integer> isotopes) {
        super("element_isotope_list.fxml");
        this.element = element;
        this.isotopes = isotopes;
    }

    public ElementIsotopeListController() {
        this(ElementHandler.getElementByName("Hydrogen"), new ArrayList<>());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add return button
        topButtonPaneController.initializeReturnButton("Elements", e -> {
            PeriodicPickerController periodicPicker = new PeriodicPickerController("periodic_picker.fxml", false, element -> {
                Main.setScene(new ElementIsotopeListController(element, ElementHandler.getAllIsotopes().get(element)));
            });
            Main.setScene(periodicPicker);
        });

        // Add continue button
        topButtonPaneController.initializeContinueButton("Continue", e -> {
            MaterialMakeupController materialMakeup = new MaterialMakeupController();
            Main.setScene(materialMakeup);
        });

        // Set continue button to be disabled unless isotopes are selected
        topButtonPaneController.continueButton.setDisable(true);
        updateContinueButton();

        // Isotope entry loop
        for (int i = -1; i < isotopes.size(); i++) {
            // Test if any of the isotopes have already been selected, if so skip adding the button for that isotope
            boolean skip = false;
            if (DataMemory.getCurrentSelectedIsotopes() != null) {
                if (DataMemory.getCurrentSelectedIsotopes().containsKey(element)) {
                    for (int j = 0; j < DataMemory.getCurrentSelectedIsotopes().get(element).size(); j++) {
                        if (i == -1) {
                            if (DataMemory.getCurrentSelectedIsotopes().get(element).get(j) == 0) {
                                skip = true;
                                break;
                            }
                        } else {
                            if (DataMemory.getCurrentSelectedIsotopes().get(element).get(j).equals(isotopes.get(i))) {
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
                createIsotopeEntry(contentPane, element, 0);
            }
            else {
                createIsotopeEntry(contentPane, element, isotopes.get(i));
            }
        }

        // Stuff everything into the final border pane
        //BorderPane output = new BorderPane(scrollPane, topPane, null, null, null);
        borderPane.setPrefSize(root.getWidth() - 16, root.getHeight()); // Where is this value of 16 coming from???
    }

    public void createIsotopeEntry(VBox content, Element element, Integer isotope) {
        ListIsotopeEntryController controller = new ListIsotopeEntryController(element, isotope, (evt, ctrl) -> {
            content.getChildren().remove(ctrl.root);
            DataMemory.addSelectedIsotope(element, isotope);
            updateContinueButton();

            // Debug print to test current selected elements
            System.out.println("\nCurrently selected isotopes:");
            for (Element isotopeElement : DataMemory.getCurrentSelectedIsotopes().keySet()) {
                for (Integer selectedIsotope : DataMemory.getCurrentSelectedIsotopes().get(isotopeElement)) {
                    System.out.println(isotopeElement.getName() + "-" + selectedIsotope);
                }
            }
        });

        content.getChildren().add(controller.getScene().getRoot());
    }

    /**
     * Make the continue button clickable if at least one isotope has been selected
     */
    public void updateContinueButton() {
        if (DataMemory.getCurrentSelectedIsotopes() == null) return;
        if (DataMemory.getCurrentSelectedIsotopes().isEmpty()) return;
        if (topPane.getChildren().size() > 2) return;
        topButtonPaneController.continueButton.setDisable(false);
    }
}
