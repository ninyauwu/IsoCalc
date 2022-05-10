package com.dl630.isocalc.scene;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public interface SceneInterface {
    Scene initScene(Stage root);
    Pane initSceneUnwrapped(Stage root);
}
