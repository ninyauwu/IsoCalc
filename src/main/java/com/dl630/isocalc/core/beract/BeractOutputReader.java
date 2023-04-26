package com.dl630.isocalc.core.beract;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class BeractOutputReader extends Thread {
    private CountDownLatch latch;

    @Override
    public void run() {
        super.run();
        while (true) {
            if (Files.exists(Paths.get("beract/test.txt_out.txt"))) {
                try {
                    Scanner scanner = new Scanner(new File("beract/test.txt_out.txt"));
                    if (scanner.hasNext()) {
                        System.out.println("result found");
                        latch.countDown();
                        stop();
                        break;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startRead(CountDownLatch latch) {
        this.latch = latch;
        this.start();
    }

    public static ArrayList<String> readResult() {
        ArrayList<String> output = new ArrayList<>();
        if (Files.exists(Paths.get("beract/test.txt_out.txt"))) {
            try {
                Scanner scanner = new Scanner(new File("beract/test.txt_out.txt"));
                if (scanner.hasNext()) {
                    while (scanner.hasNextLine()) {
                        output.add(scanner.nextLine());
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return output;
    }
}
