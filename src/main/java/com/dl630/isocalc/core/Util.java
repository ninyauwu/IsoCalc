package com.dl630.isocalc.core;

import com.dl630.isocalc.Main;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static String roundTo(String value, int precision) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(precision - getNoDecimalLength(value), RoundingMode.HALF_UP);
        return bd.toString();
    }

    public static String roundDecimalsTo(String value, int precision) {
        if (precision < 0) throw new IndexOutOfBoundsException("Precision cannot be less than 1");
        if (value.split("\\.").length < 2) {
            value += ".0";
        }
        if (value.split("\\.")[1].length() < 1) {
            value += "0";
        }
        int precisionDifference = precision - value.split("\\.")[1].length();
        if (precisionDifference > 0) {
            for (int i = 0; i < precisionDifference; i++) {
                value = value + "0";
            }
        } else if (precisionDifference < 0) {
            value = value.substring(0, value.length() + precisionDifference);
        }
        if (value.charAt(value.length() - 1) == '.') value = value.substring(0, value.length() - 1);

        return value;
    }

    public static int getNoDecimalLength(String value) {
        int pointPosition = -1;
        int length = stringIsInteger(value) ? value.length() : value.length();
        for (int i = 0; i < value.length(); i ++) {
            if (String.valueOf(value.charAt(i)).equalsIgnoreCase(".")) pointPosition = i;
        }

        return pointPosition == -1 ? length : pointPosition;
    }

    public static int getPrecision(String value) {
        if (!isDecimalValid(value)) return 1;
        for (int i = 0; i < value.length(); i++) {
            if (String.valueOf(value.charAt(i)).equalsIgnoreCase("e")) {
                String coeff = value.substring(0, i);
                return stringIsInteger(coeff) ? coeff.length() : coeff.length() - 1;
            }
        }

        return stringIsInteger(value) ? value.length() : value.length() - 1;
    }

    // Gets the precision of only the decimal digits, with a minimum of 2
    public static int getChemicalPrecision(String value) {
        if (!isDecimalValid(value)) return 1;
        for (int i = 0; i < value.length(); i++) {
            if (String.valueOf(value.charAt(i)).equalsIgnoreCase("e")) {
                String coeff = value.substring(0, i);
                return getChemicalDecimals(coeff);
            }
        }

        return getChemicalDecimals(value);
    }

    private static int getChemicalDecimals(String simpleDouble) {
        int pointPos = -1;
        for (int i = 0; i < simpleDouble.length(); i++) {
            if (simpleDouble.charAt(i) == '.') {
                pointPos = i;
            }
        }

        return pointPos == -1 ? 2 : Math.max(simpleDouble.substring(pointPos).length() - 1, 2);
    }

    public static boolean isDecimalValid(String decimalString) {
        int expPos = -1;
        String expString;
        for (int i = 0; i < decimalString.length(); i++) {
            String charString = String.valueOf(decimalString.charAt(i));
            if (charString.equalsIgnoreCase("e")) {
                if (expPos != -1) return false;
                expPos = i;
                expString = decimalString.substring(expPos);
                decimalString = decimalString.substring(0, expPos);
                if (expPos == 0) return false;
                if (!isExponentValid(expString)) return false;
            }
        }
        if (!stringIsDouble(decimalString)) return false;

        return true;
    }

    public static boolean isExponentValid(String exponentString) {
        if (!String.valueOf(exponentString.charAt(0)).equalsIgnoreCase("e")) return false;
        if (exponentString.length() < 2) return false;

        String charString = String.valueOf(exponentString.charAt(1));
        if (charString.equals("+") || charString.equals("-")) {
            if (exponentString.length() < 3) return false;
            return stringIsInteger(exponentString.substring(2));
        } else {
            return stringIsInteger(exponentString.substring(1));
        }
    }

    public static boolean isIncompleteExponentValid(String exponentString) {
        switch(exponentString.length()) {
            case 1:
                if (exponentString.equalsIgnoreCase("e")) break;
                return false;
            case 2:
                if (exponentString.equalsIgnoreCase("e+")
                        || exponentString.equalsIgnoreCase("e-")) {
                    break;
                }
                return false;
            default:
                return false;
        }

        return true;
    }

    public static boolean stringIsDouble(String doubleString) {
        if (doubleString == null) return false;
        if (doubleString.length() < 1) return false;
        try {
            Double.parseDouble(doubleString);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static boolean stringIsInteger(String doubleString) {
        if (doubleString == null) return false;
        if (doubleString.length() < 1) return false;
        try {
            Integer.parseInt(doubleString);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static void addIndirectHover(Button hoverButton, Pane affectedPane, StringProperty defaultStyle) {
        addIndirectHover(hoverButton, affectedPane, defaultStyle, 1.05F);
    }
    public static void addIndirectHover(Button hoverButton, Pane affectedPane, StringProperty defaultStyle, float hoversize) {
        hoverButton.hoverProperty().addListener(change -> updateHover(hoverButton, affectedPane, defaultStyle, hoversize));
        hoverButton.pressedProperty().addListener(change -> updateHover(hoverButton, affectedPane, defaultStyle, hoversize));
        defaultStyle.addListener(change -> updateHover(hoverButton, affectedPane, defaultStyle, hoversize));
    }

    private static void updateHover(Button hoverButton, Pane affectedPane, StringProperty defaultStyle, float hoversize) {
        if (hoverButton.isPressed()) {
            affectedPane.setScaleX(1.0);
            affectedPane.setScaleY(1.0);
            affectedPane.setStyle(defaultStyle.get() + "; -fx-background-color: #E1E1E1");
        } else {
            float newScale = hoverButton.isHover() ? hoversize : 1.0F;
            affectedPane.setScaleX(newScale);
            affectedPane.setScaleY(newScale);
            affectedPane.setStyle(defaultStyle.get() + "; -fx-background-color: white");
        }
    }

    public static String condenseStringList(List<String> list, boolean addReturns) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (addReturns && i + 1 != list.size()) builder.append("\n");
        }

        return builder.toString();
    }

}
