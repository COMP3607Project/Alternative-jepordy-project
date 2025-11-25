package com.project;
import java.time.LocalDateTime;

/**
 * Turn class represents a single turn/play in the game.
 * Follows Single Responsibility Principle - only stores turn data.
 */
public class Turn {
    private int playerId;
    private String category;
    private int questionValue;
    private String question;
    private String answer;
    private boolean correct;
    private int pointsEarned;
    private int scoreAfterPlay;
    private LocalDateTime timestamp;

    public Turn(int playerId, String category, int questionValue, String question, String answer,
                boolean correct, int pointsEarned, int scoreAfterPlay, LocalDateTime timestamp) {
        this.playerId = playerId;
        this.category = category;
        this.questionValue = questionValue;
        this.question = question;
        this.answer = answer;
        this.correct = correct;
        this.pointsEarned = pointsEarned;
        this.scoreAfterPlay = scoreAfterPlay;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    // Getters following Encapsulation
    public int getPlayerId() { return playerId; }
    public String getCategory() { return category; }
    public int getQuestionValue() { return questionValue; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public boolean isCorrect() { return correct; }
    public int getPointsEarned() { return pointsEarned; }
    public int getScoreAfterPlay() { return scoreAfterPlay; }
    public LocalDateTime getTimestamp() { return timestamp; }

}
