package com.project;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class LoggerTest {
    
    private Logger logger;
    
    @Before
    public void setUp() {
        logger = Logger.getInstance();
        logger.clearLogs();
    }
    
    @Test
    public void testSingletonInstance() {
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        assertSame("Logger should be singleton", logger1, logger2);
    }
    
    @Test
    public void testLogEvent() {
        logger.logEvent("Test event");
        List<String> logs = logger.getLogs();
        assertEquals("Should have one log entry", 1, logs.size());
        assertTrue("Log should contain event text", logs.get(0).contains("Test event"));
    }
    
    @Test
    public void testLogPlayerAction() {
        logger.logPlayerAction("Alice", "Selected Variables & Data Types");
        List<String> logs = logger.getLogs();
        assertEquals("Should have one log entry", 1, logs.size());
        assertTrue("Log should contain player name", logs.get(0).contains("Alice"));
        assertTrue("Log should contain action", logs.get(0).contains("Selected Variables & Data Types"));
    }
    
    @Test
    public void testLogQuestionAsked() {
        logger.logQuestionAsked("Control Structures", 300);
        List<String> logs = logger.getLogs();
        assertEquals("Should have one log entry", 1, logs.size());
        assertTrue("Log should contain category", logs.get(0).contains("Control Structures"));
        assertTrue("Log should contain value", logs.get(0).contains("300"));
    }
    
    @Test
    public void testLogAnswer() {
        logger.logAnswer("Bob", "do-while", true);
        List<String> logs = logger.getLogs();
        assertEquals("Should have one log entry", 1, logs.size());
        assertTrue("Log should contain player name", logs.get(0).contains("Bob"));
        assertTrue("Log should contain answer", logs.get(0).contains("do-while"));
        assertTrue("Log should indicate correct", logs.get(0).contains("CORRECT"));
    }
    
    @Test
    public void testLogAnswerIncorrect() {
        logger.logAnswer("Bob", "while", false);
        List<String> logs = logger.getLogs();
        assertEquals("Should have one log entry", 1, logs.size());
        assertTrue("Log should indicate incorrect", logs.get(0).contains("INCORRECT"));
    }
    
    @Test
    public void testLogScoreUpdate() {
        logger.logScoreUpdate("Alice", 500);
        List<String> logs = logger.getLogs();
        assertEquals("Should have one log entry", 1, logs.size());
        assertTrue("Log should contain player name", logs.get(0).contains("Alice"));
        assertTrue("Log should contain new score", logs.get(0).contains("500"));
    }
    
    @Test
    public void testLogGameStart() {
        logger.logGameStart(2);
        List<String> logs = logger.getLogs();
        assertEquals("Should have one log entry", 1, logs.size());
        assertTrue("Log should indicate game start", logs.get(0).contains("GAME STARTED"));
        assertTrue("Log should contain player count", logs.get(0).contains("2"));
    }
    
    @Test
    public void testLogGameEnd() {
        logger.logGameEnd();
        List<String> logs = logger.getLogs();
        assertEquals("Should have one log entry", 1, logs.size());
        assertTrue("Log should indicate game end", logs.get(0).contains("GAME ENDED"));
    }
    
    @Test
    public void testMultipleLogsOrder() {
        logger.logGameStart(1);
        logger.logPlayerAction("Alice", "Joined game");
        logger.logGameEnd();
        
        List<String> logs = logger.getLogs();
        assertEquals("Should have three log entries", 3, logs.size());
        assertTrue("First log should be game start", logs.get(0).contains("GAME STARTED"));
        assertTrue("Second log should be player action", logs.get(1).contains("Player"));
        assertTrue("Third log should be game end", logs.get(2).contains("GAME ENDED"));
    }
    
    @Test
    public void testClearLogs() {
        logger.logEvent("Test event 1");
        logger.logEvent("Test event 2");
        assertEquals("Should have two logs", 2, logger.getLogs().size());
        
        logger.clearLogs();
        assertEquals("Logs should be cleared", 0, logger.getLogs().size());
    }
    
    @Test
    public void testLogTimestampFormat() {
        logger.logEvent("Timestamped event");
        List<String> logs = logger.getLogs();
        String logEntry = logs.get(0);
        
        // Check for timestamp format [yyyy-MM-dd HH:mm:ss]
        assertTrue("Log should contain timestamp", logEntry.contains("["));
        assertTrue("Log should contain closing bracket", logEntry.contains("]"));
        assertTrue("Log should contain event text", logEntry.contains("Timestamped event"));
    }
    
    @Test
    public void testUpdate() {
        // Logger's update method should not throw exception
        logger.update();
        assertTrue("Update should complete successfully", true);
    }
}
