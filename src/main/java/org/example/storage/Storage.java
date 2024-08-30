package org.example.storage;

import org.example.model.Request;
import org.json.JSONObject;

import java.io.*;

public class Storage {
    private String path;

    public Storage(String path) {
        if (!path.endsWith(File.separator)) {
            this.path = path + File.separator;
        }
    }

    public void saveUrl(String url) {
        File file = new File(this.path + "url.txt");

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(url);
            System.out.println("Content successfully written to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("An IOException occurred while writing to the file: " + e.getMessage());
        }
    }

    public Request getRequest(int key){
         if (!this.path.endsWith(File.separator)) {
            this.path += File.separator;
        }

        File file = new File(this.path + key);

        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("An IOException occurred while reading the file: " + e.getMessage());
            //are your eyes burning uncle bob fans?
            return null;
        }
        return null;

    }

    public void writeString(String content, String fileName) {

        File file = new File(this.path + fileName);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(content);
            System.out.println("Content successfully written to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("An IOException occurred while writing to the file: " + e.getMessage());
        }
    }

    public String readString(String fileName) {

        if (!this.path.endsWith(File.separator)) {
            this.path += File.separator;
        }

        File file = new File(this.path + fileName);

        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("An IOException occurred while reading the file: " + e.getMessage());
            //are your eyes burning uncle bob fans?
            if (fileName.endsWith(".json")) {
                return "{}";
            }
            return "";
        }
        return contentBuilder.toString().strip();
    }
}
