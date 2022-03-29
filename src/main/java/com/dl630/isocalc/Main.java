package com.dl630.isocalc;

import com.dl630.isocalc.scene.PeriodicPicker;
import com.dl630.isocalc.scene.SceneFactory;
import com.dl630.isocalc.scene.SceneInterface;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {
    public static final String RESOURCE_ROOT = "/com/dl630/isocalc/";
    public static final String RESOURCE_ROOT_COMPLETE = "src/main/resources/com/dl630/isocalc/";
    public static final String VERSION = "0.1";
    private static final int DEFAULT_WIDTH = 1060;
    private static final int DEFAULT_HEIGHT = 620;

    public static Stage currentStage;

    @Override
    public void start(Stage stage) {
        currentStage = stage;

        Group root = new Group();
        Color backgroundColor = new Color(0.96, 0.96, 0.96, 1.0);
        Scene scene = new Scene(root, backgroundColor);
        Image icon = new Image("com/dl630/isocalc/img/icon.png");

        stage.getIcons().add(icon);
        stage.setTitle("IsoCalc version " + VERSION);
        stage.setWidth(DEFAULT_WIDTH);
        stage.setHeight(DEFAULT_HEIGHT);
        stage.setMinWidth(320);
        stage.setMinHeight(200);
        stage.setScene(scene);

        setScene("PeriodicPicker");
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setScene(String sceneType) {
        SceneFactory factory = new SceneFactory();
        SceneInterface scene = factory.create(sceneType);
        setScene(scene);
    }
    public static void setScene(SceneInterface scene) {
        currentStage.setScene(scene.initScene(currentStage));
        currentStage.show();
    }

}