package com.dl630.isocalc;

import com.dl630.isocalc.element.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

public class BeractAdapter {
    public static void calculateResult(Map<Element, ArrayList<Integer>> isotopeMap) {
        String output = """
                4e16, 2e15, 0.01, 4e15
                3600
                1
                1800
                Pb
                0.016
                0.5\n""";
        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\alexb\\IdeaProjects\\Git\\IsoCalc\\test.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (Element element : isotopeMap.keySet()) {
                for (Integer isotope : isotopeMap.get(element)) {
                    if (isotope.equals(0)) {
                        output += element.getName() + ", 1\n";
                    } else {
                        output += element.getName() + "-" + isotope + ", 1\n";
                    }
                }
            }
            output += "x";
            printWriter.print(output);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
