package com.project;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class CSVGameLoader implements GameLoader {

    private final String filePath;
    private final List<Category> categories = new ArrayList<>();

    public CSVGameLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void load() {
        File file = new File(filePath);
        try (Scanner scan = new Scanner(file)) {
            
            if (scan.hasNextLine()) {
                scan.nextLine();
            }

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] parts = line.split(",", -1);

                if (parts.length < 8) continue; // Not enough columns

                String categoryName = parts[0].trim();
                int value = Integer.parseInt(parts[1].trim());
                String questionText = parts[2].trim();

                // Create options A–D
                List<Options> options = new ArrayList<>();
                options.add(new Options("A", parts[3].trim()));
                options.add(new Options("B", parts[4].trim()));
                options.add(new Options("C", parts[5].trim()));
                options.add(new Options("D", parts[6].trim()));

                String correctAnswer = parts[7].trim();

                
                Questions q = new Questions(questionText, options, value, correctAnswer);

              
                Category found = findCategory(categoryName);
                if (found == null) {
                    found = new Category(categoryName);
                    categories.add(found);
                }

                found.addQuestions(q);
            }

        } catch (Exception e) {
            System.out.println("ERROR LOADING CSV: " + e.getMessage());
        }
    }

    private Category findCategory(String name) {
        for (Category c : categories) {
            if (c.equals(name)) return c;
        }
        return null;
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }
}

//CSVGameLoader Design was based off: https://www.baeldung.com/java-csv-file-array
