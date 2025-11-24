package com.project;

public class GameLoaderFactory {
    public static GameLoader getGameLoader(String type, String filePath) {
        if (type.equalsIgnoreCase("CSV")) {
            return new CSVGameLoader(filePath);
        } else if (type.equalsIgnoreCase("JSON")) {
            return new JSONGameLoader(filePath);
        } else if (type.equalsIgnoreCase("XML")) {
            return new XMLGameLoader(filePath);
        }
        throw new IllegalArgumentException("Unknown game loader type: " + type);
    }
}
