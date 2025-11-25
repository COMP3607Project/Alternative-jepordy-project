package com.project;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DOCX Report Strategy implementing ReportStrategy interface (Strategy Pattern).
 * Follows Single Responsibility Principle - only handles DOCX report generation.
 * Note: This is a placeholder implementation. To fully implement DOCX generation,
 * add Apache POI dependency to pom.xml
 */
public class DocxReportStrategy implements ReportStrategy {
    
    @Override
    public void generateReport(List<Player> players, List<Turn> turns, String filename) {
        String fullFilename = filename + ".docx";
        
        // TODO: Implement DOCX generation using Apache POI library
        // For now, create a simple text-based placeholder
        
        System.out.println("DOCX report generation requires external library (Apache POI).");
        System.out.println("Generating text-based placeholder for: " + fullFilename);
        
        try (FileOutputStream fos = new FileOutputStream(fullFilename + ".txt")) {
            StringBuilder content = new StringBuilder();
            
            content.append("JEOPARDY GAME REPORT (DOCX Placeholder)\n");
            content.append("=".repeat(80)).append("\n\n");
            
            // Final Scores
            content.append("FINAL SCORES\n");
            content.append("-".repeat(80)).append("\n");
            for (Player player : players) {
                content.append(String.format("Player %d: %s - %d points%n", 
                    player.getId(), player.getName(), player.getScore()));
            }
            content.append("\n");
            
            // Turn-by-Turn Table
            content.append("TURN-BY-TURN RUNDOWN\n");
            content.append("-".repeat(80)).append("\n");
            content.append(String.format("%-6s | %-15s | %-20s | %-8s | %-10s | %-8s | %-12s%n",
                "Turn#", "Player", "Category", "Value", "Correct?", "Points", "Total"));
            content.append("-".repeat(80)).append("\n");
            
            int turnNumber = 1;
            for (Turn turn : turns) {
                Player player = findPlayerById(players, turn.getPlayerId());
                String playerName = player != null ? player.getName() : "Unknown";
                
                content.append(String.format("%-6d | %-15s | %-20s | %-8d | %-10s | %-8d | %-12d%n",
                    turnNumber++,
                    truncate(playerName, 15),
                    truncate(turn.getCategory(), 20),
                    turn.getQuestionValue(),
                    turn.isCorrect() ? "Yes" : "No",
                    turn.getPointsEarned(),
                    turn.getScoreAfterPlay()));
                    
                content.append(String.format("  Q: %s%n", turn.getQuestion()));
                content.append(String.format("  A: %s%n%n", turn.getAnswer()));
            }
            
            content.append("-".repeat(80)).append("\n");
            content.append("Report generated: ").append(
                java.time.LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            fos.write(content.toString().getBytes());
            System.out.println("DOCX placeholder report generated: " + fullFilename + ".txt");
            System.out.println("To generate actual DOCX, add Apache POI to pom.xml");
            
        } catch (IOException e) {
            System.err.println("Error generating DOCX report: " + e.getMessage());
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
