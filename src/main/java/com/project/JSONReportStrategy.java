package com.project;

import java.util.List;

public class JSONReportStrategy implements ReportStrategy {

    @Override
    public void generateReport(List<String> reportLines, String caseId) {
        System.out.println("\n========== JSON REPORT ==========\n");
        String json = buildJSONReport(reportLines, caseId);
        System.out.println(json);
        System.out.println("\n================================\n");
    }

    @Override
    public void saveReport(String filename, List<String> reportLines, String caseId) {
        try {
            String json = buildJSONReport(reportLines, caseId);
            java.nio.file.Files.write(
                java.nio.file.Paths.get(filename),
                json.getBytes()
            );
            System.out.println("JSON report saved to: " + filename);
        } catch (java.io.IOException e) {
            System.out.println("Error saving JSON report: " + e.getMessage());
        }
    }

    private String buildJSONReport(List<String> reportLines, String caseId) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"caseId\": \"").append(caseId).append("\",\n");
        json.append("  \"reportType\": \"Jeopardy Game Report\",\n");
        json.append("  \"content\": [\n");

        for (int i = 0; i < reportLines.size(); i++) {
            String line = reportLines.get(i);
            if (!line.trim().isEmpty() && !line.contains("=")) {
                json.append("    {\n");
                json.append("      \"line\": ").append(i + 1).append(",\n");
                json.append("      \"text\": \"").append(escapeJSON(line)).append("\"\n");
                json.append("    }");
                if (i < reportLines.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
        }

        json.append("  ]\n");
        json.append("}\n");

        return json.toString();
    }

    private String escapeJSON(String text) {
        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
}
