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
            RootRepository.getCategoryDatabase().getCategoryDAO().insert(categories);
            return null;
        }
    }

    public static class SelectAllCategory extends AsyncTask<Void, Void, List<Category>> {

        @Override
        protected List<Category> doInBackground(Void... voids) {
            return RootRepository.getCategoryDatabase().getCategoryDAO().selectAll();
        }
    }

    public static class SelectOneByCategory extends AsyncTask<Category, Void, Category> {

        @Override
        protected Category doInBackground(Category... categories) {
            long uid = categories[0].uid;
            Category category = RootRepository.getCategoryDatabase().getCategoryDAO().selectByUID(uid);

            if(category == null) {
                category = RootRepository.getCategoryDatabase().getCategoryDAO().selectByUID(0);
            }
            return category;
        }
    }

    public static class SelectOneByUid extends AsyncTask<Long, Void, Category> {

        @Override
        protected Category doInBackground(Long... longs) {
            long uid = longs[0];
            Category category = RootRepository.getCategoryDatabase().getCategoryDAO().selectByUID(uid);

            if(category == null) {
                category = RootRepository.getCategoryDatabase().getCategoryDAO().selectByUID(0);
            }
            return category;
        }
    }

    public static class DeleteOnCategory extends AsyncTask<Category, Void, Void> {

        @Override
        protected Void doInBackground(Category... categories) {
            Category category  = categories[0];
            RootRepository.getCategoryDatabase().getCategoryDAO().delete(category);
            return null;
        }
    }

    public static class UpdateOneCategory extends AsyncTask<Category, Void, Void> {

        @Override
        protected Void doInBackground(Category... categories) {
            Category category = categories[0];
            RootRepository.getCategoryDatabase().getCategoryDAO().updateByUID(category.getUid(), category.getParentUID(), category.getCategoryName());
            return null;
        }
    }

}
