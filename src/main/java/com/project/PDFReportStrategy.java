package com.project;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * PDF Report Strategy implementing ReportStrategy interface (Strategy Pattern).
 * Follows Single Responsibility Principle - only handles PDF report generation.
 * Uses Apache PDFBox library for PDF generation.
 */
public class PDFReportStrategy implements ReportStrategy {
    
    private static final float MARGIN = 50;
    private static final float FONT_SIZE = 12;
    private static final float TITLE_SIZE = 18;
    private static final float HEADING_SIZE = 14;
    
    @Override
    public void generateReport(List<Player> players, List<Turn> turns, String filename) {
        String fullFilename = filename + ".pdf";
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            float yPosition = page.getMediaBox().getHeight() - MARGIN;
            
            // Title
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, TITLE_SIZE);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("JEOPARDY GAME REPORT");
            contentStream.endText();
            yPosition -= 30;
            
            // Horizontal line
            contentStream.moveTo(MARGIN, yPosition);
            contentStream.lineTo(page.getMediaBox().getWidth() - MARGIN, yPosition);
            contentStream.stroke();
            yPosition -= 20;
            
            // Final Scores Section
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, HEADING_SIZE);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("FINAL SCORES");
            contentStream.endText();
            yPosition -= 20;
            
            contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE);
            for (Player player : players) {
                if (yPosition < MARGIN + 50) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = page.getMediaBox().getHeight() - MARGIN;
                }
                
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN + 20, yPosition);
                String scoreText = String.format("Player %d: %s - %d points", 
                    player.getId(), player.getName(), player.getScore());
                contentStream.showText(scoreText);
                contentStream.endText();
                yPosition -= 18;
            }
            
            yPosition -= 15;
            
            // Turn-by-Turn Section
            if (yPosition < MARGIN + 100) {
                contentStream.close();
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = page.getMediaBox().getHeight() - MARGIN;
            }
            
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, HEADING_SIZE);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("TURN-BY-TURN RUNDOWN");
            contentStream.endText();
            yPosition -= 25;
            
            contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE);
            int turnNumber = 1;
            for (Turn turn : turns) {
                Player player = findPlayerById(players, turn.getPlayerId());
                String playerName = player != null ? player.getName() : "Unknown";
                
                // Check if we need a new page
                if (yPosition < MARGIN + 80) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = page.getMediaBox().getHeight() - MARGIN;
                }
                
                // Turn header
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN + 10, yPosition);
                contentStream.showText(String.format("Turn %d: %s", turnNumber++, playerName));
                contentStream.endText();
                yPosition -= 15;
                
                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE);
                
                // Category and Value
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN + 20, yPosition);
                contentStream.showText(String.format("Category: %s | Value: $%d", 
                    truncate(turn.getCategory(), 30), turn.getQuestionValue()));
                contentStream.endText();
                yPosition -= 15;
                
                // Question
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN + 20, yPosition);
                contentStream.showText("Q: " + truncate(turn.getQuestion(), 70));
                contentStream.endText();
                yPosition -= 15;
                
                // Answer and Result
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN + 20, yPosition);
                contentStream.showText(String.format("A: %s | %s", 
                    truncate(turn.getAnswer(), 20), 
                    turn.isCorrect() ? "Correct" : "Incorrect"));
                contentStream.endText();
                yPosition -= 15;
                
                // Points
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN + 20, yPosition);
                contentStream.showText(String.format("Points: %+d | Total: $%d", 
                    turn.getPointsEarned(), turn.getScoreAfterPlay()));
                contentStream.endText();
                yPosition -= 25;
            }
            
            // Footer
            yPosition -= 10;
            contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("Generated: " + 
                java.time.LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            contentStream.endText();
            
            contentStream.close();
            document.save(fullFilename);
            System.out.println("PDF report generated: " + fullFilename);
            
        } catch (IOException e) {
            System.err.println("Error generating PDF report: " + e.getMessage());
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
    
    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}
