package com.project;

public class Player {
    private String name;
    private Integer score; 

    public Player(String name)
    {
        this.name = name;
        this.score = 0;
    }

    void updateScore(int score)
    {
        this.score = score + this.score;
    }

    int getScore()
    {
        return this.score;
    }
}
