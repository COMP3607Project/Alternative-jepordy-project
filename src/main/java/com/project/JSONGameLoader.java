package com.project;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class JSONGameLoader implements GameLoader {

    private final String filePath;
    private final List<Category> categories = new ArrayList<>();

    public JSONGameLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void load() {
        try {
           
            StringBuilder json = new StringBuilder();
            Scanner scan = new Scanner(new File(filePath));

            while (scan.hasNextLine()) {
                json.append(scan.nextLine().trim());
            }
            scan.close();

            String content = json.toString().trim();

            
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1).trim();
            }

            
            List<String> objects = extractJsonObjects(content);

            for (String obj : objects) {
                parseObject(obj);
            }

        } catch (Exception e) {
            System.out.println("ERROR loading JSON: " + e.getMessage());
        }
    }

    private List<String> extractJsonObjects(String json) {
        List<String> list = new ArrayList<>();

        int depth = 0;
        boolean inString = false;
        int start = -1;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

           
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
            }

            if (!inString) {
                if (c == '{') {
                    if (depth == 0) start = i;
                    depth++;
                } else if (c == '}') {
                    depth--;
                    if (depth == 0 && start != -1) {
                        list.add(json.substring(start, i + 1));
                    }
                }
            }
        }

        return list;
    }

    private void parseObject(String obj) {
        String categoryName = extractString(obj, "\"Category\"");
        int value = extractInt(obj, "\"Value\"");
        String questionText = extractString(obj, "\"Question\"");
        String correctAnswer = extractString(obj, "\"CorrectAnswer\"");

        
        String options = extractObject(obj, "\"Options\"");

        List<Options> opts = new ArrayList<>();
        opts.add(new Options("A", extractString(options, "\"A\"")));
        opts.add(new Options("B", extractString(options, "\"B\"")));
        opts.add(new Options("C", extractString(options, "\"C\"")));
        opts.add(new Options("D", extractString(options, "\"D\"")));

        Questions q = new Questions(questionText, opts, value, correctAnswer);

        Category cat = findCategory(categoryName);
        if (cat == null) {
            cat = new Category(categoryName);
            categories.add(cat);
        }

        cat.addQuestions(q);
    }

    private String extractString(String json, String key) {
        int start = json.indexOf(key);
        if (start == -1) return "";

        start = json.indexOf(":", start) + 1;

        
        start = json.indexOf("\"", start) + 1;
        int end = json.indexOf("\"", start);

        return json.substring(start, end);
    }

    private int extractInt(String json, String key) {
        int start = json.indexOf(key);
        if (start == -1) return 0;

        start = json.indexOf(":", start) + 1;

        
        while (start < json.length() && !Character.isDigit(json.charAt(start))) {
            start++;
        }

        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) {
            end++;
        }

        return Integer.parseInt(json.substring(start, end));
    }

    private String extractObject(String json, String key) {
        int start = json.indexOf(key);
        if (start == -1) return "";

        start = json.indexOf("{", start);
        int depth = 0;

        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '{') depth++;
            if (c == '}') depth--;

            if (depth == 0) {
                return json.substring(start, i + 1);
            }
        }
        return "";
    }

    private Category findCategory(String name) {
        for (Category c : categories) {
            if (c.equals(name)) return c;
        }
        return null;
    }

    public List<Category> getCategories() {
        return categories;
    }
}


//Based on CSV but adjusted to support JSON format