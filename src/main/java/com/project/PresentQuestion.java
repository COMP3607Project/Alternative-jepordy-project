package com.project;

public class PresentQuestion implements GameState {
 
    private final PlayTurn turn; 
    private final Category category; 
    private final int value; 

    public PresentQuestion(PlayTurn turn, Category category, int value){
        this.turn = turn; 
        this.category = category; 
        this.value = value; 

    }
    
    @Override 
    public void handleGameLogic(){
        Questions question = category.findQuestion(value ); 

        if( question == null || question.getUsed()){
            System.out.println("Error: question already used or not found"); 
            return; 
        }

        turn.getBoard().flagQuestion(question); 

        System.out.println("You selected this question: \n" + question.getQuestions());

        turn.setState(new ChooseOption(turn, question)); 
    }
}
