package com.dl630.isocalc.core.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileManager<T> {
    private final String path;
    public FileManager(String path) {
        this.path = path;
    }

    public T loadFile(Type type) {
        try {
            if (!Files.exists(Paths.get("data/" + path))) {
                if (!Files.exists(Paths.get("data"))) Files.createDirectory(Paths.get("data"));
                Files.createFile(Paths.get("data/" + path));
            }
            StringBuilder outputBuilder = new StringBuilder();
            Scanner scanner = new Scanner(new File("data/" + path));
            int i = 0;
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                System.out.println(s);
                outputBuilder.append(s);
                i++;
            }
            if (i < 2) return null;

            Gson gson = new Gson();

            return gson.fromJson(outputBuilder.toString(), type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveObject(T object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(object);
        try {
            if (!Files.exists(Paths.get("data/" + path))) Files.createFile(Paths.get("data/" + path));
            Writer writer = Files.newBufferedWriter(Paths.get("data/" + path));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
