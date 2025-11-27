package com.project;
import java.util.List; 

public class Questions {
    private final String question; 
    private final String answer; 
    private final List<Options> options;
    private final int value; 
    private boolean used;
    
    public Questions(String question, List<Options> options, int value, String answer){
        this.question = question; 
        this.options = options;
        this.value =value; 
        this.answer = answer; 
    }
    
    public void setUsed(){
        this.used = true; 
    }
    public String getQuestions(){
        return question; 
    }
    
    public List<Options> getOptions(){
        return options;
    }
    public boolean getUsed(){
        return used;
    }
    public String getAnswer(){
        return answer; 

    }
    public int getValue(){
        return value; 
    }
    
}
