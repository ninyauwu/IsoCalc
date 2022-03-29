package com.dl630.isocalc.element;

import com.dl630.isocalc.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ElementHandler {
    private static Element[] allElements;
    private static Map<Element, ArrayList<Integer>> isotopeMap;
    private static Map<Element, ArrayList<Integer>> savedIsotopeMap;

    public static Map<Element, ArrayList<Integer>> getAllIsotopes() {
        if (isotopeMap != null) { return isotopeMap; }

        isotopeMap = new HashMap<>();
        File isotopeList = new File(Main.RESOURCE_ROOT_COMPLETE + "core/targets.txt");
        try {
            // Try to read the target list file
            Scanner scanner = new Scanner(isotopeList);
            while (scanner.hasNextLine()) {
                String scanLine = scanner.nextLine();

                // Isotopes are separated by spaces
                String[] splitLine = scanLine.split(" ");
                if (splitLine.length > 1) {
                    // Get the element by the first word in the line
                    Element e = getElementByName(splitLine[0]);
                    // Get all the isotopes that follow in the form of Integers
                    ArrayList<Integer> lineIsotopes = new ArrayList<>();
                    for (int i = 1; i < splitLine.length; i++) {
                        lineIsotopes.add(Integer.valueOf(splitLine[i].split("-")[1]));
                    }

                    // Insert the element and isotope integers into the list
                    isotopeMap.put(e, lineIsotopes);
                }
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return isotopeMap;
    }

    public static void addSelectedIsotope(Element element, Integer isotope) {
        if (savedIsotopeMap == null) { savedIsotopeMap = new HashMap<>(); }

        for (Element e : savedIsotopeMap.keySet()) {
            if (element == e) {
                savedIsotopeMap.get(e).add(isotope);
                return;
            }
        }

        // Add new map entry if map doesn't contain element
        ArrayList<Integer> isotopeList = new ArrayList<>();
        isotopeList.add(isotope);
        savedIsotopeMap.put(element, isotopeList);
    }

    public static Map<Element, ArrayList<Integer>> getSelectedIsotopes() {
        return savedIsotopeMap;
    }

    public static Element getElementByName(String name) {
        if (allElements == null) getAllElements();
        for (Element e : allElements) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

    public static Element[] getAllElements() {
        if (allElements != null) return allElements;

        allElements = new Element[118];
        for (int i = 0; i < allElements.length; i++) {
            float returnMass; // Mass is currently not implemented as it was considered out of scope
            Element.ElementType returnType;
            String returnName;
            String returnFullName;
            switch(i) {
                case 0:
                    returnName = "H";
                    returnFullName = "Hydrogen";
                    returnType = Element.ElementType.NONMETAL;
                    break;
                case 1:
                    returnName = "He";
                    returnFullName = "Helium";
                    returnType = Element.ElementType.NOBLE_GAS;
                    break;
                case 2:
                    returnName = "Li";
                    returnFullName = "Lithium";
                    returnType = Element.ElementType.ALKALI;
                    break;
                case 3:
                    returnName = "Be";
                    returnFullName = "Beryllium";
                    returnType = Element.ElementType.ALKALINE_EARTH;
                    break;
                case 4:
                    returnName = "B";
                    returnFullName = "Boron";
                    returnType = Element.ElementType.METALLOID;
                    break;
                case 5:
                    returnName = "C";
                    returnFullName = "Carbon";
                    returnType = Element.ElementType.NONMETAL;
                    break;
                case 6:
                    returnName = "N";
                    returnFullName = "Neon";
                    returnType = Element.ElementType.NONMETAL;
                    break;
                case 7:
                    returnName = "O";
                    returnFullName = "Oxygen";
                    returnType = Element.ElementType.NONMETAL;
                    break;
                case 8:
                    returnName = "F";
                    returnFullName = "Fluoride";
                    returnType = Element.ElementType.HALOGEN;
                    break;
                case 9:
                    returnName = "Ne";
                    returnFullName = "Neon";
                    returnType = Element.ElementType.NOBLE_GAS;
                    break;
                case 10:
                    returnName = "Na";
                    returnFullName = "Sodium";
                    returnType = Element.ElementType.ALKALI;
                    break;
                case 11:
                    returnName = "Mg";
                    returnFullName = "Magnesium";
                    returnType = Element.ElementType.ALKALINE_EARTH;
                    break;
                case 12:
                    returnName = "Al";
                    returnFullName = "Aluminum";
                    returnType = Element.ElementType.BASIC_METAL;
                    break;
                case 13:
                    returnName = "Si";
                    returnFullName = "Silicon";
                    returnType = Element.ElementType.METALLOID;
                    break;
                case 14:
                    returnName = "P";
                    returnFullName = "Phosphorus";
                    returnType = Element.ElementType.NONMETAL;
                    break;
                case 15:
                    returnName = "S";
                    returnFullName = "Sulfur";
                    returnType = Element.ElementType.NONMETAL;
                    break;
                case 16:
                    returnName = "Cl";
                    returnFullName = "Chlorine";
                    returnType = Element.ElementType.HALOGEN;
                    break;
                case 17:
                    returnName = "Ar";
                    returnFullName = "Argon";
                    returnType = Element.ElementType.NOBLE_GAS;
                    break;
                case 18:
                    returnName = "K";
                    returnFullName = "Potassium";
                    returnType = Element.ElementType.ALKALI;
                    break;
                case 19:
                    returnName = "Ca";
                    returnFullName = "Calcium";
                    returnType = Element.ElementType.ALKALINE_EARTH;
                    break;
                case 20:
                    returnName = "Sc";
                    returnFullName = "Scandium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 21:
                    returnName = "Ti";
                    returnFullName = "Titanium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 22:
                    returnName = "V";
                    returnFullName = "Vanadium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 23:
                    returnName = "Cr";
                    returnFullName = "Chromium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 24:
                    returnName = "Mn";
                    returnFullName = "Manganese";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 25:
                    returnName = "Fe";
                    returnFullName = "Iron";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 26:
                    returnName = "Co";
                    returnFullName = "Cobalt";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 27:
                    returnName = "Ni";
                    returnFullName = "Nickel";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 28:
                    returnName = "Cu";
                    returnFullName = "Copper";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 29:
                    returnName = "Zn";
                    returnFullName = "Zinc";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 30:
                    returnName = "Ga";
                    returnFullName = "Gallium";
                    returnType = Element.ElementType.BASIC_METAL;
                    break;
                case 31:
                    returnName = "Ge";
                    returnFullName = "Germanium";
                    returnType = Element.ElementType.METALLOID;
                    break;
                case 32:
                    returnName = "As";
                    returnFullName = "Arsenic";
                    returnType = Element.ElementType.METALLOID;
                    break;
                case 33:
                    returnName = "Se";
                    returnFullName = "Selenium";
                    returnType = Element.ElementType.NONMETAL;
                    break;
                case 34:
                    returnName = "Br";
                    returnFullName = "Bromine";
                    returnType = Element.ElementType.HALOGEN;
                    break;
                case 35:
                    returnName = "Kr";
                    returnFullName = "Krypton";
                    returnType = Element.ElementType.NOBLE_GAS;
                    break;
                case 36:
                    returnName = "Rb";
                    returnFullName = "Rubidium";
                    returnType = Element.ElementType.ALKALI;
                    break;
                case 37:
                    returnName = "Sr";
                    returnFullName = "Strontium";
                    returnType = Element.ElementType.ALKALINE_EARTH;
                    break;
                case 38:
                    returnName = "Y";
                    returnFullName = "Yttrium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 39:
                    returnName = "Zr";
                    returnFullName = "Zirconium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 40:
                    returnName = "Nb";
                    returnFullName = "Niobium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 41:
                    returnName = "Mo";
                    returnFullName = "Molybdenum";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 42:
                    returnName = "Tc";
                    returnFullName = "Technetium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 43:
                    returnName = "Ru";
                    returnFullName = "Ruthenium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 44:
                    returnName = "Rh";
                    returnFullName = "Rhodium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 45:
                    returnName = "Pd";
                    returnFullName = "Palladium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 46:
                    returnName = "Ag";
                    returnFullName = "Silver";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 47:
                    returnName = "Cd";
                    returnFullName = "Cadmium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 48:
                    returnName = "In";
                    returnFullName = "Indium";
                    returnType = Element.ElementType.BASIC_METAL;
                    break;
                case 49:
                    returnName = "Sn";
                    returnFullName = "Tin";
                    returnType = Element.ElementType.BASIC_METAL;
                    break;
                case 50:
                    returnName = "Sb";
                    returnFullName = "Antimony";
                    returnType = Element.ElementType.METALLOID;
                    break;
                case 51:
                    returnName = "Te";
                    returnFullName = "Tellurium";
                    returnType = Element.ElementType.METALLOID;
                    break;
                case 52:
                    returnName = "I";
                    returnFullName = "Iodine";
                    returnType = Element.ElementType.HALOGEN;
                    break;
                case 53:
                    returnName = "Xe";
                    returnFullName = "Xenon";
                    returnType = Element.ElementType.NOBLE_GAS;
                    break;
                case 54:
                    returnName = "Cs";
                    returnFullName = "Cesium";
                    returnType = Element.ElementType.ALKALI;
                    break;
                case 55:
                    returnName = "Ba";
                    returnFullName = "Barium";
                    returnType = Element.ElementType.ALKALINE_EARTH;
                    break;
                case 56:
                    returnName = "La";
                    returnFullName = "Lanthanum";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 57:
                    returnName = "Ce";
                    returnFullName = "Cerium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 58:
                    returnName = "Pr";
                    returnFullName = "Praseodymium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 59:
                    returnName = "Nd";
                    returnFullName = "Neodymium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 60:
                    returnName = "Pm";
                    returnFullName = "Promethium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 61:
                    returnName = "Sm";
                    returnFullName = "Samarium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 62:
                    returnName = "Eu";
                    returnFullName = "Europium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 63:
                    returnName = "Gd";
                    returnFullName = "Gadolinium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 64:
                    returnName = "Tb";
                    returnFullName = "Terbium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 65:
                    returnName = "Dy";
                    returnFullName = "Dysprosium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 66:
                    returnName = "Ho";
                    returnFullName = "Holmium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 67:
                    returnName = "Er";
                    returnFullName = "Erbium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 68:
                    returnName = "Tm";
                    returnFullName = "Thulium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 69:
                    returnName = "Yb";
                    returnFullName = "Ytterbium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 70:
                    returnName = "Lu";
                    returnFullName = "Lutetium";
                    returnType = Element.ElementType.LANTHANIDE;
                    break;
                case 71:
                    returnName = "Hf";
                    returnFullName = "Hafnium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 72:
                    returnName = "Ta";
                    returnFullName = "Tantalum";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 73:
                    returnName = "W";
                    returnFullName = "Tungsten";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 74:
                    returnName = "Re";
                    returnFullName = "Rhenium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 75:
                    returnName = "Os";
                    returnFullName = "Osmium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 76:
                    returnName = "Ir";
                    returnFullName = "Iridium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 77:
                    returnName = "Pt";
                    returnFullName = "Platinum";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 78:
                    returnName = "Au";
                    returnFullName = "Gold";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 79:
                    returnName = "Hg";
                    returnFullName = "Mercury";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 80:
                    returnName = "Tl";
                    returnFullName = "Thallium";
                    returnType = Element.ElementType.BASIC_METAL;
                    break;
                case 81:
                    returnName = "Pb";
                    returnFullName = "Lead";
                    returnType = Element.ElementType.BASIC_METAL;
                    break;
                case 82:
                    returnName = "Bi";
                    returnFullName = "Bismuth";
                    returnType = Element.ElementType.BASIC_METAL;
                    break;
                case 83:
                    returnName = "Po";
                    returnFullName = "Polonium";
                    returnType = Element.ElementType.NONMETAL;
                    break;
                case 84:
                    returnName = "At";
                    returnFullName = "Astatine";
                    returnType = Element.ElementType.HALOGEN;
                    break;
                case 85:
                    returnName = "Rn";
                    returnFullName = "Radon";
                    returnType = Element.ElementType.NOBLE_GAS;
                    break;
                case 86:
                    returnName = "Fr";
                    returnFullName = "Francium";
                    returnType = Element.ElementType.ALKALI;
                    break;
                case 87:
                    returnName = "Ra";
                    returnFullName = "Radium";
                    returnType = Element.ElementType.ALKALINE_EARTH;
                    break;
                case 88:
                    returnName = "Ac";
                    returnFullName = "Actinium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 89:
                    returnName = "Th";
                    returnFullName = "Thorium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 90:
                    returnName = "Pa";
                    returnFullName = "Protactinium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 91:
                    returnName = "U";
                    returnFullName = "Uranium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 92:
                    returnName = "Np";
                    returnFullName = "Neptunium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 93:
                    returnName = "Pu";
                    returnFullName = "Plutonium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 94:
                    returnName = "Am";
                    returnFullName = "Americium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 95:
                    returnName = "Cm";
                    returnFullName = "Curium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 96:
                    returnName = "Bk";
                    returnFullName = "Berkelium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 97:
                    returnName = "Cf";
                    returnFullName = "Californium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 98:
                    returnName = "Es";
                    returnFullName = "Einsteinium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 99:
                    returnName = "Fm";
                    returnFullName = "Fermium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 100:
                    returnName = "Md";
                    returnFullName = "Mendelevium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 101:
                    returnName = "No";
                    returnFullName = "Nobelium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 102:
                    returnName = "Lr";
                    returnFullName = "Lawrencium";
                    returnType = Element.ElementType.ACTINIDE;
                    break;
                case 103:
                    returnName = "Rf";
                    returnFullName = "Rutherfordium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 104:
                    returnName = "Db";
                    returnFullName = "Dubnium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 105:
                    returnName = "Sg";
                    returnFullName = "Seaborgium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 106:
                    returnName = "Bh";
                    returnFullName = "Bohrium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 107:
                    returnName = "Hs";
                    returnFullName = "Hassium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 108:
                    returnName = "Mt";
                    returnFullName = "Meitnerium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 109:
                    returnName = "Ds";
                    returnFullName = "Darmstadtium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 110:
                    returnName = "Rg";
                    returnFullName = "Roentgenium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 111:
                    returnName = "Cn";
                    returnFullName = "Copernicium";
                    returnType = Element.ElementType.TRANSITION_METAL;
                    break;
                case 112:
                    returnName = "Nh";
                    returnFullName = "Nihonium";
                    returnType = Element.ElementType.BASIC_METAL;
                    break;
                case 113:
                    returnName = "Fl";
                    returnFullName = "Flerovium";
                    returnType = Element.ElementType.BASIC_METAL;
                    break;
                case 114:
                    returnName = "Mc";
                    returnFullName = "Moscovium";
                    returnType = Element.ElementType.BASIC_METAL;
                    break;
                case 115:
                    returnName = "Lv";
                    returnFullName = "Livermorium";
                    returnType = Element.ElementType.BASIC_METAL;
                    break;
                case 116:
                    returnName = "Ts";
                    returnFullName = "Tennessine";
                    returnType = Element.ElementType.HALOGEN;
                    break;
                case 117:
                    returnName = "Og";
                    returnFullName = "Oganesson";
                    returnType = Element.ElementType.NOBLE_GAS;
                    break;
                default:
                    returnName = "ERR";
                    returnFullName = "ERROR";
                    returnType = Element.ElementType.NONMETAL;
            }
            allElements[i] = new Element(returnName, returnFullName, returnType, i + 1);
        }

        return allElements;
    }
}
