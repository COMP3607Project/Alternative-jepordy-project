package com.project;
import java.util.ArrayList;
import java.util.List; 


public class Category {
    private final String name; 
    private final List<Questions> questions = new ArrayList<>(); 
  


    public Category(String name){
        this.name = name; 
    }
    public String getName(){
        return name; 
    }
     public void addQuestions(Questions q){
    if(!q.getUsed()){
        questions.add(q);
    }
}
    public List<Questions> getQuestions(){
      return questions; 
     }
    public Questions findQuestion(int value){
        for(Questions question: this.questions ){
                if(question.getValue() == value && !question.getUsed()){
                    return question;
       }
      
    }
     return null; 
}

    public boolean equals(String categoryName){
     if(categoryName == null || this.name == null)
        return false;
    return this.name.equalsIgnoreCase(categoryName);       

}
}

