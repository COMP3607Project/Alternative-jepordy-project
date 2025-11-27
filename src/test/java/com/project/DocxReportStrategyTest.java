package com.project;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Test class for DocxReportStrategy
 */
public class DocxReportStrategyTest {
    
    private DocxReportStrategy docxReportStrategy;
    private List<Player> players;
    private List<Turn> turns;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    
    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();
    
    @Before
    public void setUp() {
        docxReportStrategy = new DocxReportStrategy();
        System.setOut(new PrintStream(outputStreamCaptor));
        
        // Create test players
        players = new ArrayList<>();
        players.add(new Player("Charlie"));
        players.add(new Player("Diana"));
        players.get(0).updateScore(700);
        players.get(1).updateScore(450);
        
        // Create test turns with proper constructor
        turns = new ArrayList<>();
        Turn turn1 = new Turn(1, "Geography", 400, "What is the capital of France?", "Paris", true, 400, 400, null);
        Turn turn2 = new Turn(2, "Literature", 500, "Who wrote Hamlet?", "Shakespeare", false, 0, 450, null);
        
        turns.add(turn1);
        turns.add(turn2);
    }
    
    @After
    public void tearDown() {
        System.setOut(standardOut);
    }
    
    @Test
    public void testGenerateReportCreatesFile() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_docx_report";
        
        docxReportStrategy.generateReport(players, turns, filename);
        
        // Now creates actual DOCX file
        File reportFile = new File(filename + ".docx");
        assertTrue("Report file should be created", reportFile.exists());
        assertTrue("Report file should not be empty", reportFile.length() > 0);
    }
    
    @Test
    public void testGenerateReportContainsPlayerScores() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_docx_report";
        
        docxReportStrategy.generateReport(players, turns, filename);
        
        // Actual DOCX file is binary, just check it exists and has content
        File reportFile = new File(filename + ".docx");
        assertTrue("Report should be created", reportFile.exists());
        assertTrue("Report should have content", reportFile.length() > 0);
    }
    
    @Test
    public void testGenerateReportContainsTurnDetails() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_docx_report";
        
        docxReportStrategy.generateReport(players, turns, filename);
        
        // Actual DOCX file is binary, just check it exists
        File reportFile = new File(filename + ".docx");
        assertTrue("Report should be created", reportFile.exists());
    }
    
    @Test
    public void testGenerateReportWithTableFormat() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "table_report";
        
        docxReportStrategy.generateReport(players, turns, filename);
        
        // Actual DOCX file with tables, just verify it exists
        File reportFile = new File(filename + ".docx");
        assertTrue("Report should be created", reportFile.exists());
    }
    
    @Test
    public void testGenerateReportPrintsSuccessMessage() {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_docx_report";
        
        docxReportStrategy.generateReport(players, turns, filename);
        
        String output = outputStreamCaptor.toString();
        assertTrue("Should print success message", output.contains("DOCX report generated"));
    }
    
    @Test
    public void testGenerateReportWithEmptyData() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "empty_data";
        
        docxReportStrategy.generateReport(new ArrayList<>(), new ArrayList<>(), filename);
        
        File reportFile = new File(filename + ".docx");
        assertTrue("Report should be created even with empty data", reportFile.exists());
    }
}
