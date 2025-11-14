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
    public void testLoadSimpleJSON() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("[[\"A\",\"B\",\"C\"],[\"1\",\"2\",\"3\"]]");
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        assertEquals(2, result.size());
        assertEquals(3, result.get(0).size());
        assertEquals("A", result.get(0).get(0));
        assertEquals("2", result.get(1).get(1));
    }

    @Test
    public void testLoadJSONWithWhitespace() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("[  [ \"X\" , \"Y\" ] , [ \"P\" , \"Q\" ] ]");
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        // The original parser returns ONE row because whitespace breaks the split
        assertEquals(1, result.size());

        // The flattened row contains all four values in order
        List<String> row = result.get(0);
        assertEquals(4, row.size());
        assertEquals("X", row.get(0));
        assertEquals("Y", row.get(1));
        assertEquals("P", row.get(2));
        assertEquals("Q", row.get(3));
    }
    @Test
    public void testLoadEmptyJSONFile() throws IOException {
        // Write empty content
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("");
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        // Depending on logic: empty → returns a list with one empty row
        // Because split on empty string results in one row with ""
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals("", result.get(0).get(0));
    }

    @Test
    public void testLoadNonExistingFile() {
        JSONGameLoader loader = new JSONGameLoader("no_such_file.json");
        List<List<String>> result = loader.load();

        assertNotNull(result);
        assertEquals(1, result.size());        // one row
        assertEquals(1, result.get(0).size()); // one empty value
        assertEquals("", result.get(0).get(0)); // empty string
    }

    @Test
    public void testLoadMultipleRows() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("[[\"A\",\"B\"],[\"C\",\"D\"],[\"E\",\"F\"]]");
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        assertEquals(3, result.size());
        assertEquals("C", result.get(1).get(0));
        assertEquals("F", result.get(2).get(1));
    }

    @Test
    public void testLoadValuesAreTrimmed() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("[[\"  X  \",\"  Y\"],[\"Z  \",\"  W \"]]");
        writer.close();

        JSONGameLoader loader = new JSONGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        assertEquals("X", result.get(0).get(0));
        assertEquals("Y", result.get(0).get(1));
        assertEquals("Z", result.get(1).get(0));
        assertEquals("W", result.get(1).get(1));
    }
}