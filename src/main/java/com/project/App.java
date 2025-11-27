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

        Player p1 = new Player("Alice");   // id = 1
        Player p2 = new Player("Bob");     // id = 2
        Player p3 = new Player("Carol");   // id = 3
        Player p4 = new Player("Dave");    // id = 4
        System.out.println("Player 1: " + p1.getName() + ", ID: " + p1.getId());
        System.out.println("Player 2: " + p2.getName() + ", ID: " + p2.getId());
        System.out.println("Player 3: " + p3.getName() + ", ID: " + p3.getId());
        System.out.println("Player 4: " + p4.getName() + ", ID: " + p4.getId());

        // GameLoader csvLoader = new CSVGameLoader("sample_game_CSV.csv");
        // GameLoader jsonLoader = new JSONGameLoader("sample_game_JSON.json");
        // GameLoader xmlLoader = new XMLGameLoader("sample_game_XML.xml");

        //Tests for GameLoaders
        GameLoader cLoader = GameLoaderFactory.getGameLoader("sample_game_CSV.csv");cLoader.load();
        GameLoader jLoader = GameLoaderFactory.getGameLoader("sample_game_JSON.json");jLoader.load();
        GameLoader xLoader = GameLoaderFactory.getGameLoader("sample_game_XML.xml");xLoader.load();

        CSVGameLoader csvLoader = (CSVGameLoader) cLoader;
        List<Category> cCategories = csvLoader.getCategories();

        JSONGameLoader jsonLoader = (JSONGameLoader) jLoader;
        List<Category> jCategories = jsonLoader.getCategories();

        XMLGameLoader xmlLoader = (XMLGameLoader) xLoader;
        List<Category> xCategories = xmlLoader.getCategories();
// Print all categories
        for (Category c : cCategories) {
            System.out.println("Category: " + c.getName());
        }

        for (Category c : jCategories) {
            System.out.println("Category: " + c.getName());
        }

        for (Category c : xCategories) {
            System.out.println("Category: " + c.getName());
        }

        // System.out.println("=== CSV ===");
        // print(csvLoader.load());

        // System.out.println("\n\n=== JSON ===");
        // print(jsonLoader.load());

        // System.out.println("\n\n=== XML ===");
        // print(xmlLoader.load());

        // Get Game singleton instance (Subject)
        Game game = Game.getInstance();
        
        // Create and register Logger observer
  //      Logger logger = new Logger();
  //      game.registerObserver(logger);
        
        System.out.println("\n \n Welcome to jepordy! "); 
      //  Game game = new Game();  
        game.startGame();
        game.playTurn(); 
        //System.out.println("Thank you for playing! Goodbye.");

    }

    // private static void print(List<List<String>> records) {
    //     for (List<String> row : records) {
    //         System.out.println(row);
    //     }
    // }
}
