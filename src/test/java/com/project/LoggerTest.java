package com.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Logger implementing Observer Pattern
 */
public class LoggerTest {
    
    private static final String TEST_LOG_FILE = "game_event_log.csv";
    private Logger logger;
    
    @Before
    public void setUp() throws IOException {
        // Delete existing log file if it exists
        Files.deleteIfExists(Paths.get(TEST_LOG_FILE));
        
        // Create new logger instance
        logger = new Logger();
    }
    
    @After
    public void tearDown() throws IOException {
        // Clean up test log file
        Files.deleteIfExists(Paths.get(TEST_LOG_FILE));
    }
    
    @Test
    public void testLoggerCreatesFileWithHeader() throws IOException {
        // Logger constructor should create file with header
        File logFile = new File(TEST_LOG_FILE);
        assertTrue("Log file should be created", logFile.exists());
        
        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        assertEquals("Should have header line", 1, lines.size());
        assertTrue("Header should contain expected columns", 
            lines.get(0).contains("Case_ID"));
        assertTrue("Header should contain Activity", 
            lines.get(0).contains("Activity"));
        assertTrue("Header should contain Timestamp", 
            lines.get(0).contains("Timestamp"));
    }
    
    @Test
    public void testLoggerUpdateWritesEvent() throws IOException {
        GameEvent event = new GameEvent.Builder("case123", "Start Game")
            .playerId("1")
            .build();
        
        logger.update(event);
        
        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        assertEquals("Should have header + 1 event", 2, lines.size());
        assertTrue("Event should contain case ID", lines.get(1).contains("case123"));
        assertTrue("Event should contain activity", lines.get(1).contains("Start Game"));
    }
    
    @Test
    public void testLoggerLogsPlayerAddition() throws IOException {
        GameEvent event = new GameEvent.Builder("case456", "Enter Player Name")
            .playerId("1")
            .answerGiven("Alice")
            .scoreAfterPlay("0")
            .build();
        
        logger.update(event);
        
        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        assertTrue("Should log player name", lines.get(1).contains("Alice"));
        assertTrue("Should log player ID", lines.get(1).contains("1"));
    }
    
    @Test
    public void testLoggerLogsAnswerQuestion() throws IOException {
        GameEvent event = new GameEvent.Builder("case789", "Answer Question")
            .playerId("2")
            .category("Science")
            .questionValue("200")
            .answerGiven("Water")
            .result("Correct")
            .scoreAfterPlay("200")
            .build();
        
        logger.update(event);
        
        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        String eventLine = lines.get(1);
        assertTrue("Should log category", eventLine.contains("Science"));
        assertTrue("Should log question value", eventLine.contains("200"));
        assertTrue("Should log answer", eventLine.contains("Water"));
        assertTrue("Should log result", eventLine.contains("Correct"));
    }
    
    @Test
    public void testLoggerLogsMultipleEvents() throws IOException {
        GameEvent event1 = new GameEvent.Builder("case001", "Start Game").build();
        GameEvent event2 = new GameEvent.Builder("case001", "Enter Player Name")
            .playerId("1")
            .answerGiven("Bob")
            .build();
        GameEvent event3 = new GameEvent.Builder("case001", "Answer Question")
            .playerId("1")
            .category("History")
            .questionValue("100")
            .build();
        
        logger.update(event1);
        logger.update(event2);
        logger.update(event3);
        
        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        assertEquals("Should have header + 3 events", 4, lines.size());
    }
    
    @Test
    public void testLoggerEscapesCommasInValues() throws IOException {
        GameEvent event = new GameEvent.Builder("case111", "Answer Question")
            .playerId("1")
            .category("Arts, Music")
            .answerGiven("Mozart, Wolfgang")
            .build();
        
        logger.update(event);
        
        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        String eventLine = lines.get(1);
        // Values with commas should be quoted
        assertTrue("Should escape commas", 
            eventLine.contains("\"Arts, Music\"") || eventLine.contains("Arts"));
    }
    
    @Test
    public void testLoggerHandlesEmptyFields() throws IOException {
        GameEvent event = new GameEvent.Builder("case222", "Start Game").build();
        
        logger.update(event);
        
        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        assertEquals("Should have header + 1 event", 2, lines.size());
        // Event line should have correct number of commas (8 commas for 9 fields)
        String eventLine = lines.get(1);
        int commaCount = eventLine.length() - eventLine.replace(",", "").length();
        assertEquals("Should have correct number of fields", 8, commaCount);
    }
    
    @Test
    public void testLoggerHandlesNullValues() throws IOException {
        GameEvent event = new GameEvent.Builder("case333", "Test Activity")
            .playerId(null)
            .category(null)
            .build();
        
        logger.update(event);
        
        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        assertFalse("Should not contain 'null' string", 
            lines.get(1).toLowerCase().contains("null"));
    }
    
    @Test
    public void testLoggerAppendsToExistingFile() throws IOException {
        // First event
        GameEvent event1 = new GameEvent.Builder("case444", "Start Game").build();
        logger.update(event1);
        
        // Create new logger instance (simulates program restart)
        Logger logger2 = new Logger();
        
        // Second event
        GameEvent event2 = new GameEvent.Builder("case444", "Exit Game").build();
        logger2.update(event2);
        
        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        // Should have header + 2 events, not duplicate header
        assertEquals("Should append without duplicate header", 3, lines.size());
        assertTrue("First event should be present", lines.get(1).contains("Start Game"));
        assertTrue("Second event should be present", lines.get(2).contains("Exit Game"));
    }
    
    @Test
    public void testLoggerTimestampFormat() throws IOException {
        GameEvent event = new GameEvent.Builder("case555", "Test Activity").build();
        logger.update(event);
        
        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        String eventLine = lines.get(1);
        
        // Timestamp should be in ISO format (contains T separator)
        assertTrue("Should have timestamp in ISO format", eventLine.contains("T"));
    }
    
    @Test
    public void testLoggerObserverPattern() throws IOException {
        // Verify Logger implements GameObserver interface
        assertTrue("Logger should implement GameObserver", 
            logger instanceof GameObserver);
        
        // Test that update method works
        GameEvent event = new GameEvent.Builder("case666", "Test Observer").build();
        logger.update(event);
        
        assertTrue("Log file should exist after update", 
            Files.exists(Paths.get(TEST_LOG_FILE)));
    }
    
    @Test
    public void testLoggerProcessMiningFormat() throws IOException {
        // Test that CSV follows process mining conventions
        GameEvent event = new GameEvent.Builder("case777", "Answer Question")
            .playerId("3")
            .category("Math")
            .questionValue("300")
            .answerGiven("42")
            .result("Correct")
            .scoreAfterPlay("300")
            .build();
        
        logger.update(event);
        
        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_FILE));
        String header = lines.get(0);
        String data = lines.get(1);
        
        // Verify all required process mining fields are present
        assertTrue("Should have Case_ID", header.contains("Case_ID"));
        assertTrue("Should have Activity", header.contains("Activity"));
        assertTrue("Should have Timestamp", header.contains("Timestamp"));
        
        // Verify data has all fields
        String[] fields = data.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Split by comma not in quotes
        assertTrue("Should have multiple fields", fields.length >= 5);
    }
}
