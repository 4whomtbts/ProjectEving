package com.example.evingPlanner;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void LocalDateTimeDiffCalculateTest() {
        LocalDateTime date = new LocalDateTime(2020, 1, 8, 0, 0,0, 0 );
        LocalDateTime date1 = new LocalDateTime(2020, 2, 5, 0, 0, 0, 0);
        int diff = Days.daysBetween(date, date1).getDays();
        assertEquals(diff, 28);
        LocalDateTime newDate = date.plusDays(diff);
        assertEquals(newDate.year().get(), 2020);
        assertEquals(newDate.monthOfYear().get(), 2);
        assertEquals(newDate.dayOfMonth().get(), 5);
    }
}