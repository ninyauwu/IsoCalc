package com.dl630.isocalc.core;

import javafx.beans.InvalidationListener;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.FloatPropertyBase;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ClampedFloatProperty extends FloatPropertyBase {

    private final FloatProperty value;
    private final float minClamp;

    public ClampedFloatProperty(float initialValue, float minClamp) {
        this.value = new SimpleFloatProperty(initialValue);
        this.minClamp = minClamp;
    }

    @Override
    public void set(float newValue) {
        value.set(Math.max(newValue, minClamp));
    }

    @Override
    public void setValue(Number v) {
        set(v.floatValue());
    }

    @Override
    public Object getBean() {
        return value.getBean();
    }

    @Override
    public String getName() {
        return value.getName();
    }
}
