package com.dl630.isocalc.scene.transition;

import com.dl630.isocalc.Main;
import com.dl630.isocalc.scene.SceneInterface;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ZoomAndFadeTransition extends SceneTransition {
    public ZoomDirection direction;
    public enum ZoomDirection {
        IN,
        OUT
    }
    public ZoomAndFadeTransition(SceneInterface currentScene, SceneInterface transitionScene, ZoomDirection direction) {
        super(currentScene, transitionScene);
        this.direction = direction;
    }

    // TODO: add zoom out transition
    @Override
    public Scene initScene(Stage root) {
        StackPane stackPane = new StackPane();
        Pane cs = currentScene.initSceneUnwrapped(root);
        Pane ts = transitionScene.initSceneUnwrapped(root);
        cs.setId("current");
        ts.setId("transition");
        stackPane.getChildren().add(ts);
        stackPane.getChildren().add(cs);

        Pane pane = new Pane();
        pane.getChildren().add(stackPane);
        Scene horizontalSwipeScene = new Scene(pane);

        Timeline timeline = new Timeline();
        double value1;
        double value2;
        double value3;
        if (direction == ZoomDirection.IN) {
            value1 = 0;
            value2 = root.getWidth();
            value3 = -root.getWidth();
        } else {
            value1 = 0;
            value2 = -root.getWidth();
            value3 = root.getWidth();
        }

        KeyValue currentValueStartX = new KeyValue(stackPane.getChildren().get(1).scaleXProperty(), 1, Interpolator.LINEAR);
        KeyValue currentValueStartY = new KeyValue(stackPane.getChildren().get(1).scaleYProperty(), 1, Interpolator.LINEAR);
        KeyFrame frameStart = new KeyFrame(Duration.seconds(0), currentValueStartX, currentValueStartY);

        KeyValue currentValueEndX = new KeyValue(stackPane.getChildren().get(1).scaleXProperty(), 1.2, Interpolator.EASE_BOTH);
        KeyValue currentValueEndY = new KeyValue(stackPane.getChildren().get(1).scaleYProperty(), 1.2, Interpolator.EASE_BOTH);
        KeyValue currentValueEndOpacity = new KeyValue(stackPane.getChildren().get(1).opacityProperty(), 0.0, Interpolator.LINEAR);
        KeyFrame frameEnd = new KeyFrame(Duration.seconds(0.3), currentValueEndX, currentValueEndY, currentValueEndOpacity);

        timeline.getKeyFrames().add(frameStart);
        timeline.getKeyFrames().add(frameEnd);
        timeline.setOnFinished(e -> {
            Main.setScene(transitionScene);
        });

        timeline.play();

        return horizontalSwipeScene;
    }

    @Override
    public Pane initSceneUnwrapped(Stage root) {
        return null;
    }

    @Override
    public Scene swapScene(Stage root) {
        Scene outScene = initScene(root);

        return outScene;
    }
}
