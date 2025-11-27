package com.project;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class CSVGameLoaderTest {

    private Path tempFile;

    @Before
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("test", ".csv");
    }

    @After
    public void tearDown() throws IOException {
        // Small delay to ensure file handles are released on Windows
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testLoadValidCSV() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n");
        writer.write("Science,100,What is H2O?,Water,Oxygen,Hydrogen,Carbon,A\n");
        writer.write("Science,200,What is CO2?,Carbon Dioxide,Oxygen,Water,Nitrogen,A\n");
        writer.write("History,100,Who was first president?,Washington,Lincoln,Jefferson,Adams,A\n");
        writer.close();

        CSVGameLoader loader = new CSVGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(2, categories.size());
        
        // Check Science category
        Category science = categories.get(0);
        assertEquals("Science", science.getName());
        assertEquals(2, science.getQuestions().size());
        
        Questions q1 = science.getQuestions().get(0);
        assertEquals("What is H2O?", q1.getQuestions());
        assertEquals(100, q1.getValue());
        assertEquals("A", q1.getAnswer());
        assertEquals(4, q1.getOptions().size());
        assertEquals("Water", q1.getOptions().get(0).getName());
        
        // Check History category
        Category history = categories.get(1);
        assertEquals("History", history.getName());
        assertEquals(1, history.getQuestions().size());
    }

    @Test
    public void testLoadCSVWithWhitespace() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n");
        writer.write("  Math  , 100 , What is 2+2? , 3 , 4 , 5 , 6 , B \n");
        writer.close();

        CSVGameLoader loader = new CSVGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(1, categories.size());
        assertEquals("Math", categories.get(0).getName());
        Questions q = categories.get(0).getQuestions().get(0);
        assertEquals("What is 2+2?", q.getQuestions());
        assertEquals("4", q.getOptions().get(1).getName());
        assertEquals("B", q.getAnswer());
    }

    @Test
    public void testLoadEmptyCSV() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n");
        writer.close();

        CSVGameLoader loader = new CSVGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertTrue(categories.isEmpty());
    }

    @Test
    public void testLoadCSVWithInsufficientColumns() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n");
        writer.write("Science,100,Incomplete\n"); // Missing columns
        writer.write("Math,200,Complete?,A,B,C,D,A\n"); // Valid row
        writer.close();

        CSVGameLoader loader = new CSVGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(1, categories.size());
        assertEquals("Math", categories.get(0).getName());
    }

    @Test
    public void testLoadNonExistentFile() {
        CSVGameLoader loader = new CSVGameLoader("non_existent_file.csv");
        loader.load();
        List<Category> categories = loader.getCategories();

        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    public void testLoadCSVWithInvalidValue() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n");
        writer.write("Science,NotANumber,Question?,A,B,C,D,A\n");
        writer.close();

        CSVGameLoader loader = new CSVGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        // Should handle error gracefully
        assertTrue(categories.isEmpty());
    }

    @Test
    public void testLoadCSVMultipleCategoriesSameName() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n");
        writer.write("Science,100,Q1?,A,B,C,D,A\n");
        writer.write("Science,200,Q2?,A,B,C,D,B\n");
        writer.close();

        CSVGameLoader loader = new CSVGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(1, categories.size());
        assertEquals(2, categories.get(0).getQuestions().size());
    }

    @Test
    public void testLoadCSVWithEmptyFields() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("Category,Value,Question,OptionA,OptionB,OptionC,OptionD,CorrectAnswer\n");
        writer.write("Science,100,,,,,D,A\n");
        writer.close();

        CSVGameLoader loader = new CSVGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(1, categories.size());
        Questions q = categories.get(0).getQuestions().get(0);
        assertEquals("", q.getQuestions());
        assertEquals("", q.getOptions().get(0).getName());
    }
}

