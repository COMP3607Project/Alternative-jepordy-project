package com.project;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * TXT Report Strategy implementing ReportStrategy interface (Strategy Pattern).
 * Follows Single Responsibility Principle - only handles TXT report generation.
 */
public class TxtReportStrategy implements ReportStrategy {
    
    @Override
    public void generateReport(List<Player> players, List<Turn> turns, String filename) {
        String fullFilename = filename + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(fullFilename))) {
            writer.println("=".repeat(80));
            writer.println("JEOPARDY GAME REPORT");
            writer.println("=".repeat(80));
            writer.println();
            
            // Final Scores Section
            writer.println("FINAL SCORES");
            writer.println("-".repeat(80));
            for (Player player : players) {
                writer.printf("Player %d: %s - %d points%n", 
                    player.getId(), player.getName(), player.getScore());
            }
            writer.println();
            
            // Turn-by-Turn Rundown Section
            writer.println("TURN-BY-TURN RUNDOWN");
            writer.println("-".repeat(80));
            writer.printf("%-6s %-15s %-20s %-8s %-40s %-15s %-10s %-8s %-12s%n",
                "Turn#", "Player", "Category", "Value", "Question", "Answer", "Correct?", "Points", "Total");
            writer.println("-".repeat(80));
            
            int turnNumber = 1;
            for (Turn turn : turns) {
                Player player = findPlayerById(players, turn.getPlayerId());
                String playerName = player != null ? player.getName() : "Unknown";
                
                String question = truncate(turn.getQuestion(), 40);
                String answer = truncate(turn.getAnswer(), 15);
                
                writer.printf("%-6d %-15s %-20s %-8d %-40s %-15s %-10s %-8d %-12d%n",
                    turnNumber++,
                    truncate(playerName, 15),
                    truncate(turn.getCategory(), 20),
                    turn.getQuestionValue(),
                    question,
                    answer,
                    turn.isCorrect() ? "Yes" : "No",
                    turn.getPointsEarned(),
                    turn.getScoreAfterPlay());
            }
            
            writer.println("-".repeat(80));
            writer.println("Report generated: " + 
                java.time.LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            writer.println("=".repeat(80));
            
            System.out.println("TXT report generated: " + fullFilename);
            
        } catch (IOException e) {
            System.err.println("Error generating TXT report: " + e.getMessage());
        }
    }
    
    private Player findPlayerById(List<Player> players, int playerId) {
        for (Player player : players) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null;
    }
    
    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}
