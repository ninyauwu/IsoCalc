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

public class HorizontalSwipeTransition extends SceneTransition {
    public HSwipeDirection direction;
    public enum HSwipeDirection {
        LEFT,
        RIGHT
    }
    public HorizontalSwipeTransition(SceneInterface currentScene, SceneInterface transitionScene, HSwipeDirection direction) {
        super(currentScene, transitionScene);
        this.direction = direction;
    }

    @Override
    public Scene initScene(Stage root) {
        StackPane stackPane = new StackPane();
        Pane cs = currentScene.initSceneUnwrapped(root);
        Pane ts = transitionScene.initSceneUnwrapped(root);
        cs.setId("current");
        ts.setId("transition");
        stackPane.getChildren().add(cs);
        stackPane.getChildren().add(ts);

        Pane pane = new Pane();
        pane.getChildren().add(stackPane);
        Scene horizontalSwipeScene = new Scene(pane);

        Timeline timeline = new Timeline();
        double value1;
        double value2;
        double value3;
        if (direction == HSwipeDirection.LEFT) {
            value1 = 0;
            value2 = root.getWidth();
            value3 = -root.getWidth();
        } else {
            value1 = 0;
            value2 = -root.getWidth();
            value3 = root.getWidth();
        }

        KeyValue currentValueStart = new KeyValue(stackPane.getChildren().get(0).translateXProperty(), value1, Interpolator.LINEAR);
        KeyValue transitionValueStart = new KeyValue(stackPane.getChildren().get(1).translateXProperty(), value2, Interpolator.LINEAR);
        KeyFrame frameStart = new KeyFrame(Duration.seconds(0), currentValueStart, transitionValueStart);

        KeyValue currentValueEnd = new KeyValue(stackPane.getChildren().get(0).translateXProperty(), value3, Interpolator.EASE_BOTH);
        KeyValue transitionValueEnd = new KeyValue(stackPane.getChildren().get(1).translateXProperty(), value1, Interpolator.EASE_BOTH);
        KeyFrame frameEnd = new KeyFrame(Duration.seconds(0.32), currentValueEnd, transitionValueEnd);

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
        return initScene(root);
    }
}
