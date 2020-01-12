package com.example.evingPlanner;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlanCycleEditorDialogTest{

    @Test
    public void testInput() {
        String valid1 = "1";
        String valid2 = "1/2/3";
        String invalid1 = "//1/2/3/4";
        String invalid2 = "1//2/3///4/";
        String invalid3 = "1//2.3//.3./";
        String invalid4 = "hello world!";
        String invalid5 = "I'm /3/4/5 incorrect";
        String correctPattern = "(-?\\d+/?){1,}";

        assertEquals(true, valid1.matches(correctPattern));
        assertEquals(true, valid2.matches(correctPattern));
        assertEquals(false, invalid1.matches(correctPattern));
        assertEquals(false, invalid2.matches(correctPattern));
        assertEquals(false, invalid3.matches(correctPattern));
        assertEquals(false, invalid4.matches(correctPattern));
        assertEquals(false, invalid5.matches(correctPattern));

    }
}
