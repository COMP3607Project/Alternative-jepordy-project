package com.project;
public class Options {
    private final String name; 
    private final String letter;

    public Options(String letter, String name){
        this.letter = letter; 
        this.name =name;
   
    }
    public String getName(){
        return this.name; 
    }

    public String getLetter(){
        return this.letter; 
    }

}


