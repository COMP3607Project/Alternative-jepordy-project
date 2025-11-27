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
        Player current = turn.getCurrentPlayer();
        if(correct){
            System.out.println("Correct! +" + value + " points"); 
            current.updateScore(value); 
            
        }else{ 
             System.out.println("InCorrect -" + value + " points"); 
           
            current.updateScore(-value); }
        System.out.println(current.getName() + "'s new score  $" + current.getScore());
        // Implementation for updating the score
     turn.setState(new TurnEnded(turn)); 
    }
    
}