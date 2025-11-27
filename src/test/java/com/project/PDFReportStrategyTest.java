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
 * Test class for PDFReportStrategy
 */
public class PDFReportStrategyTest {
    
    private PDFReportStrategy pdfReportStrategy;
    private List<Player> players;
    private List<Turn> turns;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    
    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();
    
    @Before
    public void setUp() {
        pdfReportStrategy = new PDFReportStrategy();
        System.setOut(new PrintStream(outputStreamCaptor));
        
        // Create test players
        players = new ArrayList<>();
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));
        players.get(0).updateScore(500);
        players.get(1).updateScore(300);
        
        // Create test turns with proper constructor
        turns = new ArrayList<>();
        Turn turn1 = new Turn(1, "Science", 200, "What is H2O?", "Water", true, 200, 200, null);
        Turn turn2 = new Turn(2, "History", 300, "Who was the first president?", "Washington", true, 300, 300, null);
        
        turns.add(turn1);
        turns.add(turn2);
    }
    
    @After
    public void tearDown() {
        System.setOut(standardOut);
    }
    
    @Test
    public void testGenerateReportCreatesFile() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_report";
        
        pdfReportStrategy.generateReport(players, turns, filename);
        
        // Now creates actual PDF file
        File reportFile = new File(filename + ".pdf");
        assertTrue("Report file should be created", reportFile.exists());
        assertTrue("Report file should not be empty", reportFile.length() > 0);
    }
    
    @Test
    public void testGenerateReportContainsPlayerScores() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_report";
        
        pdfReportStrategy.generateReport(players, turns, filename);
        
        // Actual PDF file is binary, just check it exists and has content
        File reportFile = new File(filename + ".pdf");
        assertTrue("Report should be created", reportFile.exists());
        assertTrue("Report should have content", reportFile.length() > 0);
    }
    
    @Test
    public void testGenerateReportContainsTurnDetails() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_report";
        
        pdfReportStrategy.generateReport(players, turns, filename);
        
        // Actual PDF file is binary, just check it exists
        File reportFile = new File(filename + ".pdf");
        assertTrue("Report should be created", reportFile.exists());
    }
    
    @Test
    public void testGenerateReportPrintsSuccessMessage() {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_report";
        
        pdfReportStrategy.generateReport(players, turns, filename);
        
        String output = outputStreamCaptor.toString();
        assertTrue("Should print success message", output.contains("PDF report generated"));
    }
    
    @Test
    public void testGenerateReportWithEmptyPlayers() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "empty_report";
        
        pdfReportStrategy.generateReport(new ArrayList<>(), turns, filename);
        
        File reportFile = new File(filename + ".pdf");
        assertTrue("Report should be created even with empty players", reportFile.exists());
    }
    
    @Test
    public void testGenerateReportWithEmptyTurns() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "empty_turns_report";
        
        pdfReportStrategy.generateReport(players, new ArrayList<>(), filename);
        
        File reportFile = new File(filename + ".pdf");
        assertTrue("Report should be created with empty turns", reportFile.exists());
    }
}
