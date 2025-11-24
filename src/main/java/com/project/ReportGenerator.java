package com.project;

import java.util.ArrayList;
import java.util.List;

public class ReportGenerator implements Observer {
    private static ReportGenerator instance;
    private List<String> gameReport = new ArrayList<>();
    private List<TurnRecord> turnRecords = new ArrayList<>();
    private String caseId;
    private int turnCount = 0;
    private ReportStrategy reportStrategy;

    private ReportGenerator() {
        // Private constructor for singleton
        this.caseId = "GAME" + System.currentTimeMillis();
        this.reportStrategy = new TextReportStrategy(); // Default strategy
    }

    public static synchronized ReportGenerator getInstance() {
        if (instance == null) {
            instance = new ReportGenerator();
        }
        return instance;
    }

    @Override
    public void update() {
        // Called when game state changes
    }

    public void recordGameStart(List<Player> players) {
        gameReport.clear();
        turnRecords.clear();
        turnCount = 0;

        gameReport.add("\nJEOPARDY PROGRAMMING GAME REPORT");
        gameReport.add("================================\n");
        gameReport.add("Case ID: " + caseId + "\n");

        StringBuilder playerList = new StringBuilder("Players: ");
        for (int i = 0; i < players.size(); i++) {
            playerList.append(players.get(i).getName());
            if (i < players.size() - 1) {
                playerList.append(", ");
            }
        }
        gameReport.add(playerList.toString());
        gameReport.add("\nGameplay Summary:");
        gameReport.add("-----------------");
    }

    public void recordTurn(String playerName, String category, int value,
                          String question, String answer, boolean correct,
                          int pointsEarned, List<Player> players) {
        turnCount++;
        
        String result = correct ? "Correct" : "Incorrect";
        String pointsText = correct ? "(+" + pointsEarned + " pts)" : "(0 pts)";

        gameReport.add("Turn " + turnCount + ": " + playerName + " selected " + category + " for " + value + " pts");
        gameReport.add("Question: " + question);
        gameReport.add("Answer: " + answer + " — " + result + " " + pointsText);

        // Record scores after turn
        StringBuilder scoresAfter = new StringBuilder("Score after turn: ");
        for (int i = 0; i < players.size(); i++) {
            scoresAfter.append(players.get(i).getName()).append(" = ").append(players.get(i).getScore());
            if (i < players.size() - 1) {
                scoresAfter.append(", ");
            }
        }
        gameReport.add(scoresAfter.toString());

        // Store turn record
        turnRecords.add(new TurnRecord(turnCount, playerName, category, value, 
                                       question, answer, correct, pointsEarned));
    }

    public void recordGameEnd(List<Player> players) {
        gameReport.add("\nFinal Scores:");
        
        // Sort players by score
        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
        
        for (Player p : sortedPlayers) {
            gameReport.add(p.getName() + ": " + p.getScore());
        }
    }

    public void generateReport() {
        reportStrategy.generateReport(gameReport, caseId);
    }

    public void saveReport(String filename) {
        reportStrategy.saveReport(filename, gameReport, caseId);
    }

    public void setReportStrategy(ReportStrategy strategy) {
        this.reportStrategy = strategy;
    }

    public ReportStrategy getReportStrategy() {
        return reportStrategy;
    }

    public List<String> getReportLines() {
        return new ArrayList<>(gameReport);
    }

    // Inner class to store turn records
    private static class TurnRecord {
        int turnNumber;
        String playerName;
        String category;
        int value;
        String question;
        String answer;
        boolean correct;
        int pointsEarned;

        TurnRecord(int turnNumber, String playerName, String category, int value,
                   String question, String answer, boolean correct, int pointsEarned) {
            this.turnNumber = turnNumber;
            this.playerName = playerName;
            this.category = category;
            this.value = value;
            this.question = question;
            this.answer = answer;
            this.correct = correct;
            this.pointsEarned = pointsEarned;
        }
    }

    public void resetInstance() {
        caseId = "GAME" + System.currentTimeMillis();
        gameReport.clear();
        turnRecords.clear();
        turnCount = 0;
        reportStrategy = new TextReportStrategy(); // Reset to default strategy
    }

    public void generateDetailedReport() {
        System.out.println("\nDETAILED TURN ANALYSIS");
        System.out.println("======================");
        
        for (TurnRecord turn : turnRecords) {
            System.out.println("\nTurn " + turn.turnNumber + ":");
            System.out.println("  Player: " + turn.playerName);
            System.out.println("  Category: " + turn.category);
            System.out.println("  Value: $" + turn.value);
            System.out.println("  Question: " + turn.question);
            System.out.println("  Player's Answer: " + turn.answer);
            System.out.println("  Result: " + (turn.correct ? "CORRECT" : "INCORRECT"));
            System.out.println("  Points Earned: " + turn.pointsEarned);
        }
        System.out.println();
    }
}
