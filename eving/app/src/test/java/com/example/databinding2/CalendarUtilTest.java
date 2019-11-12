package com.example.databinding2;

import com.example.databinding2.util.CalendarUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CalendarUtilTest {

    @Test
    public void getStartOfNextMonthTest() {
        assertEquals(37, CalendarUtil.getStartIndexOfNextMonth(2019,9));
        assertEquals(33, CalendarUtil.getStartIndexOfNextMonth(2019,10));
        assertEquals(35, CalendarUtil.getStartIndexOfNextMonth(2019,11));

    }
    @Test
    public void getLastDayOfLastMonthTest() {

        assertEquals(31, CalendarUtil.getLastDayOfLastMonth(2019,1));
        assertEquals(31, CalendarUtil.getLastDayOfLastMonth(2019,2));
        assertEquals(28, CalendarUtil.getLastDayOfLastMonth(2019,3));
        assertEquals(31, CalendarUtil.getLastDayOfLastMonth(2019,4));
        assertEquals(30, CalendarUtil.getLastDayOfLastMonth(2019,5));
        assertEquals(31, CalendarUtil.getLastDayOfLastMonth(2019,6));
        assertEquals(30, CalendarUtil.getLastDayOfLastMonth(2019,7));
        assertEquals(31, CalendarUtil.getLastDayOfLastMonth(2019,8));
        assertEquals(31, CalendarUtil.getLastDayOfLastMonth(2019,9));
        assertEquals(30, CalendarUtil.getLastDayOfLastMonth(2019,10));
        assertEquals(31, CalendarUtil.getLastDayOfLastMonth(2019,11));
        assertEquals(30, CalendarUtil.getLastDayOfLastMonth(2019,12));
        assertEquals(31, CalendarUtil.getLastDayOfLastMonth(2016,2));
        assertEquals(29, CalendarUtil.getLastDayOfLastMonth(2016,3));
    }

    @Test
    public void getFirstWeek(){
        assertEquals(7,CalendarUtil.getFirstWeek(2018,12));
        assertEquals(3,CalendarUtil.getFirstWeek(2019,1));
        assertEquals(5,CalendarUtil.getFirstWeek(2019,8));
        assertEquals(1,CalendarUtil.getFirstWeek(2019,9));
        assertEquals(3,CalendarUtil.getFirstWeek(2019,10));
        assertEquals(6,CalendarUtil.getFirstWeek(2019,11));
        assertEquals(5,CalendarUtil.getFirstWeek(2019,12));
        assertEquals(1,CalendarUtil.getFirstWeek(2019,12));
        assertEquals(4,CalendarUtil.getFirstWeek(2020,12));
    }

    @Test
    public void getFirstDayOfLastMonthTest(){
        assertEquals(28,CalendarUtil.getFirstDayOfLastMonth(2019,8));
        assertEquals(25,CalendarUtil.getFirstDayOfLastMonth(2019,9));
        assertEquals(29,CalendarUtil.getFirstDayOfLastMonth(2019,10));
        assertEquals(27,CalendarUtil.getFirstDayOfLastMonth(2019,11));
        assertEquals(24,CalendarUtil.getFirstDayOfLastMonth(2019,12));
        assertEquals(29,CalendarUtil.getFirstDayOfLastMonth(2020,1));
        assertEquals(30,CalendarUtil.getFirstDayOfLastMonth(2038,6));
    }

    @Test
    public void getLastVisibleDayOfNextMonthTest(){
        assertEquals(7,CalendarUtil.getLastVisibleDayOfNextMonth(2019,8));
        assertEquals(5,CalendarUtil.getLastVisibleDayOfNextMonth(2019,9));
        assertEquals(9,CalendarUtil.getLastVisibleDayOfNextMonth(2019,10));
        assertEquals(7,CalendarUtil.getLastVisibleDayOfNextMonth(2019,11));
        assertEquals(4,CalendarUtil.getLastVisibleDayOfNextMonth(2019,12));

        assertEquals(8,CalendarUtil.getLastVisibleDayOfNextMonth(2023,6));
        assertEquals(5,CalendarUtil.getLastVisibleDayOfNextMonth(2023,7));
        assertEquals(9,CalendarUtil.getLastVisibleDayOfNextMonth(2023,8));
        assertEquals(7,CalendarUtil.getLastVisibleDayOfNextMonth(2023,9));
        assertEquals(4,CalendarUtil.getLastVisibleDayOfNextMonth(2023,10));
    }

    @Test
    public void isInRangeOfMonthInCalendarTest(){
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2019,8,2019,7,28));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2019,8,2019,9,7));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2019,8,2019,8,1));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2019,8,2019,9,1));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2019,8,2019,8,6));
        assertFalse(CalendarUtil.isInRangeOfMonthInCalendar(2019,8,2019,7,27));
        assertFalse(CalendarUtil.isInRangeOfMonthInCalendar(2019,8,2019,9,8));

        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2019,12,2019,11,24));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2019,12,2019,11,30));
        assertFalse(CalendarUtil.isInRangeOfMonthInCalendar(2019,12,2019,11,31));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2019,12,2019,12,1));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2019,12,2019,12,31));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2019,12,2020,1,1));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2019,12,2020,1,4));
        assertFalse(CalendarUtil.isInRangeOfMonthInCalendar(2019,12,2020,1,5));
        assertFalse(CalendarUtil.isInRangeOfMonthInCalendar(2019,8,2020,1,6));

        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2016,2,2016,1,31));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2016,2,2016,3,12));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2016,2,2016,2,1));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2016,2,2016,2,29));
        assertFalse(CalendarUtil.isInRangeOfMonthInCalendar(2016,2,2016,2,30));
        assertFalse(CalendarUtil.isInRangeOfMonthInCalendar(2016,2,2016,2,31));


        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2020,12,2020,11,29));
        assertTrue(CalendarUtil.isInRangeOfMonthInCalendar(2020,12,2021,1,9));
        assertFalse(CalendarUtil.isInRangeOfMonthInCalendar(2020,12,2020,11,28));
        assertFalse(CalendarUtil.isInRangeOfMonthInCalendar(2020,12,2021,1,10));

    }

    @Test
    public void convertDateToIndexTest(){

        assertEquals(0,CalendarUtil.convertDateToIndex(2019,8,2019,7,28));
        assertEquals(3,CalendarUtil.convertDateToIndex(2019,8,2019,7,31));
        assertEquals(4,CalendarUtil.convertDateToIndex(2019,8,2019,8,1));
        assertEquals(35,CalendarUtil.convertDateToIndex(2019,8,2019,9,1));
        assertEquals(41,CalendarUtil.convertDateToIndex(2019,8,2019,9,7));
        assertEquals(0,CalendarUtil.convertDateToIndex(2020,1,2019,12,29));
        assertEquals(-1,CalendarUtil.convertDateToIndex(2025,1,2024,11,29));
        assertEquals(0,CalendarUtil.convertDateToIndex(2025,1,2024,12,29));
        assertEquals(2,CalendarUtil.convertDateToIndex(2025,1,2024,12,31));
        assertEquals(3,CalendarUtil.convertDateToIndex(2025,1,2025,1,1));
        assertEquals(33,CalendarUtil.convertDateToIndex(2025,1,2025,1,31));
        assertEquals(34,CalendarUtil.convertDateToIndex(2025,1,2025,2,1));
        assertEquals(35,CalendarUtil.convertDateToIndex(2025,1,2025,2,2));
        assertEquals(41,CalendarUtil.convertDateToIndex(2025,1,2025,2,8));
        assertEquals(-1,CalendarUtil.convertDateToIndex(2025,1,2025,2,9));

        assertEquals(-1,CalendarUtil.convertDateToIndex(2020,4,2020,3,28));
        assertEquals(0,CalendarUtil.convertDateToIndex(2020,4,2020,3,29));
        assertEquals(2,CalendarUtil.convertDateToIndex(2020,4,2020,3,31));
        assertEquals(3,CalendarUtil.convertDateToIndex(2020,4,2020,4,1));
        assertEquals(17,CalendarUtil.convertDateToIndex(2020,4,2020,4,15));
        assertEquals(32,CalendarUtil.convertDateToIndex(2020,4,2020,4,30));
        assertEquals(33,CalendarUtil.convertDateToIndex(2020,4,2020,5,1));
        assertEquals(34,CalendarUtil.convertDateToIndex(2020,4,2020,5,2));
        assertEquals(41,CalendarUtil.convertDateToIndex(2020,4,2020,5,9));
        assertEquals(-1,CalendarUtil.convertDateToIndex(2020,4,2020,5,10));

        assertEquals(-1,CalendarUtil.convertDateToIndex(2020,2,2020,1,25));
        assertEquals(0,CalendarUtil.convertDateToIndex(2020,2,2020,1,26));
        assertEquals(1,CalendarUtil.convertDateToIndex(2020,2,2020,1,27));
        assertEquals(5,CalendarUtil.convertDateToIndex(2020,2,2020,1,31));
        assertEquals(6,CalendarUtil.convertDateToIndex(2020,2,2020,2,1));
        assertEquals(20,CalendarUtil.convertDateToIndex(2020,2,2020,2,15));
        assertEquals(34,CalendarUtil.convertDateToIndex(2020,2,2020,2,29));
        assertEquals(35,CalendarUtil.convertDateToIndex(2020,2,2020,3,1));
        assertEquals(36,CalendarUtil.convertDateToIndex(2020,2,2020,3,2));
        assertEquals(41,CalendarUtil.convertDateToIndex(2020,2,2020,3,7));
        assertEquals(-1,CalendarUtil.convertDateToIndex(2020,2,2020,3,8));



    }


}
