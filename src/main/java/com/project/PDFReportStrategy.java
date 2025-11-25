package com.project;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * PDF Report Strategy implementing ReportStrategy interface (Strategy Pattern).
 * Follows Single Responsibility Principle - only handles PDF report generation.
 * Note: This is a placeholder implementation. To fully implement PDF generation,
 * add Apache PDFBox or iText dependency to pom.xml
 */
public class PDFReportStrategy implements ReportStrategy {
    
    @Override
    public void generateReport(List<Player> players, List<Turn> turns, String filename) {
        String fullFilename = filename + ".pdf";
        
        // TODO: Implement PDF generation using Apache PDFBox or iText library
        // For now, create a simple text-based placeholder
        
        System.out.println("PDF report generation requires external library (Apache PDFBox or iText).");
        System.out.println("Generating text-based placeholder for: " + fullFilename);
        
        try (FileOutputStream fos = new FileOutputStream(fullFilename + ".txt")) {
            StringBuilder content = new StringBuilder();
            
            content.append("JEOPARDY GAME REPORT (PDF Placeholder)\n");
            content.append("=".repeat(80)).append("\n\n");
            
            // Final Scores
            content.append("FINAL SCORES\n");
            content.append("-".repeat(80)).append("\n");
            for (Player player : players) {
                content.append(String.format("Player %d: %s - %d points%n", 
                    player.getId(), player.getName(), player.getScore()));
            }
            content.append("\n");
            
            // Turn-by-Turn
            content.append("TURN-BY-TURN RUNDOWN\n");
            content.append("-".repeat(80)).append("\n");
            
            int turnNumber = 1;
            for (Turn turn : turns) {
                Player player = findPlayerById(players, turn.getPlayerId());
                String playerName = player != null ? player.getName() : "Unknown";
                
                content.append(String.format("Turn %d: %s%n", turnNumber++, playerName));
                content.append(String.format("  Category: %s | Value: %d%n", 
                    turn.getCategory(), turn.getQuestionValue()));
                content.append(String.format("  Question: %s%n", turn.getQuestion()));
                content.append(String.format("  Answer: %s | %s%n", 
                    turn.getAnswer(), turn.isCorrect() ? "Correct" : "Incorrect"));
                content.append(String.format("  Points Earned: %d | Running Total: %d%n", 
                    turn.getPointsEarned(), turn.getScoreAfterPlay()));
                content.append("\n");
            }
            
            content.append("-".repeat(80)).append("\n");
            content.append("Report generated: ").append(
                java.time.LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            fos.write(content.toString().getBytes());
            System.out.println("PDF placeholder report generated: " + fullFilename + ".txt");
            System.out.println("To generate actual PDF, add iText or Apache PDFBox to pom.xml");
            
        } catch (IOException e) {
            System.err.println("Error generating PDF report: " + e.getMessage());
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
}
