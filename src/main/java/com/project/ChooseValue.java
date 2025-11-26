package com.project;
import java.util.Scanner; 

public class ChooseValue implements GameState {
    private final PlayTurn turn;
    private final Category category; 

    public ChooseValue(PlayTurn turn, Category category){
        this.turn =turn; 
        this.category = category; 
    }

    @Override 
    public void handleGameLogic(){
        Scanner scanner = new Scanner(System.in); 
        System.out.println("Available Value: "); 
        
        for(Questions q: category.getQuestions()){
            if(!q.getUsed()){
                System.out.println("- $" + q.getValue()); 

            }


        }
        System.out.println("Choose a value: "); 
        String input = scanner.nextLine().trim(); 

        int value; 
        try {
            value =Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number. "); 
            return; 
        }

        Questions q = category.findQuestion(value); 

        if(q == null || q.getUsed()){
            System.out.println("Invalid or already used question value. "); 
            return; 
        }


        turn.setState(new PresentQuestion(turn, category, value)); 

    }
}
