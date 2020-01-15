package com.example.evingPlanner.ui;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.evingPlanner.R;
import com.example.evingPlanner.domain.Category;
import com.example.evingPlanner.domain.planTypes.PlanType;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutionException;

public abstract class AbstractCategorySpinnerDialog extends DialogFragment {

    private TextInputEditText inputCategoryName;
    private TextView saveText;
    private String categoryName = null;
    private Category category;

    AbstractCategorySpinnerDialog() {
        this.category = new Category();
    }
    AbstractCategorySpinnerDialog(Category category) {
        assert (category != null);
        this.category = category;
    }

    abstract protected void databaseTransaction(Category newPlan) throws ExecutionException, InterruptedException;

    private boolean isEdit() {
        return (category != null);
    }

    private void verifyInput(String categoryName) {

        if(categoryName == null) {
            throw new IllegalArgumentException(getContext().getResources().getString(R.string.category_spinner_dialog_category_name_invalid_empty));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =inflater.inflate(R.layout.category_spinner_editor_layout, container);
        inputCategoryName = view.findViewById(R.id.category_spinner_dialog_name_input);
        saveText = view.findViewById(R.id.category_spinner_dialog_save_text);
        saveText.setOnClickListener(new AbstractCategorySpinnerDialog.SaveTextOnClickListener());

        if(isEdit()) {
            inputCategoryName.setText(category.categoryName);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);


        if (dialog != null) {
            int width = (int) (size.x * 0.95);
            int height = (int) (size.y * 0.8);
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    class SaveTextOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String categoryName = inputCategoryName.getText().toString();

            try {
                verifyInput(categoryName);
                category.categoryName = categoryName;
            }catch(IllegalArgumentException exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            try {
                databaseTransaction(category);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dismiss();
        }
    }


}
