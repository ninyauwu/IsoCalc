package com.dl630.isocalc.guielement;

import com.sun.javafx.scene.control.LabeledText;
import javafx.geometry.HPos;
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
    public PeriodicButton() { this(1, "ERR", "ERROR"); }
    public PeriodicButton(int protonCount, String element, String elementFullName) {
        super(element);
        Text protonText = new Text(String.valueOf(protonCount));
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
