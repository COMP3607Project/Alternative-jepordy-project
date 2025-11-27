package com.project;
import java.util.Scanner;

public class ChooseCategory implements GameState{
    //  private final  Gameboard board; 
      private final PlayTurn turn; 


      public ChooseCategory(PlayTurn turn){
       //   this.board = turn.getBoard(); 
          this.turn = turn; 
      }

    @Override 
    public void handleGameLogic(){
        Scanner scanner = new Scanner(System.in); 
        System.out.println("Choose a category or type 'quit' to exit: "); 


        String input = scanner.nextLine().trim(); 
        if(input.equalsIgnoreCase("quit")){
            //turn.setState(null);
            turn.quitGame();
            return; 
        }
        Category selected = turn.getBoard().getCategory(input); 


        if(selected == null){
            System.out.println("Invalid category. Please Try again. "); 
            return; 
        }
        System.out.println("You selected: " + selected.getName()); 

        turn.setState(new ChooseValue(turn, selected)); 
    }

}
