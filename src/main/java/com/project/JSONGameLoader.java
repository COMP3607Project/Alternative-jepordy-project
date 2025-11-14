package com.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONGameLoader implements GameLoader {
    private final String filePath;

    public JSONGameLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<List<String>> load() {
        List<List<String>> records = new ArrayList<>();
        StringBuilder json = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove outer brackets: [[...],[...]] → ...],[...]
        String content = json.toString().replaceAll("^\\[|\\]$", "");

        // Split on "],[" to get rows
        String[] rows = content.split("\\],\\[");

        for (String row : rows) {
            row = row.replaceAll("[\\[\\]\"]", ""); // remove brackets and quotes
            String[] values = row.split(",");
            List<String> record = new ArrayList<>();
            for (String val : values) {
                record.add(val.trim());
            }
            records.add(record);
        }

        return records;
    }
}
