package com.dl630.isocalc.scene.transition;

import com.dl630.isocalc.scene.SceneInterface;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class SceneTransition implements SceneInterface {
    public SceneInterface currentScene;
    public SceneInterface transitionScene;

    public SceneTransition(SceneInterface currentScene, SceneInterface transitionScene) {
        this.currentScene = currentScene;
        this.transitionScene = transitionScene;
    }
    @Override
    public abstract Scene initScene(Stage root);
    public abstract Scene swapScene(Stage root);
}
