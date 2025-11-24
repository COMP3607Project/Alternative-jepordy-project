package com.project;

import java.util.List;

public class HTMLReportStrategy implements ReportStrategy {

    @Override
    public void generateReport(List<String> reportLines, String caseId) {
        System.out.println("\n========== HTML REPORT ==========\n");
        String html = buildHTMLReport(reportLines, caseId);
        System.out.println(html);
        System.out.println("\n================================\n");
    }

    @Override
    public void saveReport(String filename, List<String> reportLines, String caseId) {
        try {
            String html = buildHTMLReport(reportLines, caseId);
            java.nio.file.Files.write(
                java.nio.file.Paths.get(filename),
                html.getBytes()
            );
            System.out.println("HTML report saved to: " + filename);
        } catch (java.io.IOException e) {
            System.out.println("Error saving HTML report: " + e.getMessage());
        }
    }

    private String buildHTMLReport(List<String> reportLines, String caseId) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <title>Jeopardy Game Report - ").append(caseId).append("</title>\n");
        html.append("  <style>\n");
        html.append("    body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }\n");
        html.append("    .container { background-color: white; padding: 20px; border-radius: 8px; max-width: 900px; margin: 0 auto; }\n");
        html.append("    h1 { color: #1e40af; border-bottom: 3px solid #1e40af; padding-bottom: 10px; }\n");
        html.append("    h2 { color: #1e3a8a; margin-top: 20px; }\n");
        html.append("    .case-id { background-color: #e0e7ff; padding: 10px; border-radius: 5px; margin: 10px 0; }\n");
        html.append("    .turn { background-color: #f9fafb; padding: 15px; margin: 10px 0; border-left: 4px solid #1e40af; }\n");
        html.append("    .correct { color: #16a34a; font-weight: bold; }\n");
        html.append("    .incorrect { color: #dc2626; font-weight: bold; }\n");
        html.append("    .scores { background-color: #fef3c7; padding: 15px; border-radius: 5px; margin: 15px 0; }\n");
        html.append("    table { width: 100%; border-collapse: collapse; margin: 10px 0; }\n");
        html.append("    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }\n");
        html.append("    th { background-color: #1e40af; color: white; }\n");
        html.append("  </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("  <div class=\"container\">\n");

        for (String line : reportLines) {
            if (line.contains("JEOPARDY")) {
                html.append("    <h1>").append(line).append("</h1>\n");
            } else if (line.contains("Case ID")) {
                html.append("    <div class=\"case-id\"><strong>").append(line).append("</strong></div>\n");
            } else if (line.contains("Players:")) {
                html.append("    <p><strong>").append(line).append("</strong></p>\n");
            } else if (line.contains("Gameplay Summary") || line.contains("Final Scores") || line.contains("Detailed Turn")) {
                html.append("    <h2>").append(line.replace("-", "").trim()).append("</h2>\n");
            } else if (line.contains("Turn ") && line.contains("selected")) {
                html.append("    <div class=\"turn\">\n");
                html.append("      <strong>").append(line).append("</strong>\n");
            } else if (line.contains("Question:")) {
                html.append("      <p><em>").append(line).append("</em></p>\n");
            } else if (line.contains("Answer:")) {
                if (line.contains("Correct")) {
                    html.append("      <p class=\"correct\">✓ ").append(line).append("</p>\n");
                } else if (line.contains("Incorrect")) {
                    html.append("      <p class=\"incorrect\">✗ ").append(line).append("</p>\n");
                } else {
                    html.append("      <p>").append(line).append("</p>\n");
                }
            } else if (line.contains("Score after turn")) {
                html.append("      <p><strong>").append(line).append("</strong></p>\n");
                html.append("    </div>\n");
            } else if (line.contains(":") && (line.contains("=") || line.matches(".*: \\d+.*"))) {
                html.append("    <p>").append(line).append("</p>\n");
            } else if (!line.trim().isEmpty() && !line.contains("=")) {
                html.append("    <p>").append(line).append("</p>\n");
            }
        }

        html.append("  </div>\n");
        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();
    }
}
