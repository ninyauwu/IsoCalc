package com.dl630.isocalc.core.storage;

class PathStructure {
    private String irradiation;
    private String decay;

    public PathStructure(String irradiation, String decay) {
        this.irradiation = irradiation;
        this.decay = decay;
    }

    public String getIrradiation() {
        return irradiation;
    }

    public void setIrradiation(String irradiation) {
        this.irradiation = irradiation;
    }

    public String getDecay() {
        return decay;
    }

    public void setDecay(String decay) {
        this.decay = decay;
    }
}
