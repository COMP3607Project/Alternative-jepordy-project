package com.project;
public class Options {
    private final String name; 
    private boolean isAnswer; //Marks that this option is the answer

    public Options(String name){

        this.name =name;
    }
    public String getName(){
        return this.name; 
    }

    public void setAnswer(boolean answer){
        this.isAnswer = answer; 
    }

    public String getAnswer(){
        if(isAnswer){
            getName();
        }
        return null; 
    }
}


