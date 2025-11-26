package com.project;
//import java.util.Scanner; 
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Game {
   // private List<Player> players = new ArrayList<>(); 
   // private GameLoader gameloader; 
   // private Logger logger; 
    private Gameboard gameboard; 
   // private ReportGenerator reportGenerator; 

    public void startGame(){
        System.out.println("These are the categories:"); 
        gameboard = new Gameboard();
 
         List<GameLoader> loaders = new ArrayList<>();
         loaders.add(new XMLGameLoader("sample_game_XML.xml")); 
         loaders.add(new CSVGameLoader("sample_game_CSV.csv"));
         loaders.add(new JSONGameLoader("sample_game_JSON.json"));
         List<Category> categories = loadFromMultipleSources(loaders);
       
        gameboard.loadCategories(categories); 
        gameboard.showBoard();
       // System.out.println(gameboard.getCategories());
        }
      

           

          

    



    public void endGame(){
         //WILL ADD LATER
    }

    public void playTurn(){


        Player player = new Player("Alice"); 

        PlayTurn turn = new PlayTurn(gameboard, player);

        turn.runTurn();



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

        // private List<Category> buildCategories(List<List<String>> raw){
        //     List<Category> categoryList = new ArrayList<>(); 
        //     Map<String, Category> categoryMap = new LinkedHashMap<>();

        //     for(List<String> record: raw){
        //         if(record == null || record.size()<4) continue; 

        //         String rawValue = record.get(1).trim(); 
        //         if(rawValue.equalsIgnoreCase("value")|| rawValue.equalsIgnoreCase("points")) continue;
        //         int value; 
        //         try {
        //             value = Integer.parseInt(rawValue);
        //         } catch (NumberFormatException e) {
        //             continue;
        //         }
        //         String categoryName = record.get(0).trim(); 
        //         String questionText = record.get(2).trim();
        //        // int value = Integer.parseInt(record.get(1)); 
        //         String answer =  record.size()> 4 ? record.get(4).trim(): "";
                
        //         String rawOptions = record.get(3).trim();

        //         // List<Options> options = new ArrayList<>();
        //         //     options.add(new Options("A", record.get(3)));
        //         //     options.add(new Options( "B", record.get(4)));
        //         //     options.add(new Options("C", record.get(5))); 
        //         //     options.add(new Options("D", record.get(6))); 
                
        //        List<Options> options = parseOptions(rawOptions); 
        //         if(!categoryMap.containsKey(categoryName)){
        //             categoryMap.put(categoryName, new Category(categoryName)); 
        //         }
        //         Category category = categoryMap.get(categoryName);
        //         //NOTE: QUESTIONS TAKE THE ACTUAL QUESTION, THE OPTIONS AS AN ARRAYLIST, THE VALUE AND THE ANSWER 
        //         //BUT CURRENTLY NOT GETTING OPTIONS AS A LIST WITH THE  JSON lOADERS, IDK WHY 
        //         Questions q = new Questions(questionText, options, value, answer);
        //         category.addQuestions(q); 
        //     }
        //     categoryList.addAll(categoryMap.values()); 
        //     return categoryList;
        // }

        // private List<Options> parseOptions(String rawOptions){
        //     List<Options> options = new ArrayList<>();
        //     if(rawOptions == null || rawOptions.isEmpty())
        //         return options; 
        //     String[] parts = rawOptions.split(",");

        //     for(int i= 0; i<parts.length; i++){
        //         String part = parts[i].trim(); 
        //         String letter = "";
        //         String name =  part;

        //         if(part.length() > 2 && Character.isLetter(part.charAt(0)) && part.charAt(1) == '.'){
        //             letter = String.valueOf(part.charAt(0)); 
        //             name = part.substring(2).trim();
        //         }else{
        //             letter = String.valueOf((char) ('A' + i)); 
        //         }
        //         options.add(new Options(letter, name));
        //     }
        //     return options; 
        // }

        private List<Category> buildCategories(List<List<String>> raw){
            List<Category> categoryList = new ArrayList<>(); 
            Map<String, Category> categoryMap = new LinkedHashMap<>();

            for(List<String> record: raw){
                if(record == null || record.size() < 4)
                    continue; 

                String categoryName = record.get(0).trim();
                String rawValue = record.get(1).trim(); 

                if(rawValue.equalsIgnoreCase("value") || rawValue.equalsIgnoreCase("points"))
                    continue; 
                int value; 
                try {
                    value = Integer.parseInt(rawValue);
                } catch (NumberFormatException e) {
                    continue; 
                }
                String question = record.get(2).trim();
                String rawOptions = record.get(3).trim();
                String answer = record.get(record.size() -1).trim().toUpperCase(); 
                List<Options> options = parseOptions(rawOptions, record); 
                
             

               

                categoryMap.putIfAbsent(categoryName, new Category(categoryName)); 
                Category category = categoryMap.get(categoryName);

                Questions q = new Questions(question, options, value, answer); 
                category.addQuestions(q); 
            }
            categoryList.addAll(categoryMap.values()); 
            return categoryList; 
        }
        private List<Options> parseOptions(String rawOptions, List<String> record){
            List<Options> options = new ArrayList<>(); 

            if(record != null && record.size() > 5){
               
                    for(int i = 3; i < record.size() -1 ; i ++){
                       // String letter = record.get(i).trim(); 
                        String name = record.get(i).trim();
                        if(!name.isEmpty()){ 
                           // if(letter.isEmpty() ){
                            String letter = String.valueOf((char) ('A' + (i-3)));
                          //  }
                        options.add(new Options(letter, name));
                         }
                 }
                //   if(!options.isEmpty())
                //       return options; 
                }
            
            
            if(options.isEmpty() && rawOptions != null && !rawOptions.isEmpty()){
                String[] parts =  rawOptions.split("\\s*,\\s*|\\s*\\|\\s*|\\s*;\\s*"); 

                for(int i = 0; i <parts.length; i++){
                    String part = parts[i].trim(); 
                    if(part.isEmpty())
                        continue;
                    String letter = null; 
                    String text = part; 

                     if(part.length() >= 2 && Character.isLetter(part.charAt(0))){
                  //  if(part.matches("^[A-Za-z]\\..*")){
                        char second = part.charAt(1);
                        if(second == '.' || second == ')' || second ==  ':'){
                           letter = String.valueOf(part.charAt(0));
                           text = part.substring(2).trim();
                        }
                    }  
                     if(letter == null || letter.isEmpty()){
                        letter = String.valueOf((char)('A' + i)); 
                       // text = part; 
                    }
                    options.add(new Options(letter,text)); 
                }
             //   if(!options.isEmpty())
               
            }
            return options; 
           
        // if((rawOptions == null || rawOptions.isEmpty()) && record.size() > 3) {
        //     int start = 3; 
        //     for(int i = 0; i < record.size() -start; i++){
        //         String name = record.get(start + i).trim(); 
        //         if(name.isEmpty())
        //             continue; 
        //         String letter = String.valueOf((char) ('A' + i)); 
        //         options.add(new Options(letter, name));
        //     }
        //     if(!options.isEmpty())
        //         return options;
    
    //    if(rawOptions !=null && !rawOptions.isEmpty()){
    //     options.add(new Options("A", rawOptions.trim()));
    //    }
    //    return options; 
    }

        private List<Category> mergeCategories(List<Category> categories){
            Map<String,Category> mergedMap = new LinkedHashMap<>(); 

            for(Category category: categories){
                String name = category.getName(); 
                mergedMap.putIfAbsent(name, new Category(name)); 
                Category mergedCategory =  mergedMap.get(name); 

                // if(!mergedMap.containsKey(name)){
                //     mergedMap.put(name, new Category(name)); 
                // }
                for(Questions q: category.getQuestions()){
                    boolean exists  = mergedCategory.getQuestions().stream()
                    .anyMatch(existing -> existing.getQuestions().equals(q.getQuestions()) 
                    && existing.getValue() == q.getValue());
                    if(!exists){
                    mergedMap.get(name).addQuestions(q);
                    } 
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