package com.project;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTest {
    
    private Player player;
    
    @Before
    public void setUp() {
        player = new Player("Alice");
    }
    
    @Test
    public void testPlayerInitialization() {
        assertEquals("Player name should be Alice", "Alice", player.getName());
        assertEquals("Initial score should be 0", 0, player.getScore());
        assertEquals("Initial questions answered should be 0", 0, player.getQuestionsAnswered());
        assertEquals("Initial correct answers should be 0", 0, player.getCorrectAnswers());
    }
    
    @Test
    public void testGetName() {
        assertEquals("Name should match", "Alice", player.getName());
    }
    
    @Test
    public void testGetScore() {
        assertEquals("Initial score should be 0", 0, player.getScore());
    }
    
    @Test
    public void testAddScore() {
        player.addScore(100);
        assertEquals("Score should be 100", 100, player.getScore());
        assertEquals("Correct answers should be incremented", 1, player.getCorrectAnswers());
    }
    
    @Test
    public void testAddScoreMultipleTimes() {
        player.addScore(100);
        player.addScore(200);
        player.addScore(300);
        assertEquals("Score should be 600", 600, player.getScore());
        assertEquals("Correct answers should be 3", 3, player.getCorrectAnswers());
    }
    
    @Test
    public void testUpdateScore() {
        player.updateScore(150);
        assertEquals("Score should be 150", 150, player.getScore());
        assertEquals("Correct answers should be incremented", 1, player.getCorrectAnswers());
    }
    
    @Test
    public void testRecordQuestionAttempt() {
        player.recordQuestionAttempt();
        assertEquals("Questions answered should be 1", 1, player.getQuestionsAnswered());
        
        player.recordQuestionAttempt();
        assertEquals("Questions answered should be 2", 2, player.getQuestionsAnswered());
    }
    
    @Test
    public void testGetQuestionsAnswered() {
        player.recordQuestionAttempt();
        player.recordQuestionAttempt();
        player.recordQuestionAttempt();
        assertEquals("Questions answered should be 3", 3, player.getQuestionsAnswered());
    }
    
    @Test
    public void testGetCorrectAnswers() {
        player.addScore(100);
        player.addScore(200);
        assertEquals("Correct answers should be 2", 2, player.getCorrectAnswers());
    }
    
    @Test
    public void testGetAccuracyWithNoAttempts() {
        double accuracy = player.getAccuracy();
        assertEquals("Accuracy with no attempts should be 0.0", 0.0, accuracy, 0.01);
    }
    
    @Test
    public void testGetAccuracyWithAllCorrect() {
        player.recordQuestionAttempt();
        player.recordQuestionAttempt();
        player.recordQuestionAttempt();
        player.addScore(100);
        player.addScore(200);
        player.addScore(300);
        
        double accuracy = player.getAccuracy();
        assertEquals("Accuracy should be 100%", 100.0, accuracy, 0.01);
    }
    
    @Test
    public void testGetAccuracyPartial() {
        // 3 attempts, 2 correct
        player.recordQuestionAttempt();
        player.recordQuestionAttempt();
        player.recordQuestionAttempt();
        player.addScore(100);
        player.addScore(200);
        
        double accuracy = player.getAccuracy();
        assertEquals("Accuracy should be 66.67%", 66.67, accuracy, 0.01);
    }
    
    @Test
    public void testGetStats() {
        player.addScore(500);
        player.recordQuestionAttempt();
        
        String stats = player.getStats();
        assertTrue("Stats should contain player name", stats.contains("Alice"));
        assertTrue("Stats should contain score", stats.contains("500"));
        assertTrue("Stats should contain questions count", stats.contains("Questions: 1"));
        assertTrue("Stats should contain correct count", stats.contains("Correct: 1"));
        assertTrue("Stats should contain accuracy", stats.contains("Accuracy"));
    }
    
    @Test
    public void testImplementsObserver() {
        assertTrue("Player should implement Observer", player instanceof Observer);
    }
    
    @Test
    public void testUpdateMethod() {
        // Update method should not throw exception
        player.update();
        assertTrue("Update should complete successfully", true);
    }
    
    @Test
    public void testMultiplePlayers() {
        Player bob = new Player("Bob");
        
        player.addScore(100);
        bob.addScore(200);
        
        assertEquals("Alice score should be 100", 100, player.getScore());
        assertEquals("Bob score should be 200", 200, bob.getScore());
        assertNotEquals("Players should have different scores", player.getScore(), bob.getScore());
    }
    
    @Test
    public void testPlayerNameUnchanged() {
        String originalName = player.getName();
        player.addScore(100);
        player.recordQuestionAttempt();
        
        assertEquals("Name should remain unchanged", originalName, player.getName());
    }
    
    @Test
    public void testScoreNeverNegative() {
        player.addScore(100);
        assertEquals("Score should be positive", 100, player.getScore());
        // Score operations only add, never subtract
        assertTrue("Score should be >= 0", player.getScore() >= 0);
    }
}
