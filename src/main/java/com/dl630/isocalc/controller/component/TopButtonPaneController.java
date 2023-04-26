package com.dl630.isocalc.controller.component;

import com.dl630.isocalc.scene.SceneController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

public class TopButtonPaneController extends SceneController {
    @FXML
    public Button returnButton;
    @FXML
    public ImageView returnButtonView;
    @FXML
    public Button continueButton;
    @FXML
    public ImageView continueButtonView;

    public TopButtonPaneController() {
        super("top_button_pane.fxml");
    }

    public void initializeReturnButton(String name, EventHandler<ActionEvent> event) {
        returnButton.setVisible(true);
        returnButton.setText(name);
        returnButton.setOnAction(event);
        setCosmeticButtonActions(returnButton, returnButtonView);
    }

    public void initializeContinueButton(String name, EventHandler<ActionEvent> event) {
        continueButton.setVisible(true);
        continueButton.setText(name);
        continueButton.setOnAction(event);
        setCosmeticButtonActions(continueButton, continueButtonView);
    }

    public void setCosmeticButtonActions(Button button, ImageView view) {
        returnButton.setOnMouseEntered(e -> {
            ColorAdjust hoverAdjust = new ColorAdjust();
            hoverAdjust.setBrightness(0.35);
            returnButtonView.setEffect(hoverAdjust);
        });
        returnButton.setOnMouseExited(e -> returnButtonView.setEffect(null));
        returnButton.setOnMousePressed(e -> {
            ColorAdjust pressAdjust = new ColorAdjust();
            pressAdjust.setBrightness(0.54);
            returnButtonView.setEffect(pressAdjust);
        });
    }
}
