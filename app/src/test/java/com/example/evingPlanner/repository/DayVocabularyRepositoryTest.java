package com.example.evingPlanner.repository;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.evingPlanner.domain.DayVocaJoin;
import com.example.evingPlanner.model.DayVocaJoinDAO;
import com.example.evingPlanner.model.DayVocaJoinDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DayVocabularyRepositoryTest {

    private DayVocaJoinDatabase dayVocaJoinDB;
    private DayVocaJoinDAO dayVocaJoinDAO;

    @Before
    public void start() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        dayVocaJoinDB = Room.inMemoryDatabaseBuilder(context, DayVocaJoinDatabase.class).build();
        dayVocaJoinDAO = dayVocaJoinDB.getDayVocaJoinDAO();
    }

    @After
    public void closeDB() throws IOException {
        dayVocaJoinDB.close();
    }

    @Test
    public void DayVocaJoinInserTest() throws ExecutionException, InterruptedException {

        final DayVocaJoin dayVocaJoin = new DayVocaJoin(1,1,1);

                new Thread(new Runnable() {
            @Override
            public void run() {
                    dayVocaJoinDAO.insert(dayVocaJoin);
            }
        }).run();

        //new DayVocaJoinRepsotiory.InsertDayVocas().execute(dayVocaJoin);
        //
        //assertThat(list, null);
    }

}
