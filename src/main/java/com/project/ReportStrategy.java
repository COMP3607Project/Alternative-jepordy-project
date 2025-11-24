package com.project;

import java.util.List;

public interface ReportStrategy {
    void generateReport(List<String> reportLines, String caseId);
    void saveReport(String filename, List<String> reportLines, String caseId);
}
