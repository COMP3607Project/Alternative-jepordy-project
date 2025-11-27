package com.project;


public class UpdateScore implements GameState {
    private final PlayTurn turn;
    private final Category category;
    private final Questions question; 
    private final String chosenAnswer;
    private final boolean correct; 

    public UpdateScore(PlayTurn turn, Category category, Questions question, String chosenAnswer, boolean correct){
        this.turn = turn;
        this.category = category;
        this.question = question; 
        this.chosenAnswer = chosenAnswer;
        this.correct = correct; 
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
           
            current.updateScore(-value);
        }
        System.out.println(current.getName() + "'s new score  $" + current.getScore());
        
        // Record turn in Game singleton for logging and reporting
        Turn turnRecord = new Turn(
            current.getId(),
            category.getName(),
            value,
            question.getQuestions(),
            chosenAnswer,
            correct,
            correct ? value : -value,
            current.getScore(),
            null
        );
        Game.getInstance().recordTurn(turnRecord);
        
        turn.setState(new TurnEnded(turn)); 
    }
    
}