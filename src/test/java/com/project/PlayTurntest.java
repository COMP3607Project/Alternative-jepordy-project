package com.project;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream; 
import java.io.PrintStream;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class PlayTurntest {
    private final InputStream systemIn =  System.in; 
    private final PrintStream systemOut = System.out; 
    private ByteArrayInputStream testIn; 
    private ByteArrayOutputStream testOut; 
    
    private Gameboard board; 

    @Before 
    public void setUp(){
        board = new Gameboard(); 
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut)); 
    }
    @After 
    public void restoreSystemInputOutput(){
        System.setIn(systemIn);
        System.setOut(systemOut);
    }
    private void provideInput(String data){
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
    @Test 
    public void testAddPlayers_validInput(){
        provideInput("2\nAlice\nBob\n");
        PlayTurn playTurn = new PlayTurn(board); 
        List<Player> players = playTurn.getPlayers();
        assertEquals(2, players.size());
        assertEquals("Alice", players.get(0).getName()); 
        assertEquals("Bob", players.get(1).getName()); 
    }
    @Test
    public void testAddPlayers_invalidThenValidInput(){
        provideInput("5\n0\n1\nCharlie\n");
        PlayTurn playTurn = new PlayTurn(board); 

        List<Player> players = playTurn.getPlayers();
        assertEquals(1, players.size()); 
        assertEquals("Charlie", players.get(0).getName());
    }
    @Test
    public void testSwitchPlayer(){
        provideInput("2\nAlice\nBob\n");
        PlayTurn playTurn = new PlayTurn(board); 
        Player first = playTurn.getCurrentPlayer();
        assertEquals("Alice", first.getName()); 
        playTurn.switchPlayer();
        Player second = playTurn.getCurrentPlayer(); 
        assertEquals("Bob", second.getName());

        playTurn.switchPlayer();
        Player loopBack = playTurn.getCurrentPlayer();
        assertEquals("Alice",loopBack.getName() );


         
    }
    @Test
    public void testQuitGame(){
        provideInput("1\nAlice\n");
        PlayTurn playTurn = new PlayTurn(board);

        assertFalse(playTurn.isQuitGame());
        playTurn.quitGame(); 
        assertTrue(playTurn.isQuitGame()); 
    }
}
