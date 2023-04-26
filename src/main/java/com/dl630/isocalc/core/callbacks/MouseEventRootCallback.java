package com.dl630.isocalc.core.callbacks;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public interface MouseEventRootCallback {
    void callback(AnchorPane root, MouseEvent e);
}
