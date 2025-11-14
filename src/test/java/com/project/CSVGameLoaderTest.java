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

public class CSVGameLoaderTest {

    private Path tempFile;

    @Before
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("test", ".csv");
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testLoadSimpleCSV() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("A,B,C\n");
        writer.write("1,2,3\n");
        writer.close();

        CSVGameLoader loader = new CSVGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        assertEquals(2, result.size());
        assertEquals(3, result.get(0).size());
        assertEquals("A", result.get(0).get(0));
        assertEquals("2", result.get(1).get(1));
    }

    @Test
    public void testLoadEmptyCSV() throws IOException {
        // empty file, leave as is

        CSVGameLoader loader = new CSVGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testLoadNonExistingFile() {
        CSVGameLoader loader = new CSVGameLoader("non_existing_file.csv");

        // Should not throw, should return empty list
        List<List<String>> result = loader.load();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCSVValuesAreSplitCorrectly() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("X,Y,Z\n");
        writer.close();

        CSVGameLoader loader = new CSVGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        assertEquals(1, result.size());
        assertEquals("Y", result.get(0).get(1));
    }
}
