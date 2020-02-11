package com.example.evingPlanner;

import com.example.evingPlanner.custom.YMD;
import com.example.evingPlanner.domain.Plan;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;


public class SortTest {

    class Foo implements Comparable<Foo> {
        long age;
        Foo(long age) {
            this.age = age;
        }

        @Override
        public int compareTo(Foo o) {
            if(this.age < o.age) {
                return 1;
            }else if(this.age > o.age){
                return -1;
            }
            return 0;
        }
    }
    private final ArrayList<Plan> list = new ArrayList<Plan>();
    private final ArrayList<Foo> fooList = new ArrayList<>();

    @Before
    public void init() {
        for(int i=0; i < 10; i++) {
            double seed = Math.random();
            long currentMillis = (long)(seed * 100);
            list.add(new Plan(currentMillis, currentMillis, null));
            fooList.add(new Foo(currentMillis));
        }
    }

    @Test
    public void sortTest() {
        Collections.sort(list);
        for(Plan plan : list) {
            System.out.println(plan.getUID());
        }
        assertEquals(true, list.get(0).uid < list.get(1).uid);
        assertEquals(true, list.get(5).uid < list.get(9).uid);
    }

    @Test
    public void sortFooTest() {
        Collections.sort(fooList);
        for(Foo foo : fooList) {
            System.out.println(foo.age);
        }

    }
}
