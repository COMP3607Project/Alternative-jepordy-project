package com.project;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        GameLoader csvLoader = new CSVGameLoader("sample_game_CSV.csv");
        GameLoader jsonLoader = new JSONGameLoader("sample_game_JSON.json");
        GameLoader xmlLoader = new XMLGameLoader("sample_game_XML.xml");

    //     System.out.println("=== CSV ===");
    //     print(csvLoader.load());

    //     System.out.println("\n\n=== JSON ===");
    //     print(jsonLoader.load());

    //     System.out.println("\n\n=== XML ===");
    //     print(xmlLoader.load());

        System.out.println("\n \n Welcome to jepordy! ");  
        Game.getInstance().startGame();
    }

    private static void print(List<List<String>> records) {
        for (List<String> row : records) {
            System.out.println(row);
        }
    }
}
