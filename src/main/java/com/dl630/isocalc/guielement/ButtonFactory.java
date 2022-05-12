package com.dl630.isocalc.guielement;

import com.dl630.isocalc.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;

public class ButtonFactory {
    public static Button createImageButton(String title, String imageLocation) {
        Button button = new Button(title);
        Image img = new Image(Main.RESOURCE_ROOT + imageLocation);
        ImageView view = new ImageView(img);
        view.setFitHeight(32);
        view.setPreserveRatio(true);
        button.setGraphic(view);
        button.getStylesheets().add(ButtonFactory.class.getResource(Main.RESOURCE_ROOT + "style/return_button.css").toExternalForm());
        button.setOnMouseEntered(e -> {
            ColorAdjust hoverAdjust = new ColorAdjust();
            hoverAdjust.setBrightness(0.35);
            view.setEffect(hoverAdjust);
        });
        button.setOnMouseExited(e -> view.setEffect(null));
        button.setOnMousePressed(e -> {
            ColorAdjust pressAdjust = new ColorAdjust();
            pressAdjust.setBrightness(0.54);
            view.setEffect(pressAdjust);
        });
        return button;
    }

    public static Button createReturnButton() { return createReturnButton(""); }
    public static Button createReturnButton(String title) {
        Button returnButton = createImageButton(title, "img/arrow_left_small.png");
        returnButton.setAlignment(Pos.CENTER_LEFT);
        returnButton.setGraphicTextGap(18);
        return returnButton;
    }
    public static Button createContinueButton() { return createContinueButton(""); }
    public static Button createContinueButton(String title) { return createContinueButton(title, e -> {}); }
    public static Button createContinueButton(String title, EventHandler<ActionEvent> event) {
        Button continueButton = createImageButton(title, "img/arrow_right_small.png");
        continueButton.setAlignment(Pos.CENTER_RIGHT);
        continueButton.setContentDisplay(ContentDisplay.RIGHT);
        continueButton.setGraphicTextGap(18);
        continueButton.setOnAction(event);
        return continueButton;
    }
}
