package com.dl630.isocalc.scene;

import com.dl630.isocalc.AbstractFactory;
import com.dl630.isocalc.element.ElementHandler;

public class SceneFactory implements AbstractFactory<SceneInterface> {
    @Override
    public SceneInterface create(String sceneType) {
        switch (sceneType) {
            case "PeriodicPicker":
                return new PeriodicPicker();
            case "IsotopeList":
                return new ElementIsotopeList(ElementHandler.getElementByName("H"));
            case "MaterialMakeup":
                return new MaterialMakeup();
            default:
                return null;
        }
    }
}
