package com.dl630.isocalc.controller.component;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.core.ClampedFloatProperty;
import com.dl630.isocalc.scene.SceneController;
import com.dl630.isocalc.element.Element;
import com.dl630.isocalc.guielement.ButtonFactory;
import com.dl630.isocalc.guielement.PeriodicButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ElementEntryController extends SceneController implements Initializable {
    @FXML
    public HBox rightBox;

    @FXML
    public HBox leftBox;

    @FXML
    public Rectangle greyRectangle;

    private Element element;
    private Integer isotope;

    private EventHandler<ActionEvent> elementButtonEvent;
    private float elementButtonSize;

    private EventHandler<ActionEvent> xButtonEvent;
    private VBox xButtonBox;

    private ArrayList<ElementEntryFieldController> entryFieldsRight;
    private ArrayList<ElementEntryFieldController> entryFieldsLeft;

    private boolean hideNatural;

    public ElementEntryController(Element element, Integer isotope) {
        this(element, isotope, null);
    }
    public ElementEntryController(Element element, Integer isotope, EventHandler<ActionEvent> elementButtonEvent) {
        super("component/element_entry.fxml");
        this.element = element;
        this.isotope = isotope;
        this.elementButtonEvent = elementButtonEvent;
        this.elementButtonSize = 0.0F;
        this.entryFieldsRight = new ArrayList<>();
        this.entryFieldsLeft = new ArrayList<>();
        this.hideNatural = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the rectangle next to the element button
        greyRectangle.setHeight(elementButtonSize + 2);
        greyRectangle.setWidth(12);

        // Add the element button to the left HBox
        Button elementButton = getNewElementButton();

        leftBox.getChildren().add(elementButton);


        // Initialize the left part of the entry
        for (ElementEntryFieldController controller : entryFieldsLeft) {
            leftBox.getChildren().add(controller.getScene().getRoot());
        }

        // Initialize the right part of the entry
        for (ElementEntryFieldController controller : entryFieldsRight) {
            rightBox.getChildren().add(controller.getScene().getRoot());
        }

        // Add X button to the right Hbox
        if (xButtonEvent != null) {
            addXButton(xButtonEvent, true);
        }
    }

    // Generate a new periodic button
    // Maybe we can move this to a separate FXML file?
    private Button getNewElementButton() {
        Button elementButton;
        if (element != null) {
            // Add element button
            elementButton = new PeriodicButton(element.getProtonCount(), element.getName(), element.getFullName(), isotope);
            ((PeriodicButton) elementButton).setElementStyle(element.getType());

            // Add (Natural) specification if isotope == 0
            if (isotope == 0) {
                elementButton.setText(element.getName() +  (hideNatural ? "" : "\n(Natural)"));
                elementButton.setTextAlignment(TextAlignment.CENTER);
                elementButton.getStylesheets().add(Main.RESOURCE_ROOT + "style/disable_full_opacity.css");
            }
        } else {
            // If element is null, just make it greyed out and say "..."
            elementButton = new Button("...");
            elementButton.setId("elementButton");
            elementButton.getStylesheets().add(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_undefined.css");
        }

        if (elementButtonSize == 0.0F) {
            ClampedFloatProperty size = new ClampedFloatProperty(0.0F, 50.0F);
            size.bind(root.heightProperty());
            elementButton.prefWidthProperty().bind(size);
            elementButton.prefHeightProperty().bind(size);
            greyRectangle.heightProperty().bind(size);
            root.setMinSize(elementButtonSize, elementButtonSize);
        } else {
            elementButton.setPrefSize(elementButtonSize, elementButtonSize);
            root.setMinSize(elementButtonSize, elementButtonSize);
        }

        elementButton.getStylesheets().add(Main.RESOURCE_ROOT + "style/disable_full_opacity.css");
        if (elementButtonEvent != null) {
            elementButton.setOnAction(elementButtonEvent);
        } else {
            elementButton.setDisable(true);
        }

        return elementButton;
    }

    // Add an X button dynamically, which can later be removed if no longer necessary
    public void addXButton(EventHandler<ActionEvent> event, boolean alignRight) {
        this.xButtonEvent = event;
        if (xButtonBox == null) {
            double insets = 18;

            // TODO: update this to work with FXML rather than buttonFactory
            Button xButton = ButtonFactory.createXButton(event, insets);

            xButtonBox = new VBox();
            xButtonBox.setMinSize(50, 50);
            xButtonBox.setPadding(new Insets(insets));
            xButtonBox.getChildren().add(xButton);

            if (!alignRight) {
                leftBox.getChildren().add(xButtonBox);
            } else {
                rightBox.getChildren().add(xButtonBox);
                AnchorPane.setRightAnchor(xButtonBox, 0.0);
            }
        }
    }

    public void addEntryField(ElementEntryFieldController elementEntryFieldController) {
        addEntryField(elementEntryFieldController, true);
    }
    public void addEntryField(ElementEntryFieldController elementEntryField, boolean alignRight) {
        if (alignRight) {
            entryFieldsRight.add(elementEntryField);
            return;
        }
        entryFieldsLeft.add(elementEntryField);
    }

    // Remove an X button if it was added
    public void removeXButton() {
        if (xButtonBox != null) {
            leftBox.getChildren().remove(xButtonBox);
            rightBox.getChildren().remove(xButtonBox);
            xButtonBox = null;
            xButtonEvent = null;
        }
    }

    /**
     * Set what the X button should do when it is clicked
     * @param event the onClick event
     */
    public void setXButtonEvent(EventHandler<ActionEvent> event) {
        this.xButtonEvent = event;
    }

    /**
     * Set what the element button should do when it is clicked, will be disabled otherwise
     * @param event the onClick event
     */
    public void setElementButtonEvent(EventHandler<ActionEvent> event) {
        this.elementButtonEvent = event;
    }

    /**
     * Set the size of the element button, will be dynamic otherwise
     * @param size the size in pixels
     */
    public void setElementButtonSize(float size) {
        this.elementButtonSize = size;
    }

    /**
     * Set whether a natural composition of isotopes should display as [Element] (Natural) or as [Element]
     * @param hide simple boolean
     */
    public void setHideNatural(boolean hide) {
        this.hideNatural = hide;
    }

    public void setElement(Element e) {
        this.element = e;
        leftBox.getChildren().remove(1, 2);
        leftBox.getChildren().add(1, getNewElementButton());
    }
}
