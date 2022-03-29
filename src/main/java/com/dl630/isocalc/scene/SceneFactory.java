package com.dl630.isocalc.scene;

import com.dl630.isocalc.AbstractFactory;
import com.dl630.isocalc.element.ElementHandler;
import javafx.scene.Scene;

public class SceneFactory implements AbstractFactory<SceneInterface> {
    @Override
    public SceneInterface create(String sceneType) {
        if (sceneType.equals("PeriodicPicker")) {
            return new PeriodicPicker();
        } else if (sceneType.equals("IsotopeList")) {
            return new IsotopeList(ElementHandler.getElementByName("H"));
        }

        return null;
    }
}
