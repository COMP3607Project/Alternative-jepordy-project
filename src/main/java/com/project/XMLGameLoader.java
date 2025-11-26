package com.project;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class XMLGameLoader implements GameLoader {

    private final String filePath;
    private final List<Category> categories = new ArrayList<>();

    public XMLGameLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void load() {
        try {
            
            StringBuilder xml = new StringBuilder();
            Scanner scan = new Scanner(new File(filePath));

            while (scan.hasNextLine()) {
                xml.append(scan.nextLine().trim());
            }
            scan.close();

            String content = xml.toString();

            
            List<String> items = extractBlocks(content, "QuestionItem");

            for (String block : items) {
                parseQuestionBlock(block);
            }

        } catch (Exception e) {
            System.out.println("ERROR loading XML: " + e.getMessage());
        }
    }

    private void parseQuestionBlock(String xml) {

        String categoryName = extractTag(xml, "Category");
        int value = Integer.parseInt(extractTag(xml, "Value"));
        String questionText = extractTag(xml, "QuestionText");

        
        String optionsBlock = extractTagBlock(xml, "Options");

        List<Options> options = new ArrayList<>();
        options.add(new Options("A", extractTag(optionsBlock, "OptionA")));
        options.add(new Options("B", extractTag(optionsBlock, "OptionB")));
        options.add(new Options("C", extractTag(optionsBlock, "OptionC")));
        options.add(new Options("D", extractTag(optionsBlock, "OptionD")));

        String correctAnswer = extractTag(xml, "CorrectAnswer");

        Questions q = new Questions(questionText, options, value, correctAnswer);

        
        Category cat = findCategory(categoryName);
        if (cat == null) {
            cat = new Category(categoryName);
            categories.add(cat);
        }

        cat.addQuestions(q);
    }

   
    private String extractTag(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";

        int start = xml.indexOf(open);
        if (start == -1) return "";

        start += open.length();
        int end = xml.indexOf(close, start);

        if (end == -1) return "";

        return xml.substring(start, end).trim();
    }

    
    private String extractTagBlock(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";

        int start = xml.indexOf(open);
        if (start == -1) return "";

        int end = xml.indexOf(close, start);
        if (end == -1) return "";

        end += close.length();

        return xml.substring(start, end);
    }

    
    private List<String> extractBlocks(String xml, String tag) {
        List<String> blocks = new ArrayList<>();

        String open = "<" + tag + ">";
        String close = "</" + tag + ">";

        int index = 0;

        while (true) {
            int start = xml.indexOf(open, index);
            if (start == -1) break;

            int end = xml.indexOf(close, start);
            if (end == -1) break;

            end += close.length();

            blocks.add(xml.substring(start, end));

            index = end;
        }

        return blocks;
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

//Based off this example: https://www.geeksforgeeks.org/java/read-and-write-xml-files-in-java/