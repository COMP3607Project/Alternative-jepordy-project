
package com.project;
import java.util.ArrayList;
import java.util.List; 

public class Gameboard {
    private List<Category> categories = new ArrayList<>();

    public void loadCategories(List<Category> categories){
        this.categories = categories; 
    }


    public void showBoard(){
        System.out.println("\n" + "=".repeat(80));
        System.out.println("JEOPARDY GAME BOARD");
        System.out.println("=".repeat(80) + "\n");
        
        for(Category c: categories){
            System.out.println(c.getName().toUpperCase());
            List<Questions> questions = c.getQuestions();
            
            for(Questions q: questions){
                if(!q.getUsed()){
                    System.out.print("  [$" + q.getValue() + "]");
                } else {
                    System.out.print("  [ANSWERED]");
                }
            }
            System.out.println("\n");
        }
        System.out.println("=".repeat(80) + "\n");
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

  
}