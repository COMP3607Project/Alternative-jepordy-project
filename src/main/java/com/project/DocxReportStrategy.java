package com.project;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * DOCX Report Strategy implementing ReportStrategy interface (Strategy Pattern).
 * Follows Single Responsibility Principle - only handles DOCX report generation.
 * Uses Apache POI library for DOCX generation.
 */
public class DocxReportStrategy implements ReportStrategy {
    
    @Override
    public void generateReport(List<Player> players, List<Turn> turns, String filename) {
        String fullFilename = filename + ".docx";
        
        try (XWPFDocument document = new XWPFDocument()) {
            
            // Title
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("JEOPARDY GAME REPORT");
            titleRun.setBold(true);
            titleRun.setFontSize(20);
            titleRun.addBreak();
            
            // Final Scores Section
            XWPFParagraph scoresHeading = document.createParagraph();
            XWPFRun scoresRun = scoresHeading.createRun();
            scoresRun.setText("FINAL SCORES");
            scoresRun.setBold(true);
            scoresRun.setFontSize(16);
            scoresRun.addBreak();
            
            // Scores table
            XWPFTable scoresTable = document.createTable(players.size() + 1, 3);
            
            // Header row
            XWPFTableRow headerRow = scoresTable.getRow(0);
            headerRow.getCell(0).setText("Player ID");
            headerRow.getCell(1).setText("Name");
            headerRow.getCell(2).setText("Score");
            
            // Data rows
            int rowIndex = 1;
            for (Player player : players) {
                XWPFTableRow row = scoresTable.getRow(rowIndex++);
                row.getCell(0).setText(String.valueOf(player.getId()));
                row.getCell(1).setText(player.getName());
                row.getCell(2).setText(String.valueOf(player.getScore()));
            }
            
            // Add spacing
            document.createParagraph().createRun().addBreak();
            
            // Turn-by-Turn Section
            XWPFParagraph turnsHeading = document.createParagraph();
            XWPFRun turnsRun = turnsHeading.createRun();
            turnsRun.setText("TURN-BY-TURN RUNDOWN");
            turnsRun.setBold(true);
            turnsRun.setFontSize(16);
            turnsRun.addBreak();
            
            // Turns table
            if (!turns.isEmpty()) {
                XWPFTable turnsTable = document.createTable(turns.size() + 1, 7);
                
                // Header row
                XWPFTableRow turnHeaderRow = turnsTable.getRow(0);
                turnHeaderRow.getCell(0).setText("Turn#");
                turnHeaderRow.getCell(1).setText("Player");
                turnHeaderRow.getCell(2).setText("Category");
                turnHeaderRow.getCell(3).setText("Value");
                turnHeaderRow.getCell(4).setText("Correct?");
                turnHeaderRow.getCell(5).setText("Points");
                turnHeaderRow.getCell(6).setText("Total");
                
                // Data rows
                int turnNumber = 1;
                rowIndex = 1;
                for (Turn turn : turns) {
                    Player player = findPlayerById(players, turn.getPlayerId());
                    String playerName = player != null ? player.getName() : "Unknown";
                    
                    XWPFTableRow row = turnsTable.getRow(rowIndex++);
                    row.getCell(0).setText(String.valueOf(turnNumber++));
                    row.getCell(1).setText(playerName);
                    row.getCell(2).setText(turn.getCategory());
                    row.getCell(3).setText("$" + turn.getQuestionValue());
                    row.getCell(4).setText(turn.isCorrect() ? "Yes" : "No");
                    row.getCell(5).setText(String.format("%+d", turn.getPointsEarned()));
                    row.getCell(6).setText("$" + turn.getScoreAfterPlay());
                }
                
                // Add detailed turn information
                document.createParagraph().createRun().addBreak();
                
                XWPFParagraph detailsHeading = document.createParagraph();
                XWPFRun detailsRun = detailsHeading.createRun();
                detailsRun.setText("DETAILED TURN INFORMATION");
                detailsRun.setBold(true);
                detailsRun.setFontSize(14);
                detailsRun.addBreak();
                
                turnNumber = 1;
                for (Turn turn : turns) {
                    Player player = findPlayerById(players, turn.getPlayerId());
                    String playerName = player != null ? player.getName() : "Unknown";
                    
                    XWPFParagraph turnDetail = document.createParagraph();
                    XWPFRun turnRun = turnDetail.createRun();
                    turnRun.setText(String.format("Turn %d - %s", turnNumber++, playerName));
                    turnRun.setBold(true);
                    turnRun.addBreak();
                    
                    XWPFRun detailRun = turnDetail.createRun();
                    detailRun.setText(String.format("Question: %s", turn.getQuestion()));
                    detailRun.addBreak();
                    detailRun.setText(String.format("Answer: %s", turn.getAnswer()));
                    detailRun.addBreak();
                    detailRun.addBreak();
                }
            }
            
            // Footer
            XWPFParagraph footer = document.createParagraph();
            footer.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun footerRun = footer.createRun();
            footerRun.setItalic(true);
            footerRun.setFontSize(10);
            footerRun.setText("Generated: " + 
                java.time.LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            // Save document
            try (FileOutputStream out = new FileOutputStream(fullFilename)) {
                document.write(out);
            }
            
            System.out.println("DOCX report generated: " + fullFilename);
            
        } catch (IOException e) {
            System.err.println("Error generating DOCX report: " + e.getMessage());
            e.printStackTrace();
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
