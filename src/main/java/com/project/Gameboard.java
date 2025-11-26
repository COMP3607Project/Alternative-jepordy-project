
package com.project;
import java.util.ArrayList;
import java.util.List; 

public class Gameboard {
    private List<Category> categories = new ArrayList<>();

    public void loadCategories(List<Category> categories){
        this.categories = categories; 
    }


    public void showBoard(){
        for(Category c: categories){
                  System.out.println(c.getName () + " : ");
            for(Questions question: c.getQuestions()){
                if(!question.getUsed()){
                    System.out.println("    - $" + question.getValue()); 
                }
            System.out.println(); 
        }
        }
    }
    
 
    
    public Questions getQuestions(String categoryName,int value) {
       for(Category c: categories){
            if(c.equals(categoryName)){
       
             return c.findQuestion(value); 
       }
       
    }
    return null; 
}

  
  public void flagQuestion(Questions question){
    question.setUsed();
  }

   public void addCategory(Category c){
         categories.add(c);
    }

    public List<Category> getCategories(){
        return categories;
    }
   
    public Category getCategory(String name){
        for(Category c: categories){
            if(c.getName().equalsIgnoreCase(name)){
              return c;
            }
          
        }
    
         return null; 
        }
}