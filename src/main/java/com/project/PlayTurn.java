
package com.project;

public class PlayTurn {
    private final Gameboard board; 
    private GameState state; 
    private final Player currentPlayer; 


    public PlayTurn(Gameboard board, Player currentPlayer){
        this.board = board; 
        this.currentPlayer = currentPlayer; 
        this.state = new ChooseCategory(this); 
    }

    public void setState(GameState newState){
        this.state=  newState; 

    }
    
    public void runTurn(){
        while(state != null){
            state.handleGameLogic();
        }
        
    }

    public Gameboard getBoard(){
        return board;
    }

    public Player getCurrentPlayer(){
        return currentPlayer; 
    }
    
}

