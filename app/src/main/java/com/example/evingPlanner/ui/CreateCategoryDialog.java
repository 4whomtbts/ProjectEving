package com.example.evingPlanner.ui;

import com.example.evingPlanner.domain.Category;
import com.example.evingPlanner.repository.CategoryRepository;

import java.util.concurrent.ExecutionException;

public class CreateCategoryDialog extends AbstractCategorySpinnerDialog {

    public CreateCategoryDialog() {
        super();
    }

    @Override
    protected void databaseTransaction(Category category) throws ExecutionException, InterruptedException {
        new CategoryRepository.InsertOneCategory().execute(category).get();
    }
}
