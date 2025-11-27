package com.project;


import org.junit.Test;



import static org.junit.Assert.*;

public class GameLoaderFactoryTest {

    @Test
    public void testGetCSVLoader() {
        GameLoader loader = GameLoaderFactory.getGameLoader("test.csv");
        assertNotNull(loader);
        assertTrue(loader instanceof CSVGameLoader);
    }

    @Test
    public void testGetJSONLoader() {
        GameLoader loader = GameLoaderFactory.getGameLoader("test.json");
        assertNotNull(loader);
        assertTrue(loader instanceof JSONGameLoader);
    }

    @Test
    public void testGetXMLLoader() {
        GameLoader loader = GameLoaderFactory.getGameLoader("test.xml");
        assertNotNull(loader);
        assertTrue(loader instanceof XMLGameLoader);
    }

    @Test
    public void testGetLoaderCaseInsensitive() {
        GameLoader csvLoader = GameLoaderFactory.getGameLoader("TEST.CSV");
        assertTrue(csvLoader instanceof CSVGameLoader);

        GameLoader jsonLoader = GameLoaderFactory.getGameLoader("file.JSON");
        assertTrue(jsonLoader instanceof JSONGameLoader);

        GameLoader xmlLoader = GameLoaderFactory.getGameLoader("data.XML");
        assertTrue(xmlLoader instanceof XMLGameLoader);
    }

    @Test
    public void testGetLoaderWithPath() {
        GameLoader loader = GameLoaderFactory.getGameLoader("/path/to/file.csv");
        assertTrue(loader instanceof CSVGameLoader);

        loader = GameLoaderFactory.getGameLoader("C:\\Users\\data.json");
        assertTrue(loader instanceof JSONGameLoader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLoaderUnsupportedExtension() {
        GameLoaderFactory.getGameLoader("test.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLoaderNoExtension() {
        GameLoaderFactory.getGameLoader("testfile");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLoaderUnknownFormat() {
        GameLoaderFactory.getGameLoader("data.pdf");
    }
}