package com.project;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue; 
import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
    private Player player1; 
    private Player player2; 


    @Before
    public void setUp(){
        player1 = new Player("Alice"); 
        player2 =  new Player("Bob"); 

    }
    @Test
    public void testplayerIdAssignment(){
         assertTrue(player1.getId () < player2.getId());

    }
 
    @Test
    public void testPlayerName(){
        assertEquals("Alice", player1.getName()); 
        assertEquals("Bob", player2.getName());

    }
    @Test
    public void testInitialScore(){
        assertEquals(0, player1.getScore()); 
        assertEquals(0, player2.getScore());

    }
    @Test
    public void testUpdateScorePositive(){
        player1.updateScore(100); 
        assertEquals(100, player1.getScore()); 

        player1.updateScore(50);
        assertEquals(150, player1.getScore()); 

    }
    @Test 
    public void testUpdateScoreNegative(){
        player2.updateScore(-10);
        assertEquals(-10, player2.getScore()); 

    }

    @Test
    public void testMultiplePlayerScoresIndependent(){
        player1.updateScore(20); 
        player2.updateScore(30);

        assertEquals(20, player1.getScore()); 
        assertEquals(30, player2.getScore()); 
    }
}
