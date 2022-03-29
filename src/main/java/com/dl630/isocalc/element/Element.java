package com.dl630.isocalc.element;

public class Element {
    public enum ElementType {
        ALKALI,
        ALKALINE_EARTH,
        TRANSITION_METAL,
        METALLOID,
        NONMETAL,
        BASIC_METAL,
        HALOGEN,
        NOBLE_GAS,
        LANTHANIDE,
        ACTINIDE
    }

    private String name;
    private String fullName;
    private ElementType type;
    private int protonCount;

    Element(String name, String fullName, ElementType type, int protonCount) {
        this.name = name;
        this.type = type;
        this.protonCount = protonCount;
        this.fullName = fullName;
    }

    public String getName() { return name; }
    public String getFullName() { return fullName; }
    public ElementType getType() { return type; }
    public int getProtonCount() { return protonCount; }
}
