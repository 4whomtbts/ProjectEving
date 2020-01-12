package com.example.evingPlanner.repository;

import android.os.AsyncTask;

import com.example.evingPlanner.domain.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private static CategoryRepository Inst;

    public static CategoryRepository get(){
        if(Inst == null){
            Inst = new CategoryRepository();
        }
        return Inst;
    }

    public static class InsertOneCategory extends AsyncTask<Category, Void, Void> {

        @Override
        protected Void doInBackground(Category... categories) {
            Category category = categories[0];
            RootRepository.getCategoryDAO().insert(categories);
            return null;
        }
    }

    public static class SelectAllCategory extends AsyncTask<Void, Void, List<Category>> {

        @Override
        protected List<Category> doInBackground(Void... voids) {
            List<Category> categories = RootRepository.getCategoryDAO().selectAll();
            return null;
        }
    }

}
