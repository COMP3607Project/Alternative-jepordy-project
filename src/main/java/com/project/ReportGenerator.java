package com.project;

import java.util.List;

/**
 * ReportGenerator using Strategy Pattern (SOLID).
 * Follows Open/Closed Principle - can add new report formats without modifying this class.
 * Follows Dependency Inversion Principle - depends on ReportStrategy abstraction.
 * Follows Single Responsibility Principle - only coordinates report generation.
 */
public class ReportGenerator {
    private ReportStrategy strategy;
    
    /**
     * Constructor accepting a report strategy
     * @param strategy The report generation strategy to use
     */
    public ReportGenerator(ReportStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Set a different report strategy at runtime (Strategy Pattern)
     * @param strategy The new report strategy
     */
    public void setStrategy(ReportStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * Generate report using the current strategy
     * @param players List of players with final scores
     * @param turns List of all game turns
     * @param filename Base filename for the report
     */
    public void generateReport(List<Player> players, List<Turn> turns, String filename) {
        if (strategy == null) {
            System.err.println("No report strategy set. Please set a strategy first.");
            return;
        }
        
        if (players == null || players.isEmpty()) {
            System.err.println("No players data available for report generation.");
            return;
        }
        
        if (turns == null || turns.isEmpty()) {
            System.err.println("No turns data available for report generation.");
            return;
        }
        
        strategy.generateReport(players, turns, filename);
    }
}
