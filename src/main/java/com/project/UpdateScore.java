package com.project;

public class UpdateScore implements GameState {
    private final PlayTurn turn; 
    private final Questions question; 
    private final boolean correct; 

    public UpdateScore(PlayTurn turn, Questions question, boolean correct){
        this.turn = turn;
        this.question = question; 
        this.correct =  correct; 
    }
    @Override
    public void handleGameLogic() {
        int value = question.getValue(); 

        if(correct){
            System.out.println("Correct! +" + value + " points"); 
            turn.getCurrentPlayer().updateScore(value); 
            
        }else 
             System.out.println("InCorrect -" + value + " points"); 
            turn.getCurrentPlayer().updateScore(-value); 
        // Implementation for updating the score
     turn.setState(new ChooseCategory(turn)); 
    }
    
}
