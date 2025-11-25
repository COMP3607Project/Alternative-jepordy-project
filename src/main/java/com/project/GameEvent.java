package com.project;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Encapsulates game event data following Single Responsibility Principle (SOLID).
 * Represents an event that occurs during the game for logging purposes.
 */
public class GameEvent {
    private final String caseId;
    private final String playerId;
    private final String activity;
    private final LocalDateTime timestamp;
    private final String category;
    private final String questionValue;
    private final String answerGiven;
    private final String result;
    private final String scoreAfterPlay;

    private GameEvent(Builder builder) {
        this.caseId = builder.caseId;
        this.playerId = builder.playerId;
        this.activity = builder.activity;
        this.timestamp = builder.timestamp != null ? builder.timestamp : LocalDateTime.now();
        this.category = builder.category;
        this.questionValue = builder.questionValue;
        this.answerGiven = builder.answerGiven;
        this.result = builder.result;
        this.scoreAfterPlay = builder.scoreAfterPlay;
    }

    // Getters
    public String getCaseId() { return caseId; }
    public String getPlayerId() { return playerId; }
    public String getActivity() { return activity; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getCategory() { return category; }
    public String getQuestionValue() { return questionValue; }
    public String getAnswerGiven() { return answerGiven; }
    public String getResult() { return result; }
    public String getScoreAfterPlay() { return scoreAfterPlay; }

    /**
     * Returns ISO-formatted timestamp string
     */
    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Builder pattern for flexible GameEvent creation (Open/Closed Principle)
     */
    public static class Builder {
        private String caseId;
        private String playerId;
        private String activity;
        private LocalDateTime timestamp;
        private String category = "";
        private String questionValue = "";
        private String answerGiven = "";
        private String result = "";
        private String scoreAfterPlay = "";

        public Builder(String caseId, String activity) {
            this.caseId = caseId;
            this.activity = activity;
        }

        public Builder playerId(String playerId) {
            this.playerId = playerId;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder questionValue(int questionValue) {
            this.questionValue = String.valueOf(questionValue);
            return this;
        }

        public Builder questionValue(String questionValue) {
            this.questionValue = questionValue;
            return this;
        }

        public Builder answerGiven(String answerGiven) {
            this.answerGiven = answerGiven;
            return this;
        }

        public Builder result(boolean correct) {
            this.result = correct ? "Correct" : "Incorrect";
            return this;
        }

        public Builder result(String result) {
            this.result = result;
            return this;
        }

        public Builder scoreAfterPlay(int score) {
            this.scoreAfterPlay = String.valueOf(score);
            return this;
        }

        public Builder scoreAfterPlay(String score) {
            this.scoreAfterPlay = score;
            return this;
        }

        public GameEvent build() {
            return new GameEvent(this);
        }
    }
}
