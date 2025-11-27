package com.project;



//import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class CategoryTest {
    private Category category; 
    private Questions q100; 
    private Questions q200;

    @Before 
    public void Setup(){
        category = new Category("History");
        ArrayList<Options> opt1 = new ArrayList<>(); 
        opt1.add(new Options("A", "Answer A"));

        ArrayList<Options> opt2 = new ArrayList<>(); 
        opt2.add(new Options("B", "Answer B"));


        q100 = new Questions("Q1", opt1 , 100, "A");
        q200 = new Questions("Q2", opt2 , 200, "B");
    
    }

    @Test 
    public void testAddQuestionsSucessfully(){
        category.addQuestions(q100);
        assertEquals(1,category.getQuestions().size());
    
    }

    @Test 
    public void testAddUsedQuestionsFails(){
        q100.setUsed();
        category.addQuestions(q100); 
        assertTrue(category.getQuestions().isEmpty());

    }
    @Test
    public void tesFindQuestion(){
        category.addQuestions(q100); 
        category.addQuestions(q200); 
        Questions found = category.findQuestion(200);
        assertNotNull(found); 
        assertEquals(200, found.getValue()); 

    }

    @Test 
    public void testFindQuestionReturnsNullWhenUsed(){
       category.addQuestions(q100); 
       q100.setUsed(); 
       assertNull(category.findQuestion(100)); 
    }
   
    @Test
    public void testCategoryEqualsIgnoreCase(){
        assertTrue(category.equals("HISTORY")); 
        assertTrue(category.equals("history"));
        assertFalse(category.equals("Geography")); 

    } 

    @Test
    public void testCategoryEqualsWithNull(){
        assertFalse(category.equals(null)); 
    }
    
}
