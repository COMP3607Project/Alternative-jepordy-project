package com.project;

public class AcceptAnswer implements GameState {
    private final PlayTurn turn;
    private final Category category;
    private final Questions question; 
    private final String chosenOption; 

    public AcceptAnswer(PlayTurn turn, Category category, Questions question, String chosenOption){
        this.turn = turn;
        this.category = category;
        this.question = question; 
        this.chosenOption = chosenOption; 
    }
    @Override
    public void handleGameLogic() {
        boolean correct = question.getAnswer().equalsIgnoreCase(chosenOption); 
        System.out.println("The correct answer was: " + question.getAnswer()); 

        turn.setState(new UpdateScore(turn, category, question, chosenOption, correct)); 
        // Implementation for accepting an answer
       // System.out.println("Answer accepted.");
    }
    
}

