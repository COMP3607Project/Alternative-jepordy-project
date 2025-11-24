package com.project;
import java.time.LocalDateTime;

public class Turn {
    private int Player;
    private String category;
    private int questionValue;
    private String question;
    private String answer;
    private boolean correct;
    private int pointsEarned;
    private int scoreAfterPlay;
    private LocalDateTime timestamp;

    public Turn(int Player, String category, int questionValue, String question, String answer,
                boolean correct, int pointsEarned, int scoreAfterPlay, LocalDateTime timestamp) {
        this.Player = Player;
        this.category = category;
        this.questionValue = questionValue;
        this.question = question;
        this.answer = answer;
        this.correct = correct;
        this.pointsEarned = pointsEarned;
        this.scoreAfterPlay = scoreAfterPlay;
        this.timestamp = LocalDateTime.now();
    }

}
