package com.dl630.isocalc.element;

import java.math.BigDecimal;

public class IsotopeMass {
    private final Element element;
    private final Integer isotope;
    private final BigDecimal mass;

    public IsotopeMass(Element element, Integer isotope, BigDecimal mass) {
        this.element = element;
        this.isotope = isotope;
        this.mass = mass;
    }

    public Element getElement() {
        return element;
    }

    public Integer getIsotope() {
        return isotope;
    }

    public BigDecimal getMass() {
        return mass;
    }
}
