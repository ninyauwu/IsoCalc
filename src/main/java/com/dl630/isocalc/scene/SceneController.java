package com.dl630.isocalc.scene;

import com.dl630.isocalc.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

public abstract class SceneController {
    @FXML
    public Pane root;

    private final String fxmlPath;

    private Scene scene;

    protected SceneController(String fxmlPath) {
        this.fxmlPath = Main.RESOURCE_ROOT + "fxml/" + fxmlPath;
        scene = null;
    }

    public String getFXMLPath() {
        return this.fxmlPath;
    }


    /**
     * Get the Scene from this controller, or create a new one if it doesn't already exist
     * @return the scene
     */
    public Scene getScene() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
        loader.setController(this);
        try {
            if (scene == null) scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scene;
    }

}
