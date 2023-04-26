package com.dl630.isocalc.core.storage;

import com.dl630.isocalc.element.IsotopeMass;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MaterialMakeup extends VersionedData{
    private ArrayList<IsotopeMass> masses;
    private BigDecimal totalMass;
    private boolean totalMassUnchanged;

    public MaterialMakeup() {
        super("0.1");
        totalMassUnchanged = true;
    }

    public ArrayList<IsotopeMass> getMasses() {
        if (masses == null) masses = new ArrayList<>();
        return masses;
    }

    public void setMasses(ArrayList<IsotopeMass> masses) {
        this.masses = masses;
    }

    public BigDecimal getTotalMass() {
        return totalMass;
    }

    public void setTotalMass(BigDecimal totalMass) {
        this.totalMass = totalMass;
    }

    public void setTotalMassUnchanged(boolean totalMassUnchanged) {
        this.totalMassUnchanged = totalMassUnchanged;
    }

    public boolean isTotalMassUnchanged() {
        return totalMassUnchanged;
    }
}
