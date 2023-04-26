package com.dl630.isocalc;

import javafx.beans.property.SimpleStringProperty;

public class OutputDataModel {
    public SimpleStringProperty target;
    public SimpleStringProperty product;
    public SimpleStringProperty halfLife;
    public SimpleStringProperty h10_1m;
    public SimpleStringProperty h07;
    public SimpleStringProperty h10_shieldedAt1m;
    public SimpleStringProperty h10_atContainerSurface;

    public OutputDataModel(String target,
                           String product,
                           String halfLife,
                           String h10_1m,
                           String h07,
                           String h10_shieldedAt1m,
                           String h10_atContainerSurface) {
        this.target                 = new SimpleStringProperty(target);
        this.product                = new SimpleStringProperty(product);
        this.halfLife = new SimpleStringProperty(halfLife);
        this.h10_1m                 = new SimpleStringProperty(h10_1m);
        this.h07                    = new SimpleStringProperty(h07);
        this.h10_shieldedAt1m       = new SimpleStringProperty(h10_shieldedAt1m);
        this.h10_atContainerSurface = new SimpleStringProperty(h10_atContainerSurface);

    }

    public String getTarget() {
        return target.get();
    }

    public SimpleStringProperty targetProperty() {
        return target;
    }

    public void setTarget(String target) {
        this.target.set(target);
    }

    public String getProduct() {
        return product.get();
    }

    public SimpleStringProperty productProperty() {
        return product;
    }

    public void setProduct(String product) {
        this.product.set(product);
    }

    public String getHalfLife() {
        return halfLife.get();
    }

    public SimpleStringProperty halfLifeProperty() {
        return halfLife;
    }

    public void setHalfLife(String halfLife) {
        this.halfLife.set(halfLife);
    }

    public String getH10_1m() {
        return h10_1m.get();
    }

    public SimpleStringProperty h10_1mProperty() {
        return h10_1m;
    }

    public void setH10_1m(String h10_1m) {
        this.h10_1m.set(h10_1m);
    }

    public String getH07() {
        return h07.get();
    }

    public SimpleStringProperty h07Property() {
        return h07;
    }

    public void setH07(String h07) {
        this.h07.set(h07);
    }

    public String getH10_shieldedAt1m() {
        return h10_shieldedAt1m.get();
    }

    public SimpleStringProperty h10_shieldedAt1mProperty() {
        return h10_shieldedAt1m;
    }

    public void setH10_shieldedAt1m(String h10_shieldedAt1m) {
        this.h10_shieldedAt1m.set(h10_shieldedAt1m);
    }

    public String getH10_atContainerSurface() {
        return h10_atContainerSurface.get();
    }

    public SimpleStringProperty h10_atContainerSurfaceProperty() {
        return h10_atContainerSurface;
    }

    public void setH10_atContainerSurface(String h10_atContainerSurface) {
        this.h10_atContainerSurface.set(h10_atContainerSurface);
    }
}
