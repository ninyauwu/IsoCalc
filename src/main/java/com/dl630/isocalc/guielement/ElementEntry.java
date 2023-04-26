package com.dl630.isocalc.guielement;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.element.Element;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class ElementEntry {
    private Element element;
    private Integer isotope;
    private Button elementButton;
    private HBox leftBox;
    private HBox rightBox;
    private AnchorPane pane;
    private VBox xButtonBox;

    private EventHandler<ActionEvent> xButtonEvent;
    private EventHandler<ActionEvent> elementButtonEvent;

    private boolean alignRight;
    private final ArrayList<ElementEntryField> entryFieldsLeft;
    private final ArrayList<ElementEntryField> entryFieldsRight;

    private Float buttonSize;
    private Float fieldSpacing;

    public ElementEntry() {
        this(null, null);
    }
    public ElementEntry(Element element) {
        this(element, null);
    }
    public ElementEntry(Element element, Integer isotope) {
        this.isotope = isotope;
        this.element = element;

        this.xButtonEvent = null;
        this.entryFieldsLeft = new ArrayList<>();
        this.entryFieldsRight = new ArrayList<>();
        this.fieldSpacing = 0.0F;
        this.buttonSize = 50.0F;
    }

    public AnchorPane getEntry(DoubleBinding width) {
        return getEntry(width, buttonSize * 0.24F);
    }
    public AnchorPane getEntry(DoubleBinding width, float fontSize) {
        pane = new AnchorPane();
        pane.prefWidthProperty().bind(width);

        leftBox = new HBox();
        leftBox.setSpacing(12);
        pane.getChildren().add(leftBox);

        rightBox = new HBox();
        rightBox.setSpacing(12);
        pane.getChildren().add(rightBox);
        AnchorPane.setRightAnchor(rightBox, 0.0);

        Rectangle greyRectangle = new Rectangle(12, buttonSize + 2);
        greyRectangle.setFill(new Color(0.8, 0.8, 0.8, 1.0));
        leftBox.getChildren().add(greyRectangle);

        if (elementButton == null) {
            elementButton = getNewElementButton();
        }
        leftBox.getChildren().add(elementButton);

        pane.setMinSize(buttonSize, buttonSize);
        elementButton.setPrefSize(buttonSize, buttonSize);
        pane.prefWidthProperty().bind(width);

        for (ElementEntryField field : entryFieldsLeft) {
            field.generateNodes(buttonSize, fontSize);
            leftBox.getChildren().add(field.getParentBox());
        }

        int i = 0;
        for (ElementEntryField field : entryFieldsRight) {
            field.generateNodes(buttonSize, fontSize);
            rightBox.getChildren().add(field.getParentBox());
            Pane spacerPane = new Pane();
            spacerPane.setMinWidth(fieldSpacing);
            if (i + 1 < entryFieldsRight.size()) rightBox.getChildren().add(spacerPane);
            i++;
        }

        if (xButtonEvent != null) {
            addXButton(xButtonEvent, alignRight);
        }


        return pane;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
        if (elementButton != null && leftBox != null) {
            elementButton = getNewElementButton();
            leftBox.getChildren().set(1, elementButton);
        }
    }

    public void setElementButtonAction(EventHandler<ActionEvent> action) {
        if (action == null) {
            elementButton.setDisable(true);
        }
        if (elementButton != null) {
            elementButton.setOnAction(action);
            elementButton.setDisable(false);
        }
        elementButtonEvent = action;
    }

    private Button getNewElementButton() {
        Button elementButton;
        if (element != null) {
            elementButton = new PeriodicButton(element.getProtonCount(), element.getName(), element.getFullName(), isotope);
            elementButton.setPrefSize(50, 50);
            ((PeriodicButton) elementButton).setElementStyle(element.getType());
        } else {
            elementButton = new Button("...");
            elementButton.setId("elementButton");
            elementButton.setPrefSize(50, 50);
            elementButton.getStylesheets().add(Main.RESOURCE_ROOT + "style/elementbuttons/element_button_undefined.css");
        }

        elementButton.getStylesheets().add(Main.RESOURCE_ROOT + "style/disable_full_opacity.css");
        if (elementButtonEvent != null) {
            elementButton.setOnAction(elementButtonEvent);
        } else {
            elementButton.setDisable(true);
        }

        return elementButton;
    }

    public void addEntryField(ElementEntryField elementEntryField) {
        addEntryField(elementEntryField, false);
    }
    public void addEntryField(ElementEntryField elementEntryField, boolean alignRight) {
        if (alignRight) {
            entryFieldsRight.add(elementEntryField);
        } else {
            entryFieldsLeft.add(elementEntryField);
        }
    }

    public void addXButton(EventHandler<ActionEvent> event, boolean alignRight) {
        this.xButtonEvent = event;
        this.alignRight = alignRight;
        if (xButtonBox == null && pane != null) {
            double insets = 18;
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

    public void removeXButton() {
        if (xButtonBox != null && pane != null) {
            leftBox.getChildren().remove(xButtonBox);
            rightBox.getChildren().remove(xButtonBox);
            xButtonBox = null;
            xButtonEvent = null;
        }
    }

    public void setButtonSize(float buttonSize) {
        this.buttonSize = buttonSize;
    }

    public void setFieldSpacing(Float fieldSpacing) {
        this.fieldSpacing = fieldSpacing;
    }
}
