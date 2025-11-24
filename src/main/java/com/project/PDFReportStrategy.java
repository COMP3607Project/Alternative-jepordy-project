package com.project;

import java.util.List;

public class PDFReportStrategy implements ReportStrategy {

    @Override
    public void generateReport(List<String> reportLines, String caseId) {
        System.out.println("\n========== PDF REPORT ==========\n");
        System.out.println("PDF Report Preview (use saveReport to generate actual PDF):");
        System.out.println("Note: Full PDF generation requires Apache PDFBox library\n");
        for (String line : reportLines) {
            System.out.println(line);
        }
        System.out.println("\n===============================\n");
    }

    @Override
    public void saveReport(String filename, List<String> reportLines, String caseId) {
        // This is a basic implementation. For production, use Apache PDFBox library
        // For now, we'll save as a formatted text file that looks like PDF content
        try {
            StringBuilder pdfContent = new StringBuilder();
            pdfContent.append("%PDF-1.4\n");
            pdfContent.append("1 0 obj\n");
            pdfContent.append("<< /Type /Catalog /Pages 2 0 R >>\n");
            pdfContent.append("endobj\n");
            pdfContent.append("2 0 obj\n");
            pdfContent.append("<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n");
            pdfContent.append("endobj\n");
            pdfContent.append("3 0 obj\n");
            pdfContent.append("<< /Type /Page /Parent 2 0 R /Resources << >> /MediaBox [0 0 612 792] /Contents 4 0 R >>\n");
            pdfContent.append("endobj\n");
            pdfContent.append("4 0 obj\n");
            pdfContent.append("<< >>\n");
            pdfContent.append("stream\n");

            // Add report content as text
            pdfContent.append("BT\n");
            pdfContent.append("/F1 12 Tf\n");
            pdfContent.append("50 750 Td\n");

            int lineCount = 0;
            for (String line : reportLines) {
                if (lineCount > 0 && lineCount % 30 == 0) {
                    // New page
                    pdfContent.append("ET\n");
                    pdfContent.append("showpage\n");
                    pdfContent.append("BT\n");
                    pdfContent.append("/F1 12 Tf\n");
                    pdfContent.append("50 750 Td\n");
                }
                pdfContent.append("(").append(escapePDF(line)).append(") Tj\n");
                pdfContent.append("0 -15 Td\n");
                lineCount++;
            }

            pdfContent.append("ET\n");
            pdfContent.append("endstream\n");
            pdfContent.append("endobj\n");
            pdfContent.append("xref\n");
            pdfContent.append("0 5\n");
            pdfContent.append("0000000000 65535 f\n");
            pdfContent.append("0000000009 00000 n\n");
            pdfContent.append("0000000058 00000 n\n");
            pdfContent.append("0000000115 00000 n\n");
            pdfContent.append("0000000214 00000 n\n");
            pdfContent.append("trailer\n");
            pdfContent.append("<< /Size 5 /Root 1 0 R >>\n");
            pdfContent.append("startxref\n");
            pdfContent.append("450\n");
            pdfContent.append("%%EOF\n");

            java.nio.file.Files.write(
                java.nio.file.Paths.get(filename),
                pdfContent.toString().getBytes()
            );
            System.out.println("PDF report saved to: " + filename);
            System.out.println("Note: For better PDF generation, consider using Apache PDFBox or iText library");
        } catch (java.io.IOException e) {
            System.out.println("Error saving PDF report: " + e.getMessage());
        }
    }

    private String escapePDF(String text) {
        return text
            .replace("\\", "\\\\")
            .replace("(", "\\(")
            .replace(")", "\\)");
    }
}
