package com.project;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
public class GameboardTest {
    private Gameboard board; 
    private Category category; 
    private Questions q100; 

    @Before 
    public void setup(){
        board = new Gameboard(); 

        ArrayList<Options> opts = new ArrayList<>(); 
        opts.add(new Options("A", "History")); 
        q100 = new Questions("Q1", opts, 100, "A"); 

        category = new Category("History"); 
        category.addQuestions(q100);
        board.addCategory(category); 

       
    }
   @Test 
   public void testAddCategory(){
    assertEquals(1,board.getCategories().size());


   }
   @Test 
   public void testGetQuestionsValidValue(){
    Questions result = board.getQuestions("History", 100); 
    assertNotNull(result);
    assertEquals(100, result.getValue());
   }
   @Test
   public void testGteQuestionsInvalidcategory(){
    assertNull(board.getQuestions("Science", 100)); 
   } 
   @Test 
   public void testFlagQuestion(){
    board.flagQuestion(q100); 
    assertTrue(q100.getUsed()); 
   }


}


