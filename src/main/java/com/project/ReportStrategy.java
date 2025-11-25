package com.project;

import java.util.List;

/**
 * Strategy Pattern interface for report generation (SOLID).
 * Follows Interface Segregation Principle - single focused method.
 * Allows different report formats without modifying ReportGenerator.
 */
public interface ReportStrategy {
    /**
     * Generate a report with the given data
     * @param players List of players with final scores
     * @param turns List of all turns in the game
     * @param filename Base filename (without extension)
     */
    void generateReport(List<Player> players, List<Turn> turns, String filename);
}
