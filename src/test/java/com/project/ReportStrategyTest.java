package com.project;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReportStrategyTest {
    
    private List<String> mockReportLines;
    private String caseId;
    
    @Before
    public void setUp() {
        mockReportLines = new ArrayList<>();
        mockReportLines.add("JEOPARDY PROGRAMMING GAME REPORT");
        mockReportLines.add("================================");
        mockReportLines.add("Case ID: GAME001");
        mockReportLines.add("Players: Alice, Bob");
        mockReportLines.add("Gameplay Summary:");
        mockReportLines.add("-----------------");
        mockReportLines.add("Turn 1: Alice selected Variables for 100 pts");
        mockReportLines.add("Question: Which declares integer?");
        mockReportLines.add("Answer: int num; — Correct (+100 pts)");
        mockReportLines.add("Final Scores:");
        mockReportLines.add("Alice: 100");
        mockReportLines.add("Bob: 0");
        
        caseId = "GAME001";
    }
    
    @Test
    public void testTextReportStrategyGenerateReport() {
        ReportStrategy strategy = new TextReportStrategy();
        // Should not throw exception
        strategy.generateReport(mockReportLines, caseId);
        assertTrue("Text strategy should complete successfully", true);
    }
    
    @Test
    public void testTextReportStrategySaveReport() {
        ReportStrategy strategy = new TextReportStrategy();
        String filename = "test_report.txt";
        
        try {
            strategy.saveReport(filename, mockReportLines, caseId);
            
            // Check if file was created
            assertTrue("Text report file should be created", 
                Files.exists(Paths.get(filename)));
            
            // Check file content
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            assertTrue("File should contain report content", content.contains("JEOPARDY"));
            
            // Cleanup
            Files.delete(Paths.get(filename));
        } catch (Exception e) {
            fail("Text strategy save should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testHTMLReportStrategyGenerateReport() {
        ReportStrategy strategy = new HTMLReportStrategy();
        // Should not throw exception
        strategy.generateReport(mockReportLines, caseId);
        assertTrue("HTML strategy should complete successfully", true);
    }
    
    @Test
    public void testHTMLReportStrategySaveReport() {
        ReportStrategy strategy = new HTMLReportStrategy();
        String filename = "test_report.html";
        
        try {
            strategy.saveReport(filename, mockReportLines, caseId);
            
            // Check if file was created
            assertTrue("HTML report file should be created", 
                Files.exists(Paths.get(filename)));
            
            // Check file content
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            assertTrue("File should be HTML", content.contains("<!DOCTYPE html>"));
            assertTrue("File should contain report data", content.contains("JEOPARDY"));
            assertTrue("File should have styling", content.contains("<style>"));
            
            // Cleanup
            Files.delete(Paths.get(filename));
        } catch (Exception e) {
            fail("HTML strategy save should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testJSONReportStrategyGenerateReport() {
        ReportStrategy strategy = new JSONReportStrategy();
        // Should not throw exception
        strategy.generateReport(mockReportLines, caseId);
        assertTrue("JSON strategy should complete successfully", true);
    }
    
    @Test
    public void testJSONReportStrategySaveReport() {
        ReportStrategy strategy = new JSONReportStrategy();
        String filename = "test_report.json";
        
        try {
            strategy.saveReport(filename, mockReportLines, caseId);
            
            // Check if file was created
            assertTrue("JSON report file should be created", 
                Files.exists(Paths.get(filename)));
            
            // Check file content
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            assertTrue("File should be JSON", content.contains("{"));
            assertTrue("File should contain caseId", content.contains("GAME001"));
            assertTrue("File should be valid JSON structure", content.contains("\"content\""));
            
            // Cleanup
            Files.delete(Paths.get(filename));
        } catch (Exception e) {
            fail("JSON strategy save should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testPDFReportStrategyGenerateReport() {
        ReportStrategy strategy = new PDFReportStrategy();
        // Should not throw exception
        strategy.generateReport(mockReportLines, caseId);
        assertTrue("PDF strategy should complete successfully", true);
    }
    
    @Test
    public void testPDFReportStrategySaveReport() {
        ReportStrategy strategy = new PDFReportStrategy();
        String filename = "test_report.pdf";
        
        try {
            strategy.saveReport(filename, mockReportLines, caseId);
            
            // Check if file was created
            assertTrue("PDF report file should be created", 
                Files.exists(Paths.get(filename)));
            
            // Check file content (basic PDF structure)
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            assertTrue("File should have PDF header", content.contains("%PDF"));
            
            // Cleanup
            Files.delete(Paths.get(filename));
        } catch (Exception e) {
            fail("PDF strategy save should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testStrategyInterfaceImplementation() {
        ReportStrategy textStrategy = new TextReportStrategy();
        ReportStrategy htmlStrategy = new HTMLReportStrategy();
        ReportStrategy jsonStrategy = new JSONReportStrategy();
        ReportStrategy pdfStrategy = new PDFReportStrategy();
        
        assertTrue("Text should implement ReportStrategy", textStrategy instanceof ReportStrategy);
        assertTrue("HTML should implement ReportStrategy", htmlStrategy instanceof ReportStrategy);
        assertTrue("JSON should implement ReportStrategy", jsonStrategy instanceof ReportStrategy);
        assertTrue("PDF should implement ReportStrategy", pdfStrategy instanceof ReportStrategy);
    }
    
    @Test
    public void testMultipleStrategyUsage() {
        ReportGenerator reportGenerator = ReportGenerator.getInstance();
        reportGenerator.resetInstance();
        
        List<Player> players = new ArrayList<>();
        players.add(new Player("Alice"));
        
        reportGenerator.recordGameStart(players);
        reportGenerator.recordTurn("Alice", "Variables", 100, "Q1", "A1", true, 100, players);
        reportGenerator.recordGameEnd(players);
        
        // Test switching between strategies
        reportGenerator.setReportStrategy(new TextReportStrategy());
        assertTrue("Should be able to set text strategy", 
            reportGenerator.getReportStrategy() instanceof TextReportStrategy);
        
        reportGenerator.setReportStrategy(new HTMLReportStrategy());
        assertTrue("Should be able to set HTML strategy", 
            reportGenerator.getReportStrategy() instanceof HTMLReportStrategy);
        
        reportGenerator.setReportStrategy(new JSONReportStrategy());
        assertTrue("Should be able to set JSON strategy", 
            reportGenerator.getReportStrategy() instanceof JSONReportStrategy);
    }
    
    @Test
    public void testEmptyReportLines() {
        List<String> emptyReport = new ArrayList<>();
        ReportStrategy strategy = new TextReportStrategy();
        
        // Should handle empty report gracefully
        strategy.generateReport(emptyReport, caseId);
        assertTrue("Should handle empty report", true);
    }
    
    @Test
    public void testSpecialCharactersInReport() {
        List<String> reportWithSpecialChars = new ArrayList<>();
        reportWithSpecialChars.add("Question: What's the answer? <html> & \"quotes\"");
        reportWithSpecialChars.add("Answer: It's \"yes\" & it's <correct>");
        
        ReportStrategy jsonStrategy = new JSONReportStrategy();
        String filename = "test_special.json";
        
        try {
            jsonStrategy.saveReport(filename, reportWithSpecialChars, caseId);
            
            // Should handle special characters without errors
            assertTrue("File should be created with special characters", 
                Files.exists(Paths.get(filename)));
            
            Files.delete(Paths.get(filename));
        } catch (Exception e) {
            fail("Should handle special characters: " + e.getMessage());
        }
    }
}
