package com.project;

/**
 * Player class following Single Responsibility Principle (SOLID).
 * Responsible only for managing player data.
 */
public class Player {
    private static int nextId = 1;
    private int id;
    private String name; 
    private int score; 

    public Player(String name){
        this.id = nextId++;
        this.name = name; 
        this.score = 0; 
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name; 
    }

    public int getScore(){
        return score; 
    }

    public void updateScore(int points){
        this.score += points; 
    }
}
