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
    public void testLoadValidXML() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("<?xml version=\"1.0\"?>\n" +
            "<Questions>\n" +
            "  <QuestionItem>\n" +
            "    <Category>Science</Category>\n" +
            "    <Value>100</Value>\n" +
            "    <QuestionText>What is H2O?</QuestionText>\n" +
            "    <Options>\n" +
            "      <OptionA>Water</OptionA>\n" +
            "      <OptionB>Oxygen</OptionB>\n" +
            "      <OptionC>Hydrogen</OptionC>\n" +
            "      <OptionD>Carbon</OptionD>\n" +
            "    </Options>\n" +
            "    <CorrectAnswer>A</CorrectAnswer>\n" +
            "  </QuestionItem>\n" +
            "  <QuestionItem>\n" +
            "    <Category>Math</Category>\n" +
            "    <Value>200</Value>\n" +
            "    <QuestionText>What is 2+2?</QuestionText>\n" +
            "    <Options>\n" +
            "      <OptionA>3</OptionA>\n" +
            "      <OptionB>4</OptionB>\n" +
            "      <OptionC>5</OptionC>\n" +
            "      <OptionD>6</OptionD>\n" +
            "    </Options>\n" +
            "    <CorrectAnswer>B</CorrectAnswer>\n" +
            "  </QuestionItem>\n" +
            "</Questions>");
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(2, categories.size());
        
        Category science = categories.get(0);
        assertEquals("Science", science.getName());
        Questions q1 = science.getQuestions().get(0);
        assertEquals("What is H2O?", q1.getQuestions());
        assertEquals(100, q1.getValue());
        assertEquals("Water", q1.getOptions().get(0).getName());
        assertEquals("A", q1.getAnswer());
        
        Category math = categories.get(1);
        assertEquals("Math", math.getName());
    }

    @Test
    public void testLoadXMLWithWhitespace() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("<Questions>\n" +
            "  <QuestionItem>\n" +
            "    <Category>  Science  </Category>\n" +
            "    <Value>  100  </Value>\n" +
            "    <QuestionText>  Test?  </QuestionText>\n" +
            "    <Options>\n" +
            "      <OptionA>  A  </OptionA>\n" +
            "      <OptionB>  B  </OptionB>\n" +
            "      <OptionC>  C  </OptionC>\n" +
            "      <OptionD>  D  </OptionD>\n" +
            "    </Options>\n" +
            "    <CorrectAnswer>  A  </CorrectAnswer>\n" +
            "  </QuestionItem>\n" +
            "</Questions>");
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(1, categories.size());
        Questions q = categories.get(0).getQuestions().get(0);
        assertEquals("Test?", q.getQuestions());
        assertEquals("A", q.getOptions().get(0).getName());
    }

    @Test
    public void testLoadEmptyXML() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("<Questions></Questions>");
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertTrue(categories.isEmpty());
    }

    @Test
    public void testLoadNonExistentFile() {
        XMLGameLoader loader = new XMLGameLoader("does_not_exist.xml");
        loader.load();
        List<Category> categories = loader.getCategories();

        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    public void testLoadMalformedXML() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("<Questions><QuestionItem><Category>Test</Category>"); // Missing closing tags
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertNotNull(categories);
    }

    @Test
    public void testLoadXMLWithMissingTags() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("<Questions>\n" +
            "  <QuestionItem>\n" +
            "    <Category>Science</Category>\n" +
            "    <Value>100</Value>\n" +
            "  </QuestionItem>\n" +
            "</Questions>");
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        // Should handle missing tags gracefully
        assertNotNull(categories);
    }

    @Test
    public void testLoadXMLMultipleSameCategory() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("<Questions>\n" +
            "  <QuestionItem>\n" +
            "    <Category>Science</Category>\n" +
            "    <Value>100</Value>\n" +
            "    <QuestionText>Q1?</QuestionText>\n" +
            "    <Options><OptionA>A1</OptionA><OptionB>B1</OptionB><OptionC>C1</OptionC><OptionD>D1</OptionD></Options>\n" +
            "    <CorrectAnswer>A</CorrectAnswer>\n" +
            "  </QuestionItem>\n" +
            "  <QuestionItem>\n" +
            "    <Category>Science</Category>\n" +
            "    <Value>200</Value>\n" +
            "    <QuestionText>Q2?</QuestionText>\n" +
            "    <Options><OptionA>A2</OptionA><OptionB>B2</OptionB><OptionC>C2</OptionC><OptionD>D2</OptionD></Options>\n" +
            "    <CorrectAnswer>B</CorrectAnswer>\n" +
            "  </QuestionItem>\n" +
            "</Questions>");
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        assertEquals(1, categories.size());
        assertEquals(2, categories.get(0).getQuestions().size());
    }

    @Test
    public void testLoadXMLWithInvalidValue() throws IOException {
        FileWriter writer = new FileWriter(tempFile.toFile());
        writer.write("<Questions>\n" +
            "  <QuestionItem>\n" +
            "    <Category>Science</Category>\n" +
            "    <Value>NotANumber</Value>\n" +
            "    <QuestionText>Q?</QuestionText>\n" +
            "    <Options><OptionA>A</OptionA><OptionB>B</OptionB><OptionC>C</OptionC><OptionD>D</OptionD></Options>\n" +
            "    <CorrectAnswer>A</CorrectAnswer>\n" +
            "  </QuestionItem>\n" +
            "</Questions>");
        writer.close();

        XMLGameLoader loader = new XMLGameLoader(tempFile.toString());
        loader.load();
        List<Category> categories = loader.getCategories();

        // Should handle error gracefully
        assertTrue(categories.isEmpty());
    }
}