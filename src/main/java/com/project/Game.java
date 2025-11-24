package com.project;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Game {
    private static Game instance;
    private List<Player> players = new ArrayList<>(); 
    private Logger logger; 
    private Gameboard gameboard; 
    private ReportGenerator reportGenerator;
    private List<Observer> observers = new ArrayList<>();

    private Game() {
        // Private constructor to prevent instantiation
        this.logger = Logger.getInstance();
        this.reportGenerator = ReportGenerator.getInstance();
        registerObserver(logger);
        registerObserver(reportGenerator);
    }

    public static synchronized Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void startGame(){
        players = new ArrayList<>();
        logger.logGameStart(0); // Will update after getting number of players
        
        System.out.println("How many players are playing?");
        Scanner scanner = new Scanner(System.in);   
        int numPlayers = Integer.parseInt(scanner.nextLine());
        logger.logEvent("Number of players selected: " + numPlayers);
        
        for(int i=1; i<=numPlayers; i++){
            System.out.println("Enter name for player " + i + ": ");
            String playerName = scanner.nextLine().trim(); 
            this.players.add(new Player(playerName)); 
            logger.logEvent("Player added: " + playerName);
        }
        
        displayPlayers();
        System.out.println("These are the categories:"); 
        gameboard = new Gameboard();
 
        List<GameLoader> loaders = new ArrayList<>();
        loaders.add(new XMLGameLoader("sample_game_XML.xml")); 
        loaders.add(new CSVGameLoader("sample_game_CSV.csv"));
        loaders.add(new JSONGameLoader("sample_game_JSON.json"));
        List<Category> categories = loadFromMultipleSources(loaders);
       
        gameboard.loadCategories(categories); 
        logger.logEvent("Game board loaded with " + categories.size() + " categories");
        
        reportGenerator.recordGameStart(players);
        
        gameboard.showBoard();
        notifyObservers();
    }
      

           

          

    



    public void endGame(){
        logger.logGameEnd();
        reportGenerator.recordGameEnd(players);
        notifyObservers();
        displayGameSummary();
    }

    private void displayGameSummary() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("GAME SUMMARY");
        System.out.println("=".repeat(80));
        
        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
        
        int rank = 1;
        for (Player p : sortedPlayers) {
            System.out.println(rank + ". " + p.getName() + " - $" + p.getScore());
            rank++;
        }
        System.out.println("=".repeat(80) + "\n");
        
        logger.printAllLogs();
        reportGenerator.generateReport();
    }

    public void playTurn(String playerName, String categoryName, int value, String playerAnswer){
        logger.logPlayerAction(playerName, "Selected " + categoryName + " for $" + value);
        
        Questions question = gameboard.getQuestions(categoryName, value);
        if (question == null) {
            logger.logEvent("ERROR: Question not found or already used");
            System.out.println("Question not found or already used");
            return; 
        }
        
        logger.logQuestionAsked(categoryName, value);
        
        boolean correct = question.getAnswer().equalsIgnoreCase(playerAnswer);
        logger.logAnswer(playerName, playerAnswer, correct);
        
        int pointsEarned = 0;
        if (correct) {
            pointsEarned = value;
            updatePlayerScore(playerName, pointsEarned);
        }
        
        gameboard.flagQuestion(question);
        
        reportGenerator.recordTurn(playerName, categoryName, value, 
                                  question.getQuestions(), playerAnswer, 
                                  correct, pointsEarned, players);
        
        notifyObservers();
    }

    private void updatePlayerScore(String playerName, int points) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(playerName)) {
                p.addScore(points);
                logger.logScoreUpdate(playerName, p.getScore());
                break;
            }
        }
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

        public void displayPlayers(){
            System.out.println("Current Players: ");
            for(Player p: players){
                System.out.println(p.getName() + " - $" + p.getScore());
            }
        }

        public List<Player> getPlayers() {
            return players;
        }

        public Gameboard getGameboard() {
            return gameboard;
        }

        public Logger getLogger() {
            return logger;
        }

        public ReportGenerator getReportGenerator() {
            return reportGenerator;
        }
    }