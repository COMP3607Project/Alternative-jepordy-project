package com.project;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class JSONGameLoaderTest {

    private Path tempFile;

    @Before
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("json_test", ".json");
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testLoadValidJSON() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("[" +
            "{" +
                "\"Category\":\"Science\"," +
                "\"Value\":100," +
                "\"Question\":\"What is H2O?\"," +
                "\"Options\":{\"A\":\"Water\",\"B\":\"Oxygen\",\"C\":\"Hydrogen\",\"D\":\"Carbon\"}," +
                "\"CorrectAnswer\":\"A\"" +
            "}," +
            "{" +
                "\"Category\":\"Math\"," +
                "\"Value\":200," +
                "\"Question\":\"What is 2+2?\"," +
                "\"Options\":{\"A\":\"3\",\"B\":\"4\",\"C\":\"5\",\"D\":\"6\"}," +
                "\"CorrectAnswer\":\"B\"" +
            "}" +
        "]");
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(2, categories.size());
        
        Category science = categories.get(0);
        assertEquals("Science", science.getName());
        Questions q1 = science.getQuestions().get(0);
        assertEquals("What is H2O?", q1.getQuestions());
        assertEquals(100, q1.getValue());
        assertEquals("Water", q1.getOptions().get(0).getName());
        
        Category math = categories.get(1);
        assertEquals("Math", math.getName());
        Questions q2 = math.getQuestions().get(0);
        assertEquals("4", q2.getOptions().get(1).getName());
    }

    @Test
    public void testLoadJSONWithWhitespace() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("[\n" +
            "  {\n" +
            "    \"Category\" : \"Science\" ,\n" +
            "    \"Value\" : 100 ,\n" +
            "    \"Question\" : \"Test?\" ,\n" +
            "    \"Options\" : { \"A\" : \"Opt1\" , \"B\" : \"Opt2\" , \"C\" : \"Opt3\" , \"D\" : \"Opt4\" } ,\n" +
            "    \"CorrectAnswer\" : \"A\"\n" +
            "  }\n" +
        "]");
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(1, categories.size());
        assertEquals("Science", categories.get(0).getName());
    }

    @Test
    public void testLoadEmptyJSON() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("[]");
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertTrue(categories.isEmpty());
    }

    @Test
    public void testLoadNonExistentFile() {
        JSONGameLoader loader = new JSONGameLoader("no_such_file.json");
        loader.load();
        List<Category> categories = loader.getCategories();

        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    public void testLoadMalformedJSON() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("[{\"Category\":\"Science\",\"Value\":}]"); // Missing value
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        // Should handle error gracefully
        assertNotNull(categories);
    }

    @Test
    public void testLoadJSONMissingFields() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("[{\"Category\":\"Science\"}]"); // Missing most fields
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        // Should create category even with missing data
        assertEquals(1, categories.size());
    }

    @Test
    public void testLoadJSONMultipleSameCategory() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("[" +
            "{\"Category\":\"Science\",\"Value\":100,\"Question\":\"Q1?\",\"Options\":{\"A\":\"A1\",\"B\":\"B1\",\"C\":\"C1\",\"D\":\"D1\"},\"CorrectAnswer\":\"A\"}," +
            "{\"Category\":\"Science\",\"Value\":200,\"Question\":\"Q2?\",\"Options\":{\"A\":\"A2\",\"B\":\"B2\",\"C\":\"C2\",\"D\":\"D2\"},\"CorrectAnswer\":\"B\"}" +
        "]");
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(1, categories.size());
        assertEquals(2, categories.get(0).getQuestions().size());
    }

    @Test
    public void testLoadJSONWithNestedQuotes() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("[{" +
            "\"Category\":\"Test\"," +
            "\"Value\":100," +
            "\"Question\":\"What is \\\"quoted\\\"?\"," +
            "\"Options\":{\"A\":\"Option A\",\"B\":\"Option B\",\"C\":\"Option C\",\"D\":\"Option D\"}," +
            "\"CorrectAnswer\":\"A\"" +
        "}]");
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(1, categories.size());
    }
}