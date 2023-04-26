package com.dl630.isocalc.core;

import com.dl630.isocalc.core.Util;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;

public class IntegerTextFormatter extends TextFormatter<Integer> {
    private static final DecimalFormat format = new DecimalFormat("#.0;-#.0");

    public IntegerTextFormatter(boolean allowsNegative) {
        this(allowsNegative, false);
    }

    public IntegerTextFormatter(boolean allowsNegative, boolean allowsEmpty) {
        super(getStringConverter(allowsEmpty), 0, getUnaryOperator(allowsNegative, allowsEmpty));
    }

    private static StringConverter<Integer> getStringConverter(boolean allowsEmpty) {
        return new StringConverter<>() {
            @Override
            public String toString(Integer number) {
                if (number == null) {
                    return "";
                }

                return String.valueOf(number);
            }

            @Override
            public Integer fromString(String string) {
                if (string == null) {
                    return null;
                }
                if (string.equals("")) {
                    return allowsEmpty ? null : 0;
                }
                return tryParseInt(string);
            }
        };
    }

    private static UnaryOperator<TextFormatter.Change> getUnaryOperator(boolean allowsNegative, boolean allowsEmpty) {
        return change -> {
            String newText = change.getControlNewText();
            if (!allowsNegative && newText.startsWith("-")) {
                return null;
            }

            if (newText.isEmpty() && allowsEmpty) {
                return change;
            }

            if (tryParseInt(newText) == null) {
                return null;
            }

            return change;
        };
    }

    // source: https://stackoverflow.com/questions/31202879/java-tryparseint-best-practice
    public static Integer tryParseInt(String text) {
        if (text != null && !text.isEmpty()) {
            String parseText = text;
            if (text.startsWith("-")) {
                parseText = text.substring(1);
            }
            if (parseText.matches("[0-9]+")) {
                return Integer.valueOf(text);
            }
        }
        return null;
    }
}
