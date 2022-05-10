package com.dl630.isocalc.scene;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.guielement.ButtonFactory;
import com.dl630.isocalc.scene.transition.HorizontalSwipeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public abstract class IsotopeList implements SceneInterface {
    protected AnchorPane topPane;

    public AnchorPane createTopPane(String returnButtonTitle, EventHandler<ActionEvent> returnEvent) {
        AnchorPane buttonPane = new AnchorPane();
        topPane = buttonPane;

        // Add return button
        Button returnButton = ButtonFactory.createReturnButton(returnButtonTitle);
        returnButton.setFont(Font.font(16));
        returnButton.setPrefSize(132.0, 48.0);
        returnButton.setOnAction(returnEvent);
        HBox returnButtonBox = createButtonHBox();
        returnButtonBox.setMinWidth(148.0);
        returnButtonBox.getChildren().add(returnButton);

        buttonPane.getChildren().add(returnButtonBox);
        buttonPane.setLeftAnchor(returnButton, 0.0);

        return buttonPane;
    }

    public static HBox createButtonHBox() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setMinHeight(64);

        return hBox;
    }
}
