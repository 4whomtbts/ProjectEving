package com.example.evingPlanner.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DayTest {

    @Test
    public void TestgetFields() {
        Day day = new Day(19961012);
        assertEquals(1996, day.getYear());
        assertEquals(10, day.getMonth());
        assertEquals(12, day.getDay());

        day = new Day(20000101);
        assertEquals(2000, day.getYear());
        assertEquals(1, day.getMonth());
        assertEquals(1, day.getDay());

        day = new Day(20201231);
        assertEquals(2020, day.getYear());
        assertEquals(12, day.getMonth());
        assertEquals(31, day.getDay());

        day = new Day(2020, 1, 1);
        assertEquals(2020, day.getYear());
        assertEquals(1, day.getMonth());
        assertEquals(1, day.getDay());

    }
}
