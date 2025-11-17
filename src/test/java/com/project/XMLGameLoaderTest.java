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

public class XMLGameLoaderTest {

    private Path tempFile;

    @Before
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("xml_test", ".xml");
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testLoadSimpleXML() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write(
            "<Root>" +
                "<QuestionItem>" +
                    "<Question>A?</Question>" +
                    "<Answer1>B</Answer1>" +
                    "<Answer2>C</Answer2>" +
                "</QuestionItem>" +
            "</Root>"
        );
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).size());
        assertEquals("A?", result.get(0).get(0));
        assertEquals("B", result.get(0).get(1));
        assertEquals("C", result.get(0).get(2));
    }

    @Test
    public void testNestedXMLStructure() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write(
            "<Root>" +
                "<QuestionItem>" +
                    "<Question>" +
                        "<Text>Main question?</Text>" +
                    "</Question>" +
                    "<Answers>" +
                        "<Correct>D</Correct>" +
                        "<Wrong>E</Wrong>" +
                    "</Answers>" +
                "</QuestionItem>" +
            "</Root>"
        );
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        // flattenElement should give: "Main question?", "D", "E"
        assertEquals(1, result.size());
        assertEquals("Main question?", result.get(0).get(0));
        assertEquals("D", result.get(0).get(1));
        assertEquals("E", result.get(0).get(2));
    }

    @Test
    public void testMultipleQuestionItems() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write(
            "<Root>" +
                "<QuestionItem>" +
                    "<Field>One</Field>" +
                "</QuestionItem>" +

                "<QuestionItem>" +
                    "<Field>Two</Field>" +
                "</QuestionItem>" +
            "</Root>"
        );
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        assertEquals(2, result.size());
        assertEquals("One", result.get(0).get(0));
        assertEquals("Two", result.get(1).get(0));
    }

    @Test
    public void testWhitespaceAndFormatting() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write(
            "<Root>" +
                "<QuestionItem>" +
                    "<Text>   Hello World   </Text>" +
                "</QuestionItem>" +
            "</Root>"
        );
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        assertEquals("Hello World", result.get(0).get(0)); // trims whitespace
    }

    @Test
    public void testLoadEmptyXML() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write(""); // empty file
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        // Loader returns empty list on parsing error
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLoadNonExistingFile() {
        XMLGameLoader loader = new XMLGameLoader("does_not_exist.xml");
        List<List<String>> result = loader.load();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeeplyNestedElements() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write(
            "<Root>" +
                "<QuestionItem>" +
                    "<Wrapper>" +
                        "<Level1>" +
                            "<Level2>" +
                                "<Actual>Value</Actual>" +
                            "</Level2>" +
                        "</Level1>" +
                    "</Wrapper>" +
                "</QuestionItem>" +
            "</Root>"
        );
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        List<List<String>> result = loader.load();

        assertEquals(1, result.size());
        assertEquals("Value", result.get(0).get(0));
    }
}