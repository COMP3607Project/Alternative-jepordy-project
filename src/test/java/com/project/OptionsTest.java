package com.project;


import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OptionsTest {
    @Test 
    public void testOptionAnswer(){
        Options opt = new Options("Example"); 
        assertEquals("Example", opt.getAnswer()); 
    }
}
