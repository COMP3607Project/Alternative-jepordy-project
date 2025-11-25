package com.project;
//import java.util.Scanner; 
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Game class implementing Singleton Pattern and Subject in Observer Pattern (SOLID).
 * Follows Single Responsibility Principle - manages game flow and notifies observers.
 * Follows Open/Closed Principle - can add new observers without modifying Game class.
 * Singleton ensures only one game instance exists.
 */
public class Game {
    // Singleton instance
    private static Game instance;
    
   // private List<Player> players = new ArrayList<>(); 
   // private GameLoader gameloader; 
    private Gameboard gameboard; 
   // private ReportGenerator reportGenerator;
    
    // Observer Pattern - List of observers
    private List<GameObserver> observers = new ArrayList<>();
    
    // Case ID for process mining (unique per game session)
    private String caseId; 

    /**
     * Private constructor for Singleton Pattern
     */
    private Game() {
        this.caseId = UUID.randomUUID().toString();
        this.observers = new ArrayList<>();
    }

    /**
     * Get the singleton instance of Game (Thread-safe lazy initialization)
     * @return The single Game instance
     */
    public static synchronized Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    /**
     * Register an observer
     */
    public void registerObserver(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove an observer
     * @param observer Observer to remove
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notify all observers of a game event (Observer Pattern)
     * @param event The game event to notify observers about
     */
    private void notifyObservers(GameEvent event) {
        for (GameObserver observer : observers) {
            observer.update(event);
        }
    }

    /**
     * Helper method to create and notify a game event
     */
    private void logEvent(String activity, String playerId, String category, 
                         String questionValue, String answerGiven, String result, String scoreAfterPlay) {
        GameEvent event = new GameEvent.Builder(caseId, activity)
            .playerId(playerId)
            .category(category)
            .questionValue(questionValue)
            .answerGiven(answerGiven)
            .result(result)
            .scoreAfterPlay(scoreAfterPlay)
            .build();
        notifyObservers(event);
    }

    /**
     * Overloaded helper for simple events without question details
     */
    private void logEvent(String activity) {
        GameEvent event = new GameEvent.Builder(caseId, activity).build();
        notifyObservers(event);
    }

    public void startGame(){
        logEvent("Start Game");
        
        System.out.println("These are the categories:"); 
        gameboard = new Gameboard();
 
         List<GameLoader> loaders = new ArrayList<>();
         loaders.add(new XMLGameLoader("sample_game_XML.xml")); 
         loaders.add(new CSVGameLoader("sample_game_CSV.csv"));
         loaders.add(new JSONGameLoader("sample_game_JSON.json"));
         List<Category> categories = loadFromMultipleSources(loaders);
       
        gameboard.loadCategories(categories); 
        gameboard.showBoard();
        }
      

           

          

    



    public void endGame(){
         //WILL ADD LATER
    }

    public void playTurn(){


   //     System.out.println("Please pick a category");
        //   String categoryName = scanner.nextLine().trim();

        //   System.out.print("Please pick a value: ");
        //   int value = Integer.parseInt(scanner.nextLine());

        //    Questions question = gameBoard.getQuestions(categoryName, value); 
        
        //    if(question == null){
        //     System.out.print("Question not found or already used");
        //     return; 
        //    }else{
        //      gameboard.flagQuestion();
            
        }    

        private List<Category> buildCategories(List<List<String>> raw){
            List<Category> categoryList = new ArrayList<>(); 
            Map<String, Category> categoryMap = new LinkedHashMap<>();

            for(List<String> record: raw){
                if(record == null || record.size()<8) continue; 

                String rawValue = record.get(1).trim(); 
                if(rawValue.equalsIgnoreCase("value")|| rawValue.equalsIgnoreCase("points")) continue;
                int value; 
                try {
                    value = Integer.parseInt(rawValue);
                } catch (NumberFormatException e) {
                    continue;
                }
                String categoryName = record.get(0); 
                String questionText = record.get(2);
               // int value = Integer.parseInt(record.get(1)); 
                String answer =  record.get(7); 
                List<Options> options = new ArrayList<>();
                    options.add(new Options( record.get(3)));
                    options.add(new Options( record.get(4)));
                    options.add(new Options(record.get(5))); 
                    options.add(new Options(record.get(6))); 
                

                if(!categoryMap.containsKey(categoryName)){
                    categoryMap.put(categoryName, new Category(categoryName)); 
                }
                Category category = categoryMap.get(categoryName);
                //NOTE: QUESTIONS TAKE THE ACTUAL QUESTION, THE OPTIONS AS AN ARRAYLIST, THE VALUE AND THE ANSWER 
                //BUT CURRENTLY NOT GETTING OPTIONS AS A LIST WITH THE  JSON lOADERS, IDK WHY 
                Questions q = new Questions(questionText, options, value, answer);
                category.addQuestions(q); 
            }
            categoryList.addAll(categoryMap.values()); 
            return categoryList;
        }

        private List<Category> mergeCategories(List<Category> categories){
            Map<String,Category> mergedMap = new LinkedHashMap<>(); 

            for(Category category: categories){
                String name = category.getName(); 
                if(!mergedMap.containsKey(name)){
                    mergedMap.put(name, new Category(name)); 
                }
                for(Questions q: category.getQuestions()){
                    mergedMap.get(name).addQuestions(q); 
                }
            }
            return new ArrayList<>(mergedMap.values());
        }
         private List<Category> loadFromMultipleSources(List<GameLoader> loaders){
            List<Category> allCategories = new ArrayList<>(); 

            for(GameLoader loader: loaders){
                List<List<String>> rawData =  loader.load(); 
                List<Category> categories = buildCategories(rawData); 
                allCategories.addAll(categories);
            }
            return mergeCategories(allCategories); 
         }
    }