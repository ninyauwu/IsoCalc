package com.dl630.isocalc;

import com.dl630.isocalc.controller.MaterialMakeupListController;
import com.dl630.isocalc.scene.SceneController;
import com.dl630.isocalc.core.storage.DataMemory;
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

        DataMemory.loadSavedRadiationSettingMap();
        DataMemory.loadSavedPaths();
        DataMemory.loadSavedMasses();

        stage.getIcons().add(icon);
        stage.setTitle("IsoCalc version " + VERSION);
        stage.setWidth(DEFAULT_WIDTH);
        stage.setHeight(DEFAULT_HEIGHT);
        stage.setMinWidth(320);
        stage.setMinHeight(200);
        stage.setScene(scene);

//        setScene(swipeTransition);
//        PeriodicPickerController periodicPicker = new PeriodicPickerController("periodic_picker.fxml", false, element -> {
//            setScene(new ElementIsotopeListController(element, ElementHandler.getAllIsotopes().get(element)));
//        });
//        setScene(periodicPicker);
        MaterialMakeupListController materialMakeupListController = new MaterialMakeupListController();
        setScene(materialMakeupListController);
        currentStage.show();

//        Map<Element, ArrayList<Integer>> map = new HashMap<>();
//        ArrayList<Integer> isotopes = new ArrayList<>();
//        isotopes.add(235);
//        map.put(ElementHandler.getElementByName("U"), isotopes);
        //BeractAdapter.calculateResult(map);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setScene(SceneController controller) {
        try {
            currentStage.setScene(controller.getScene());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}