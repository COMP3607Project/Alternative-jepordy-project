package com.project;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Logger implements Observer {
    private static Logger instance;
    private List<String> logs = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Logger() {
        // Private constructor for singleton
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    @Override
    public void update() {
        // Logger is notified when game state changes
    }

    public void logEvent(String event) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = "[" + timestamp + "] " + event;
        logs.add(logEntry);
        System.out.println(logEntry);
    }

    public void logPlayerAction(String playerName, String action) {
        logEvent("Player: " + playerName + " -> " + action);
    }

    public void logQuestionAsked(String category, int value) {
        logEvent("Question Asked: Category=" + category + ", Value=$" + value);
    }

    public void logAnswer(String playerName, String answer, boolean correct) {
        String result = correct ? "CORRECT" : "INCORRECT";
        logEvent("Answer: " + playerName + " answered " + result + " - " + answer);
    }

    public void logScoreUpdate(String playerName, int newScore) {
        logEvent("Score Update: " + playerName + " -> $" + newScore);
    }

    public void logGameStart(int numPlayers) {
        logEvent("GAME STARTED with " + numPlayers + " players");
    }

    public void logGameEnd() {
        logEvent("GAME ENDED");
    }

    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    public void clearLogs() {
        logs.clear();
    }

    public void printAllLogs() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("GAME LOG");
        System.out.println("=".repeat(80));
        for (String log : logs) {
            System.out.println(log);
        }
        System.out.println("=".repeat(80) + "\n");
    }
}
