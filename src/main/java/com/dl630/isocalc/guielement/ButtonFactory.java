package com.dl630.isocalc.guielement;

import com.dl630.isocalc.Main;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonFactory {
    public static Button createImageButton(String title, String imageLocation) {
        Button button = new Button();
        Image img = new Image(Main.RESOURCE_ROOT + "img/arrow_left_small.png");
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
}
