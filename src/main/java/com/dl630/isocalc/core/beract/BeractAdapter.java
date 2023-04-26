package com.dl630.isocalc.core.beract;

import com.dl630.isocalc.core.storage.DataMemory;
import com.dl630.isocalc.core.storage.RadiationSettingObject;
import com.dl630.isocalc.element.IsotopeMass;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class BeractAdapter {
    public static void calculateResult(ArrayList<IsotopeMass> isotopes, RadiationSettingObject flux) {
        StringBuilder output = new StringBuilder();
        output.append(flux.getThermalFlux() + ", "
                + flux.getEpithermalFlux() + ", "
                + flux.getAlpha() + ", "
                + flux.getFastFlux() + "\n");
        output.append(DataMemory.getSavedIrradiationTime() + "\n");
        output.append((DataMemory.getSavedCycleCount() == null ? 1 : DataMemory.getSavedCycleCount()) + "\n");
        output.append(DataMemory.getSavedDecayTime() == null ? 0 : DataMemory.getSavedDecayTime() + "\n");
        output.append(DataMemory.getSavedContainerElement() == null ? "Pb\n" : DataMemory.getSavedContainerElement().getName() + "\n");
        output.append(DataMemory.getSavedContainerThickness() == null ? "0.0\n" : DataMemory.getSavedContainerThickness() + "\n");
        output.append(0.5 + "\n");
        try {
            FileWriter fileWriter = new FileWriter(Paths.get("beract/test.txt").toString());
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (IsotopeMass mass : isotopes) {
                if (mass.getIsotope().equals(0)) {
                    output.append(mass.getElement().getName() + ", " + mass.getMass() + "\n");
                } else {
                    output.append(mass.getElement().getName() + "-" + mass.getIsotope() + ", " + mass.getMass() + "\n");
                }
            }
            output.append("x");
            printWriter.print(output);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            launchBeract();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void launchBeract() throws FileNotFoundException {
        // Get the VB script path
        String script = Paths.get("beract/beract_launch.vbs").toString();
        if (!Files.exists(Paths.get("beract/beract_launch.vbs"))) {
            throw new FileNotFoundException("Failed to find beract at " + Paths.get("beract").toAbsolutePath());
        }

        // Get the wscript.exe path
        String executable = "C:\\Windows\\System32\\wscript.exe";
        String[] cmdArr = { executable, script };

        // Run the VB script
        try {
            ProcessBuilder pb = new ProcessBuilder(Paths.get("beract\\beract2022.exe").toAbsolutePath().toString(), Paths.get("beract").toAbsolutePath().toString(), "test.txt");
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
