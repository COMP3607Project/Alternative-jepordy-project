package com.project;

public class Player implements Observer {
    private String name; 
    private int score; 
    private int questionsAnswered;
    private int correctAnswers;

    public Player(String name){
        this.name = name; 
        this.score = 0;
        this.questionsAnswered = 0;
        this.correctAnswers = 0;
    }

    public String getName(){
        return name; 
    }

    public int getScore(){
        return score; 
    }

    public void addScore(int points){
        this.score += points; 
        this.correctAnswers++;
        update();
    }

    public void updateScore(int points){
        this.score += points; 
        this.correctAnswers++;
        update();
    }

    public void recordQuestionAttempt() {
        this.questionsAnswered++;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public double getAccuracy() {
        if (questionsAnswered == 0) return 0.0;
        return (double) correctAnswers / questionsAnswered * 100;
    }

    @Override
    public void update() {
        // Called when player's score is updated
        System.out.println("  [" + name + " updated: Score=$" + score + "]");
    }

    public String getStats() {
        return String.format("%s - Score: $%d | Questions: %d | Correct: %d | Accuracy: %.1f%%", 
            name, score, questionsAnswered, correctAnswers, getAccuracy());
    }
}
