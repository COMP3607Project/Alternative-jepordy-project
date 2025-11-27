package com.project;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class QuestionTest {
    @Test
    public void testQuestionCreation(){
        ArrayList<Options> opts = new ArrayList<>();
        opts.add(new Options("A", "Answer A"));
        opts.add(new Options("B", "Answer B"));

        Questions q =  new Questions("What is Java?", opts, 300, "A");
        assertEquals(2, q.getOptions().size()); 
        assertEquals(300, q.getValue()); 
        assertEquals(300, q.getValue()); 
        assertEquals("A", q.getAnswer());
        assertFalse(q.getUsed());

    }

    @Test
    public void testSetUsed(){
        ArrayList<Options> opts = new ArrayList<>();
        Questions q = new Questions("Q", opts, 100, "A");

        
        q.setUsed();
        assertTrue(q.getUsed());  
    }
}
