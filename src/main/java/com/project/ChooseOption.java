package com.project;
import java.util.List; 
import java.util.Scanner; 

public class ChooseOption implements GameState {
    private final PlayTurn turn; 
    private final Category category;
    private final Questions question; 

    public ChooseOption(PlayTurn turn, Category category, Questions question){
        this.turn = turn;
        this.category = category;
        this.question = question;
    }

    @Override 
    public void handleGameLogic(){
        Scanner scanner = new Scanner(System.in); 

        System.out.println("Choose an option: "); 

        List<Options> options = question.getOptions(); 

        for(int i = 0; i<options.size(); i++){
            System.out.println("Letter: " + options.get(i).getLetter() +  ".  Options Name: " + options.get(i).getName() + "\n" );
        }
        System.out.println("Please enter your choice: (A-D)  ");
        String input = scanner.nextLine().trim(); 

        Options chosenOption = null; 

        for(Options opt: options){
            if(opt.getLetter().equalsIgnoreCase(input)){
                chosenOption = opt; 
                break;
            }
        }

        if(chosenOption == null){
            System.out.println("Invalid option. Try again.");
            return;
        }

        turn.setState(new AcceptAnswer(turn, category, question, chosenOption.getLetter()));
    
    }
}