package com.dl630.isocalc.core.storage;

import java.math.BigDecimal;

public class RadiationSettingObject {
    private BigDecimal thermalFlux;
    private BigDecimal epithermalFlux;
    private BigDecimal fastFlux;
    private Double alpha;

    public RadiationSettingObject() {}
    public RadiationSettingObject(BigDecimal thermalFlux, BigDecimal epithermalFlux, BigDecimal fastFlux, Double alpha) {
        this.thermalFlux = thermalFlux;
        this.epithermalFlux = epithermalFlux;
        this.fastFlux = fastFlux;
        this.alpha = alpha;
    }

    public BigDecimal getThermalFlux() {
        return thermalFlux;
    }

    public void setThermalFlux(BigDecimal thermalFlux) {
        this.thermalFlux = thermalFlux;
    }

    public BigDecimal getEpithermalFlux() {
        return epithermalFlux;
    }

    public void setEpithermalFlux(BigDecimal epithermalFlux) {
        this.epithermalFlux = epithermalFlux;
    }

    public BigDecimal getFastFlux() {
        return fastFlux;
    }

    public void setFastFlux(BigDecimal fastFlux) {
        this.fastFlux = fastFlux;
    }

    public Double getAlpha() {
        return alpha;
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }
}
