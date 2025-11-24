package com.project;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class ReportGeneratorTest {
    
    private ReportGenerator reportGenerator;
    private List<Player> players;
    
    @Before
    public void setUp() {
        reportGenerator = ReportGenerator.getInstance();
        reportGenerator.resetInstance();
        
        players = new ArrayList<>();
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));
    }
    
    @Test
    public void testSingletonInstance() {
        ReportGenerator gen1 = ReportGenerator.getInstance();
        ReportGenerator gen2 = ReportGenerator.getInstance();
        assertSame("ReportGenerator should be singleton", gen1, gen2);
    }
    
    @Test
    public void testRecordGameStart() {
        reportGenerator.recordGameStart(players);
        List<String> report = reportGenerator.getReportLines();
        
        assertFalse("Report should not be empty", report.isEmpty());
        assertTrue("Report should contain title", 
            report.stream().anyMatch(line -> line.contains("JEOPARDY")));
        assertTrue("Report should contain case ID", 
            report.stream().anyMatch(line -> line.contains("Case ID")));
        assertTrue("Report should contain players", 
            report.stream().anyMatch(line -> line.contains("Alice") && line.contains("Bob")));
    }
    
    @Test
    public void testRecordTurn() {
        reportGenerator.recordGameStart(players);
        reportGenerator.recordTurn("Alice", "Variables & Data Types", 100,
                "Which declares integer?", "int num;", true, 100, players);
        
        List<String> report = reportGenerator.getReportLines();
        
        assertTrue("Report should contain turn info", 
            report.stream().anyMatch(line -> line.contains("Turn 1")));
        assertTrue("Report should contain player name", 
            report.stream().anyMatch(line -> line.contains("Alice")));
        assertTrue("Report should contain category", 
            report.stream().anyMatch(line -> line.contains("Variables & Data Types")));
        assertTrue("Report should contain value", 
            report.stream().anyMatch(line -> line.contains("100")));
    }
    
    @Test
    public void testRecordMultipleTurns() {
        reportGenerator.recordGameStart(players);
        
        reportGenerator.recordTurn("Alice", "Variables", 100,
                "Q1", "A1", true, 100, players);
        reportGenerator.recordTurn("Bob", "Control", 200,
                "Q2", "A2", true, 200, players);
        reportGenerator.recordTurn("Alice", "Functions", 300,
                "Q3", "A3", false, 0, players);
        
        List<String> report = reportGenerator.getReportLines();
        
        assertTrue("Report should contain Turn 1", 
            report.stream().anyMatch(line -> line.contains("Turn 1")));
        assertTrue("Report should contain Turn 2", 
            report.stream().anyMatch(line -> line.contains("Turn 2")));
        assertTrue("Report should contain Turn 3", 
            report.stream().anyMatch(line -> line.contains("Turn 3")));
    }
    
    @Test
    public void testRecordGameEnd() {
        reportGenerator.recordGameStart(players);
        reportGenerator.recordTurn("Alice", "Variables", 100,
                "Q1", "A1", true, 100, players);
        reportGenerator.recordGameEnd(players);
        
        List<String> report = reportGenerator.getReportLines();
        
        assertTrue("Report should contain Final Scores", 
            report.stream().anyMatch(line -> line.contains("Final Scores")));
    }
    
    @Test
    public void testSetReportStrategy() {
        ReportStrategy textStrategy = new TextReportStrategy();
        reportGenerator.setReportStrategy(textStrategy);
        
        assertSame("Strategy should be set", textStrategy, reportGenerator.getReportStrategy());
    }
    
    @Test
    public void testGetReportStrategy() {
        ReportStrategy currentStrategy = reportGenerator.getReportStrategy();
        assertNotNull("Strategy should not be null", currentStrategy);
        assertTrue("Default strategy should be TextReportStrategy", 
            currentStrategy instanceof TextReportStrategy);
    }
    
    @Test
    public void testChangeReportStrategy() {
        ReportStrategy textStrategy = new TextReportStrategy();
        ReportStrategy htmlStrategy = new HTMLReportStrategy();
        
        reportGenerator.setReportStrategy(textStrategy);
        assertEquals("Strategy should be text", textStrategy, reportGenerator.getReportStrategy());
        
        reportGenerator.setReportStrategy(htmlStrategy);
        assertEquals("Strategy should be html", htmlStrategy, reportGenerator.getReportStrategy());
    }
    
    @Test
    public void testGetReportLines() {
        reportGenerator.recordGameStart(players);
        List<String> report = reportGenerator.getReportLines();
        
        assertNotNull("Report lines should not be null", report);
        assertFalse("Report lines should not be empty", report.isEmpty());
    }
    
    @Test
    public void testResetInstance() {
        reportGenerator.recordGameStart(players);
        assertFalse("Report should have content", reportGenerator.getReportLines().isEmpty());
        
        reportGenerator.resetInstance();
        assertTrue("Report should be cleared after reset", reportGenerator.getReportLines().isEmpty());
    }
    
    @Test
    public void testImplementsObserver() {
        assertTrue("ReportGenerator should implement Observer", reportGenerator instanceof Observer);
    }
    
    @Test
    public void testUpdateMethod() {
        // Update method should not throw exception
        reportGenerator.update();
        assertTrue("Update should complete successfully", true);
    }
    
    @Test
    public void testCorrectAnswerLogging() {
        reportGenerator.recordGameStart(players);
        reportGenerator.recordTurn("Alice", "Category", 100,
                "Question", "Answer", true, 100, players);
        
        List<String> report = reportGenerator.getReportLines();
        assertTrue("Report should contain Correct indicator", 
            report.stream().anyMatch(line -> line.contains("Correct")));
    }
    
    @Test
    public void testIncorrectAnswerLogging() {
        reportGenerator.recordGameStart(players);
        reportGenerator.recordTurn("Alice", "Category", 100,
                "Question", "Wrong", false, 0, players);
        
        List<String> report = reportGenerator.getReportLines();
        assertTrue("Report should contain Incorrect indicator", 
            report.stream().anyMatch(line -> line.contains("Incorrect")));
    }
    
    @Test
    public void testCaseIdGeneration() {
        reportGenerator.resetInstance();
        List<String> report1 = reportGenerator.getReportLines();
        reportGenerator.recordGameStart(players);
        
        String caseId1 = reportGenerator.getReportLines().stream()
            .filter(line -> line.contains("Case ID"))
            .findFirst()
            .orElse("");
        
        reportGenerator.resetInstance();
        reportGenerator.recordGameStart(players);
        
        String caseId2 = reportGenerator.getReportLines().stream()
            .filter(line -> line.contains("Case ID"))
            .findFirst()
            .orElse("");
        
        assertNotEquals("Case IDs should be different", caseId1, caseId2);
    }
    
    @Test
    public void testGameplaySequence() {
        reportGenerator.recordGameStart(players);
        
        // Simulate game sequence
        reportGenerator.recordTurn("Alice", "Variables", 100, "Q1", "A1", true, 100, players);
        reportGenerator.recordTurn("Bob", "Control", 200, "Q2", "A2", true, 200, players);
        reportGenerator.recordTurn("Alice", "Functions", 300, "Q3", "A3", true, 200, players);
        
        reportGenerator.recordGameEnd(players);
        
        List<String> report = reportGenerator.getReportLines();
        
        // Verify report structure
        assertTrue("Should contain game start", report.stream().anyMatch(l -> l.contains("JEOPARDY")));
        assertTrue("Should contain turns", report.stream().anyMatch(l -> l.contains("Turn")));
        assertTrue("Should contain final scores", report.stream().anyMatch(l -> l.contains("Final Scores")));
    }
}
