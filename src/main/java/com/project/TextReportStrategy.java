package com.project;

import java.util.List;

public class TextReportStrategy implements ReportStrategy {

    @Override
    public void generateReport(List<String> reportLines, String caseId) {
        System.out.println("\n========== TEXT REPORT ==========\n");
        for (String line : reportLines) {
            System.out.println(line);
        }
        System.out.println("\n================================\n");
    }

    @Override
    public void saveReport(String filename, List<String> reportLines, String caseId) {
        try {
            StringBuilder content = new StringBuilder();
            for (String line : reportLines) {
                content.append(line).append("\n");
            }
            java.nio.file.Files.write(
                java.nio.file.Paths.get(filename),
                content.toString().getBytes()
            );
            System.out.println("Text report saved to: " + filename);
        } catch (java.io.IOException e) {
            System.out.println("Error saving text report: " + e.getMessage());
        }
    }
}
