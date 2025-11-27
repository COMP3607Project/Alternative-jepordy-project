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
 * Test class for TxtReportStrategy
 */
public class TxtReportStrategyTest {
    
    private TxtReportStrategy txtReportStrategy;
    private List<Player> players;
    private List<Turn> turns;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    
    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();
    
    @Before
    public void setUp() {
        txtReportStrategy = new TxtReportStrategy();
        System.setOut(new PrintStream(outputStreamCaptor));
        
        // Create test players
        players = new ArrayList<>();
        players.add(new Player( "Eve"));
        players.add(new Player( "Frank"));
        players.get(0).updateScore(850);
        players.get(1).updateScore(620);
        
        // Create test turns with proper constructor
        turns = new ArrayList<>();
        Turn turn1 = new Turn(1, "Science", 300, "What is photosynthesis?", "Process plants use", true, 300, 300, null);
        Turn turn2 = new Turn(2, "Math", 200, "What is Pi?", "3.14159", true, 200, 200, null);
        Turn turn3 = new Turn(1, "History", 400, "When was WWI?", "1914", false, 0, 300, null);
        
        turns.add(turn1);
        turns.add(turn2);
        turns.add(turn3);
    }
    
    @After
    public void tearDown() {
        System.setOut(standardOut);
    }
    
    @Test
    public void testGenerateReportCreatesFile() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_txt_report";
        
        txtReportStrategy.generateReport(players, turns, filename);
        
        File reportFile = new File(filename + ".txt");
        assertTrue("Report file should be created", reportFile.exists());
        assertTrue("Report file should not be empty", reportFile.length() > 0);
    }
    
    @Test
    public void testGenerateReportContainsPlayerScores() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_txt_report";
        
        txtReportStrategy.generateReport(players, turns, filename);
        
        File reportFile = new File(filename + ".txt");
        String content = Files.readString(reportFile.toPath());
        
        assertTrue("Report should contain player Eve", content.contains("Eve"));
        assertTrue("Report should contain player Frank", content.contains("Frank"));
        assertTrue("Report should contain Eve's score", content.contains("850 points"));
        assertTrue("Report should contain Frank's score", content.contains("620 points"));
    }
    
    @Test
    public void testGenerateReportContainsTurnDetails() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "test_txt_report";
        
        txtReportStrategy.generateReport(players, turns, filename);
        
        File reportFile = new File(filename + ".txt");
        String content = Files.readString(reportFile.toPath());
        
        assertTrue("Report should contain category", content.contains("Science"));
        assertTrue("Report should contain question text", content.contains("photosynthesis"));
        assertTrue("Report should contain part of answer", content.contains("Process"));
        assertTrue("Report should show correct answers", content.contains("Yes"));
        assertTrue("Report should show incorrect answers", content.contains("No"));
    }
    
    @Test
    public void testGenerateReportHasProperFormatting() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "format_test";
        
        txtReportStrategy.generateReport(players, turns, filename);
        
        File reportFile = new File(filename + ".txt");
        String content = Files.readString(reportFile.toPath());
        
        assertTrue("Report should have title", content.contains("JEOPARDY GAME REPORT"));
        assertTrue("Report should have scores section", content.contains("FINAL SCORES"));
        assertTrue("Report should have turns section", content.contains("TURN-BY-TURN RUNDOWN"));
    }
    
    @Test
    public void testGenerateReportWithTableHeaders() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "headers_test";
        
        txtReportStrategy.generateReport(players, turns, filename);
        
        File reportFile = new File(filename + ".txt");
        String content = Files.readString(reportFile.toPath());
        
        assertTrue("Report should have Turn# column", content.contains("Turn#"));
        assertTrue("Report should have Player column", content.contains("Player"));
        assertTrue("Report should have Category column", content.contains("Category"));
        assertTrue("Report should have Question column", content.contains("Question"));
    }
    
    @Test
    public void testGenerateReportWithEmptyPlayers() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "empty_players";
        
        txtReportStrategy.generateReport(new ArrayList<>(), turns, filename);
        
        File reportFile = new File(filename + ".txt");
        assertTrue("Report should be created with empty players", reportFile.exists());
    }
    
    @Test
    public void testGenerateReportWithEmptyTurns() throws Exception {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "empty_turns";
        
        txtReportStrategy.generateReport(players, new ArrayList<>(), filename);
        
        File reportFile = new File(filename + ".txt");
        String content = Files.readString(reportFile.toPath());
        
        assertTrue("Report should contain players", content.contains("Eve"));
        assertTrue("Report should have turn section", content.contains("TURN-BY-TURN RUNDOWN"));
    }
    
    @Test
    public void testGenerateReportPrintsSuccessMessage() {
        String filename = tempDir.getRoot().getAbsolutePath() + File.separator + "success_test";
        
        txtReportStrategy.generateReport(players, turns, filename);
        
        String output = outputStreamCaptor.toString();
        assertTrue("Should print success message", output.contains("TXT report generated"));
    }
}
