package com.project;

public class GameLoaderFactory {

    public static GameLoader getGameLoader(String filePath) {
        String lower = filePath.toLowerCase();

        if (lower.endsWith(".csv")) {
            return new CSVGameLoader(filePath);
        }
        if (lower.endsWith(".json")) {
            return new JSONGameLoader(filePath);
        }
        if (lower.endsWith(".xml")) {
            return new XMLGameLoader(filePath);
        }

        throw new IllegalArgumentException("No strategy for file type: " + filePath);
    }
}
