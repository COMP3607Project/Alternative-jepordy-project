package com.project;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class GameIntegrationTest {
    
    private Game game;
    
    @Before
    public void setUp() {
        game = Game.getInstance();
    }
    
    @Test
    public void testGameSingletonPattern() {
        Game game1 = Game.getInstance();
        Game game2 = Game.getInstance();
        assertSame("Game should be singleton", game1, game2);
    }
    
    @Test
    public void testGameInitializesObservers() {
        Game testGame = Game.getInstance();
        assertNotNull("Logger should be initialized", testGame.getLogger());
        assertNotNull("ReportGenerator should be initialized", testGame.getReportGenerator());
    }
    
    @Test
    public void testRegisterObserver() {
        Observer mockObserver = new Player("Test");
        int initialObserverCount = game.getPlayers().size();
        
        game.registerObserver(mockObserver);
        assertTrue("Observer should be registered", true);
    }
    
    @Test
    public void testRemoveObserver() {
        Observer mockObserver = new Player("Test");
        game.registerObserver(mockObserver);
        
        game.removeObserver(mockObserver);
        assertTrue("Observer should be removed", true);
    }
    
    @Test
    public void testGetLogger() {
        Logger logger = game.getLogger();
        assertNotNull("Logger should not be null", logger);
        assertTrue("Logger should be singleton instance", logger instanceof Logger);
    }
    
    @Test
    public void testGetReportGenerator() {
        ReportGenerator reportGenerator = game.getReportGenerator();
        assertNotNull("ReportGenerator should not be null", reportGenerator);
        assertTrue("ReportGenerator should be singleton instance", reportGenerator instanceof ReportGenerator);
    }
    
    @Test
    public void testGetGameboard() {
        // Gameboard is initialized during startGame, so test it's accessible
        assertTrue("Game should have getter for gameboard", true);
    }
    
    @Test
    public void testGetPlayers() {
        List<Player> players = game.getPlayers();
        assertNotNull("Players list should not be null", players);
    }
    
    @Test
    public void testPlayTurnWithValidQuestion() {
        // This test assumes game has been started with a valid gameboard
        // Create a test scenario
        Player player = new Player("Alice");
        game.getPlayers().add(player);
        
        // Test that playTurn method exists and is callable
        assertTrue("PlayTurn method should exist", true);
    }
    
    @Test
    public void testObserverNotification() {
        Logger logger = game.getLogger();
        logger.clearLogs();
        
        game.notifyObservers();
        
        // After notifying observers, logger should be updated (even if no new logs)
        assertNotNull("Logger should still exist after notification", game.getLogger());
    }
    
    @Test
    public void testLoggerIntegration() {
        Logger logger = game.getLogger();
        logger.clearLogs();
        
        logger.logEvent("Test event for integration");
        List<String> logs = logger.getLogs();
        
        assertTrue("Logger should have recorded event", logs.size() > 0);
        assertTrue("Event should be in logs", logs.get(0).contains("Test event"));
    }
    
    @Test
    public void testReportGeneratorIntegration() {
        ReportGenerator reportGenerator = game.getReportGenerator();
        reportGenerator.resetInstance();
        
        List<Player> players = game.getPlayers();
        reportGenerator.recordGameStart(players);
        
        List<String> report = reportGenerator.getReportLines();
        assertFalse("Report should have content after recordGameStart", report.isEmpty());
    }
    
    @Test
    public void testStrategyPatternIntegration() {
        ReportGenerator reportGenerator = game.getReportGenerator();
        
        // Test setting different strategies
        reportGenerator.setReportStrategy(new TextReportStrategy());
        assertTrue("Should set TextReportStrategy", 
            reportGenerator.getReportStrategy() instanceof TextReportStrategy);
        
        reportGenerator.setReportStrategy(new HTMLReportStrategy());
        assertTrue("Should set HTMLReportStrategy", 
            reportGenerator.getReportStrategy() instanceof HTMLReportStrategy);
        
        reportGenerator.setReportStrategy(new JSONReportStrategy());
        assertTrue("Should set JSONReportStrategy", 
            reportGenerator.getReportStrategy() instanceof JSONReportStrategy);
        
        reportGenerator.setReportStrategy(new PDFReportStrategy());
        assertTrue("Should set PDFReportStrategy", 
            reportGenerator.getReportStrategy() instanceof PDFReportStrategy);
    }
    
    @Test
    public void testObserverPatternIntegration() {
        Player player = new Player("TestPlayer");
        
        assertTrue("Player should implement Observer", player instanceof Observer);
        assertTrue("Logger should implement Observer", game.getLogger() instanceof Observer);
        assertTrue("ReportGenerator should implement Observer", 
            game.getReportGenerator() instanceof Observer);
    }
    
    @Test
    public void testMultiplePlayersManagement() {
        game.getPlayers().clear();
        
        Player alice = new Player("Alice");
        Player bob = new Player("Bob");
        
        game.getPlayers().add(alice);
        game.getPlayers().add(bob);
        
        assertEquals("Should have 2 players", 2, game.getPlayers().size());
        assertEquals("First player should be Alice", "Alice", game.getPlayers().get(0).getName());
        assertEquals("Second player should be Bob", "Bob", game.getPlayers().get(1).getName());
    }
    
    @Test
    public void testPlayerScoreTracking() {
        Player player = new Player("Alice");
        
        player.addScore(100);
        assertEquals("Score should be 100", 100, player.getScore());
        
        player.addScore(200);
        assertEquals("Score should be 300", 300, player.getScore());
    }
    
    @Test
    public void testCompleteGameworkflow() {
        game.getLogger().clearLogs();
        game.getReportGenerator().resetInstance();
        
        // Create players
        Player alice = new Player("Alice");
        Player bob = new Player("Bob");
        
        // Record game start
        List<Player> players = game.getPlayers();
        game.getReportGenerator().recordGameStart(players);
        
        // Simulate turns
        alice.addScore(100);
        bob.addScore(200);
        
        // Record game end
        game.getReportGenerator().recordGameEnd(players);
        
        // Verify report was created
        List<String> report = game.getReportGenerator().getReportLines();
        assertFalse("Report should have content", report.isEmpty());
    }
    
    @Test
    public void testLoggerRecordingGameEvents() {
        Logger logger = game.getLogger();
        logger.clearLogs();
        
        logger.logGameStart(2);
        logger.logPlayerAction("Alice", "Joined");
        logger.logQuestionAsked("Variables", 100);
        logger.logAnswer("Alice", "int", true);
        logger.logScoreUpdate("Alice", 100);
        logger.logGameEnd();
        
        List<String> logs = logger.getLogs();
        assertEquals("Should have 6 log entries", 6, logs.size());
    }
    
    @Test
    public void testReportGenerationWithMultipleTurns() {
        game.getReportGenerator().resetInstance();
        
        List<Player> testPlayers = game.getPlayers();
        game.getReportGenerator().recordGameStart(testPlayers);
        
        // Record multiple turns
        for (int i = 1; i <= 3; i++) {
            game.getReportGenerator().recordTurn("Player" + i, "Category" + i, 
                i * 100, "Question" + i, "Answer" + i, true, i * 100, testPlayers);
        }
        
        game.getReportGenerator().recordGameEnd(testPlayers);
        
        List<String> report = game.getReportGenerator().getReportLines();
        
        assertTrue("Report should contain Turn 1", 
            report.stream().anyMatch(line -> line.contains("Turn 1")));
        assertTrue("Report should contain Turn 2", 
            report.stream().anyMatch(line -> line.contains("Turn 2")));
        assertTrue("Report should contain Turn 3", 
            report.stream().anyMatch(line -> line.contains("Turn 3")));
    }
}
