package com.project;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Logger class implementing Observer Pattern and Single Responsibility Principle (SOLID).
 * Responsible for logging game events to CSV file for process mining.
 */
public class Logger implements GameObserver {
    private static final String LOG_FILE = "game_event_log.csv";
    private static final String HEADER = "Case_ID,Player_ID,Activity,Timestamp,Category,Question_Value,Answer_Given,Result,Score_After_Play";
    
    public Logger() {
        initializeLogFile();
    }

    /**
     * Initialize the CSV log file with headers if it doesn't exist
     */
    private void initializeLogFile() {
        try {
            if (!Files.exists(Paths.get(LOG_FILE))) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE))) {
                    writer.println(HEADER);
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing log file: " + e.getMessage());
        }
    }

    /**
     * Observer pattern update method - called when game events occur
     * Follows Dependency Inversion Principle by depending on GameEvent abstraction
     */
    @Override
    public void update(GameEvent event) {
        logEvent(event);
    }

    /**
     * Logs event to CSV file in process mining format
     * Follows Single Responsibility Principle - only handles logging
     */
    private void logEvent(GameEvent event) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            String logEntry = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                escapeCsv(event.getCaseId()),
                escapeCsv(event.getPlayerId()),
                escapeCsv(event.getActivity()),
                event.getFormattedTimestamp(),
                escapeCsv(event.getCategory()),
                escapeCsv(event.getQuestionValue()),
                escapeCsv(event.getAnswerGiven()),
                escapeCsv(event.getResult()),
                escapeCsv(event.getScoreAfterPlay())
            );
            writer.println(logEntry);
        } catch (IOException e) {
            System.err.println("Error logging event: " + e.getMessage());
        }
    }

    /**
     * Escapes CSV special characters
     */
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
