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
        players.add(new Player( "Alice"));
        players.add(new Player( "Bob"));
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
        
        // PDF placeholder creates a .txt file
        File reportFile = new File(filename + ".pdf.txt");
        assertTrue("Report file should be created", reportFile.exists());
        assertTrue("Report file should not be empty", reportFile.length() > 0);
    }
    
    @Test
    public void testGenerateReportContainsPlayerScores() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_report";
        
        pdfReportStrategy.generateReport(players, turns, filename);
        
        File reportFile = new File(filename + ".pdf.txt");
        String content = Files.readString(reportFile.toPath());
        
        assertTrue("Report should contain player Alice", content.contains("Alice"));
        assertTrue("Report should contain player Bob", content.contains("Bob"));
        assertTrue("Report should contain Alice's score", content.contains("500 points"));
        assertTrue("Report should contain Bob's score", content.contains("300 points"));
    }
    
    @Test
    public void testGenerateReportContainsTurnDetails() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_report";
        
        pdfReportStrategy.generateReport(players, turns, filename);
        
        File reportFile = new File(filename + ".pdf.txt");
        String content = Files.readString(reportFile.toPath());
        
        assertTrue("Report should contain category", content.contains("Science"));
        assertTrue("Report should contain question", content.contains("What is H2O?"));
        assertTrue("Report should contain answer", content.contains("Water"));
        assertTrue("Report should indicate correct answer", content.contains("Correct"));
    }
    
    @Test
    public void testGenerateReportPrintsPlaceholderMessage() {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_report";
        
        pdfReportStrategy.generateReport(players, turns, filename);
        
        String output = outputStreamCaptor.toString();
        assertTrue("Should print placeholder message", output.contains("PDF report generation requires external library"));
        assertTrue("Should mention required libraries", output.contains("Apache PDFBox or iText"));
    }
    
    @Test
    public void testGenerateReportWithEmptyPlayers() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "empty_report";
        
        pdfReportStrategy.generateReport(new ArrayList<>(), turns, filename);
        
        File reportFile = new File(filename + ".pdf.txt");
        assertTrue("Report should be created even with empty players", reportFile.exists());
    }
    
    @Test
    public void testGenerateReportWithEmptyTurns() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "empty_turns_report";
        
        pdfReportStrategy.generateReport(players, new ArrayList<>(), filename);
        
        File reportFile = new File(filename + ".pdf.txt");
        String content = Files.readString(reportFile.toPath());
        
        assertTrue("Report should still contain players", content.contains("Alice"));
        assertTrue("Report should have turn section", content.contains("TURN-BY-TURN RUNDOWN"));
    }
}
