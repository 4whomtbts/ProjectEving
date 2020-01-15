package com.example.evingPlanner.ui;

import com.example.evingPlanner.domain.Category;
import com.example.evingPlanner.repository.CategoryRepository;

import java.util.concurrent.ExecutionException;

public class CategorySpinnerEditDialog extends AbstractCategorySpinnerDialog {

    public CategorySpinnerEditDialog(Category category) {
        super(category);
    }
    @Override
    protected void databaseTransaction(Category category) throws ExecutionException, InterruptedException {
        new CategoryRepository.UpdateOneCategory().execute(category);
    }
}
