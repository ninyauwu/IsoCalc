package com.dl630.isocalc;

import com.dl630.isocalc.element.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

public class BeractAdapter {
    public static void calculateResult(Map<Element, ArrayList<Integer>> isotopeMap) {
        StringBuilder output = new StringBuilder();
        output.append("""
                4e16, 2e15, 0.01, 4e15
                3600
                1
                1800
                Pb
                0.016
                0.5\n""");
        try {
            FileWriter fileWriter = new FileWriter("D:\\IdeaProjects\\IsoCalc\\beract\\test.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (Element element : isotopeMap.keySet()) {
                for (Integer isotope : isotopeMap.get(element)) {
                    if (isotope.equals(0)) {
                        output.append(element.getName() + ", 1\n");
                    } else {
                        output.append(element.getName() + "-" + isotope + ", 1\n");
                    }
                }
            }
            output.append("x");
            printWriter.print(output.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        launchBeract();
    }

    private static void launchBeract() {
        String script = "D:\\IdeaProjects\\IsoCalc\\beract\\beract_launch.vbs";

        // search for real path:
        String executable = "C:\\Windows\\System32\\wscript.exe";
        String[] cmdArr = { executable, script };
        try {
            Runtime runTime = Runtime.getRuntime();
            Process process = runTime.exec(cmdArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
