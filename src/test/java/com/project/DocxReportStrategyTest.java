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
        players.add(new Player(1, "Charlie"));
        players.add(new Player(2, "Diana"));
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
        
        // DOCX placeholder creates a .txt file
        File reportFile = new File(filename + ".docx.txt");
        assertTrue("Report file should be created", reportFile.exists());
        assertTrue("Report file should not be empty", reportFile.length() > 0);
    }
    
    @Test
    public void testGenerateReportContainsPlayerScores() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_docx_report";
        
        docxReportStrategy.generateReport(players, turns, filename);
        
        File reportFile = new File(filename + ".docx.txt");
        String content = Files.readString(reportFile.toPath());
        
        assertTrue("Report should contain player Charlie", content.contains("Charlie"));
        assertTrue("Report should contain player Diana", content.contains("Diana"));
        assertTrue("Report should contain Charlie's score", content.contains("700 points"));
        assertTrue("Report should contain Diana's score", content.contains("450 points"));
    }
    
    @Test
    public void testGenerateReportContainsTurnDetails() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_docx_report";
        
        docxReportStrategy.generateReport(players, turns, filename);
        
        File reportFile = new File(filename + ".docx.txt");
        String content = Files.readString(reportFile.toPath());
        
        assertTrue("Report should contain category", content.contains("Geography"));
        assertTrue("Report should contain question", content.contains("What is the capital of France?"));
        assertTrue("Report should contain answer", content.contains("Paris"));
        assertTrue("Report should show correct answers as 'Yes'", content.contains("Yes"));
        assertTrue("Report should show incorrect answers as 'No'", content.contains("No"));
    }
    
    @Test
    public void testGenerateReportWithTableFormat() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "table_report";
        
        docxReportStrategy.generateReport(players, turns, filename);
        
        File reportFile = new File(filename + ".docx.txt");
        String content = Files.readString(reportFile.toPath());
        
        assertTrue("Report should have table header", content.contains("Turn#"));
        assertTrue("Report should have Player column", content.contains("Player"));
        assertTrue("Report should have Category column", content.contains("Category"));
        assertTrue("Report should have Value column", content.contains("Value"));
    }
    
    @Test
    public void testGenerateReportPrintsPlaceholderMessage() {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_docx_report";
        
        docxReportStrategy.generateReport(players, turns, filename);
        
        String output = outputStreamCaptor.toString();
        assertTrue("Should print placeholder message", output.contains("DOCX report generation requires external library"));
        assertTrue("Should mention required library", output.contains("Apache POI"));
    }
    
    @Test
    public void testGenerateReportWithEmptyData() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "empty_data";
        
        docxReportStrategy.generateReport(new ArrayList<>(), new ArrayList<>(), filename);
        
        File reportFile = new File(filename + ".docx.txt");
        assertTrue("Report should be created even with empty data", reportFile.exists());
    }
}
