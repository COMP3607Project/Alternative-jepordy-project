package com.project;
public class TurnEnded implements GameState {
    private final PlayTurn turn; 

    public TurnEnded(PlayTurn turn){
        this.turn = turn; 
    }
    @Override 
    public void handleGameLogic(){
        System.out.println("Ending Turn");
        turn.setState(null);

    }
}
