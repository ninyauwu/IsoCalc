package com.dl630.isocalc.core;

import com.dl630.isocalc.core.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTest {

    @Test
    public void testGetChemicalPrecision() {
        Assertions.assertEquals(4, Util.getChemicalPrecision("0.0001"));
        Assertions.assertEquals(4, Util.getChemicalPrecision("0.0001E+1"));
        Assertions.assertEquals(2, Util.getChemicalPrecision("0.01"));
        Assertions.assertEquals(2, Util.getChemicalPrecision("0.1"));
        Assertions.assertEquals(2, Util.getChemicalPrecision("0"));
    }

    @Test
    public void testRoundToClampInt() {
        Assertions.assertEquals("0.01", Util.roundDecimalsTo("0.01", 2));
        Assertions.assertEquals("0.00", Util.roundDecimalsTo("0.0001", 2));
        Assertions.assertEquals("0.30", Util.roundDecimalsTo("0.3", 2));
        Assertions.assertEquals("0.300", Util.roundDecimalsTo("0.3", 3));
        Assertions.assertEquals("0.300", Util.roundDecimalsTo("0.30", 3));
    }

    @Test
    public void testChemicalRounding() {
        Assertions.assertEquals("0.30", Util.roundDecimalsTo("0.3", Util.getChemicalPrecision("0")));
        Assertions.assertEquals("0.30", Util.roundDecimalsTo("0.3", Util.getChemicalPrecision("0.00")));
        Assertions.assertEquals("0.300", Util.roundDecimalsTo("0.3", Util.getChemicalPrecision("0.001")));
        Assertions.assertEquals("0.3000", Util.roundDecimalsTo("0.3", Util.getChemicalPrecision("0.0012")));
        Assertions.assertEquals("1.000", Util.roundDecimalsTo("1", 3));
    }
}
