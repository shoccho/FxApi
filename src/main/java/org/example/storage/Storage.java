package org.example.storage;

import java.io.*;

public class Storage {
    private String path;

    public Storage(String path) {
        if (!path.endsWith(File.separator)) {
            this.path = path + File.separator;
        }
    }

    public void saveJSON(String content, String fileName) {

        File file = new File(this.path + fileName + ".json");

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(content);
            System.out.println("Content successfully written to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("An IOException occurred while writing to the file: " + e.getMessage());
        }
    }

    public String readJSON(String fileName) {

        if (!this.path.endsWith(File.separator)) {
            this.path += File.separator;
        }

        File file = new File(this.path + fileName + ".json");

        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("An IOException occurred while reading the file: " + e.getMessage());
            return "{}";
        }
        return contentBuilder.toString();
    }
}
