package com.dl630.isocalc.core.callbacks;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

public interface ActionEventRootCallback {
    void callback(AnchorPane root, ActionEvent event);
}
