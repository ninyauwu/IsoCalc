package com.dl630.isocalc.guielement;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.element.Element;
import com.sun.javafx.scene.control.LabeledText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class PeriodicButton extends Button {
    private Text protonText;

    public PeriodicButton() { this(1, "ERR", "ERROR", null); }
    public PeriodicButton(int protonCount, String element, String elementFullName) {
        this(protonCount, element, elementFullName, null);
    }
    public PeriodicButton(Integer protonCount, String element, String elementFullName, Integer isotope) {
        super(isotope == null ? element : element + "-" + isotope);
        if (isotope != null) this.setFont(new Font(10));
        this.setPadding(new Insets(0));
        protonText = new Text(protonCount == null ? "" : String.valueOf(protonCount));
        protonText.setId("protonText");
        protonText.setLayoutX(4);
        protonText.setLayoutY(14);

        Label elementFullNameText = new Label(elementFullName);
        elementFullNameText.setId("elementFullNameText");

        Pane pane = new Pane();
        pane.getChildren().add(elementFullNameText);

        //Text massText = new Text(String.format("%.3f", molarMass));
        //massText.setId("massText");
        //massText.setLayoutX(10);
        //massText.setLayoutY(44);

        getChildren().add(protonText);
        //getChildren().add(elementFullNameText);
        //getChildren().add(massText);
    }

    @Override
    public ButtonSkin createDefaultSkin() {
        return new PeriodicButtonSkin(this);
    }

    @Override
    public final void setWidth(double value) {
        super.setWidth(value);
        super.setHeight(value);
        protonText.setFont(new Font(value * 0.2));
    }

    @Override
    protected final void setHeight(double value) {
        super.setHeight(value);
        super.setWidth(value);
        protonText.setFont(new Font(value * 0.2));
    }

    public void setElementStyle(Element.ElementType type) {
        String styleSheet;
        if (type == null) {
            styleSheet = Main.RESOURCE_ROOT + "style/elementbuttons/element_button_undefined.css";
        } else {
            styleSheet = Main.RESOURCE_ROOT +
                    "style/elementbuttons/element_button_" +
                    type.toString().toLowerCase() +
                    ".css";
        }
        this.getStylesheets().add(this.getClass().getResource(styleSheet).toExternalForm());
    }
}

class PeriodicButtonSkin extends ButtonSkin {
    public PeriodicButtonSkin(Button control) {
        super(control);
    }

    @Override
    protected void updateChildren() {
        Labeled labeled = getSkinnable();

        // Unforunately it is necessary to export all nodes before running super.updateChildren() as this function is necessary for rendering the label, but also removes all children
        ArrayList<Node> nodeList = new ArrayList<>();
        for (Node n : labeled.getChildrenUnmodifiable()) {
            nodeList.add(n);
        }
        super.updateChildren();
        for (Node n : nodeList) {
            getChildren().add(n);
        }
    }
}
