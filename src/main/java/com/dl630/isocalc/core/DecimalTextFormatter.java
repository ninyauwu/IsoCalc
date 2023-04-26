package com.dl630.isocalc.core;

import com.dl630.isocalc.core.Util;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;

public class DecimalTextFormatter extends TextFormatter<Number> {
    private static final DecimalFormat format = new DecimalFormat("#.0;-#.0");

    public DecimalTextFormatter(int minDecimals, int maxDecimals) {
        this(minDecimals, maxDecimals, true);
    }

    public DecimalTextFormatter(int minDecimals, int maxDecimals, boolean allowsNegative) {
        this(minDecimals, maxDecimals, allowsNegative, false);
    }

    public DecimalTextFormatter(int minDecimals, int maxDecimals, boolean allowsNegative, boolean allowsEmpty) {
        this(minDecimals, maxDecimals, allowsNegative, allowsEmpty, -1);
    }

    public DecimalTextFormatter(int minDecimals, int maxDecimals, boolean allowsNegative, boolean allowsEmpty, int maxNoOfDigitsBeforeDecimal) {
        super(getStringConverter(minDecimals, maxDecimals, allowsEmpty), 0, getUnaryOperator(maxDecimals, minDecimals, allowsNegative, allowsEmpty, maxNoOfDigitsBeforeDecimal));
    }

    private static StringConverter<Number> getStringConverter(int minDecimals, int maxDecimals, boolean allowsEmpty) {
        return new StringConverter<>() {
            @Override
            public String toString(Number number) {
                if (number == null) {
                    return "";
                }
//                String format = "0.0";
//                for (int i = 0; i < maxDecimals; i++) {
//                    if (i < minDecimals) {
//                        format = format + "0";
//                    } else {
//                        format = format + "#";
//                    }
//                }
//                format = format + ";-" + format;
//                DecimalFormat df = new DecimalFormat(format);
//                return df.format(number);
                return String.valueOf(number);
            }

            @Override
            public Number fromString(String string) {
                if (string == null) {
                    return null;
                }
                if (string.equals("")) {
                    return allowsEmpty ? null : 0;
                }
                return new BigDecimal(string);
            }
        };
    }

    private static UnaryOperator<Change> getUnaryOperator(int maxDecimals, int minDecimals, boolean allowsNegative, boolean allowsEmpty, int noOfDigitsBeforeDecimal) {
        return change -> {
            String newText = change.getControlNewText();
            if (!allowsNegative && newText.startsWith("-")) {
                return null;
            }

            if (newText.isEmpty()) {
                return change;
            }

            int expPos = -1;
            String expString = "";
            for (int i = 0; i < newText.length(); i++) {
                if (String.valueOf(newText.charAt(i)).equalsIgnoreCase("e")) {
                    if (expPos != -1) return null;
                    expPos = i;
                    expString = newText.substring(expPos);
                    newText = newText.substring(0, expPos);
                }
            }

            if (expPos != -1) {
                if (!Util.isExponentValid(expString) && !Util.isIncompleteExponentValid(expString)) return null;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(newText, parsePosition);

            if (change.getCaretPosition() == 1) {
                if (newText.equals(".")) {
                    return change;
                }
            }


            if (object == null || parsePosition.getIndex() < newText.length()) {
                return null;
            } else {
                if(noOfDigitsBeforeDecimal != -1)
                {
                    int signum = new BigDecimal(newText).signum();
                    int precision = new BigDecimal(newText).precision();
                    int scale = new BigDecimal(newText).scale();

                    int val = signum == 0 ? 1 : precision - scale;
                    if (val > noOfDigitsBeforeDecimal) {
                        return null;
                    }
                }

                int decPos = newText.indexOf(".");
                if (decPos > 0) {
                    int numberOfDecimals = newText.substring(decPos + 1).length();
                    if (numberOfDecimals > maxDecimals) {
                        return null;
                    }
                }
                return change;
            }
        };
    }
}