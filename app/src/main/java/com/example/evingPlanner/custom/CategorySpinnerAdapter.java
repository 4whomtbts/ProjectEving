package com.example.evingPlanner.custom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.evingPlanner.R;
import com.example.evingPlanner.domain.Category;
import com.example.evingPlanner.domain.planTypes.PlanType;
import com.example.evingPlanner.repository.CategoryRepository;
import com.example.evingPlanner.ui.CategorySpinnerEditDialog;
import com.example.evingPlanner.ui.CreateCategoryDialog;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {

    private Context context;
    private FragmentManager fragmentManager;
    public ArrayList<Category> categoryArrayList;
    public CategorySpinnerAdapter.PlanClickListener planTypeSelectedListener;

    private boolean isPositionIsButton(int position) {
        return position == categoryArrayList.size();
    }

    public CategorySpinnerAdapter(Context context, FragmentManager fragmentManager,
                                  int resource) {
        super(context, resource);
        this.context = context;
        try {
            this.categoryArrayList = (ArrayList<Category>)new CategoryRepository.SelectAllCategory().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        if (categoryArrayList != null) {
            return categoryArrayList.size() + 1;
        } else {
            return 0;
        }
    }

    @Override
    public Category getItem(int position) {
        if(categoryArrayList.size() == 1)
            position = 0;
        return categoryArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (categoryArrayList != null) {
            TextView label;
            label = (TextView) super.getView(position, convertView, parent);

            if (position == categoryArrayList.size()) {
                label.setText(categoryArrayList.get(position-1).getCategoryName());
                return label;
            }

            label.setText(categoryArrayList.get(position).getCategoryName());
            return label;

        }
        return null;
    }

    /**
     * 복습 주기 추가를 위해서 맨 아래의 요소는 추가버튼 요소로 그린다.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {

        @NonNull
        View item;
        if (position == categoryArrayList.size()) {
            item = LayoutInflater.from(context).inflate(R.layout.category_spinner_button_layout, parent, false);
            LinearLayout wrapper = item.findViewById(R.id.plan_spinner_button_layout_wrapper);
            ImageView plusButton = item.findViewById(R.id.plan_spinner_plus_image);

            wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment createCategoryDialog = new CreateCategoryDialog();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    createCategoryDialog.show(transaction, "CATEGORY_CREATE_DIALOG");
                    fragmentManager.executePendingTransactions();
                    createCategoryDialog.getDialog()
                            .setOnDismissListener(
                                    new CreateCategoryDismissListener());
                }
            });
            return item;
        }

        final Category currentCategory = categoryArrayList.get(position);

        item = LayoutInflater.from(context).inflate(R.layout.category_spinner_item, parent, false);
        ConstraintLayout container = item.findViewById(R.id.category_spinner_item_container);
        final String title = categoryArrayList.get(position).getCategoryName();
        TextView titleTextView = item.findViewById(R.id.category_spinner_category_name);
        titleTextView.setText(title);
        ImageButton optionButton = item.findViewById(R.id.category_spinner_option_button);
        optionButton.setFocusable(false);
        optionButton.setFocusableInTouchMode(false);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                Menu popupMenu = popup.getMenu();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.plan_cycle_item_option_edit) {
                            DialogFragment categorySpinnerEditDialog =
                                    new CategorySpinnerEditDialog(currentCategory);
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            categorySpinnerEditDialog.show(transaction, "CATEGORY_EDIT_DIALOG");
                            fragmentManager.executePendingTransactions();

                            categorySpinnerEditDialog.getDialog()
                                    .setOnDismissListener(
                                            new EditCategoryDismissListener(currentCategory, position));

                        }

                        if (item.getItemId() == R.id.plan_cycle_item_option_delete) { // 삭제모드

                            if(currentCategory.isDefaultCategory()) {
                                Toast.makeText(context,
                                        context.getResources().getString(R.string.cannot_delete_default_category),
                                        Toast.LENGTH_LONG).show();
                                return true;
                            }
                            DialogInterface.OnClickListener deleteDialogClickListener =
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    try {
                                                        new CategoryRepository.DeleteOnCategory()
                                                                              .execute(currentCategory)
                                                                              .get();
                                                        ArrayList<Category> newList = new ArrayList<>(categoryArrayList);
                                                        categoryArrayList.clear();
                                                        newList.remove(position);
                                                        categoryArrayList = newList;
                                                        notifyDataSetChanged();
                                                        dialog.dismiss();
                                                        break;
                                                    } catch (Exception e) {
                                                        Toast.makeText(context,
                                                                context.getResources()
                                                                        .getString(R.string.system_error_try_again),
                                                                Toast.LENGTH_LONG).show();
                                                        e.printStackTrace();
                                                    }
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    dialog.dismiss();
                                                    break;
                                            }
                                        }
                                    };

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.plan_remove_reask_dialog);
                            builder.setMessage(getContext().getResources().getString(R.string.delete_reask))
                                    .setPositiveButton(getContext().getResources().getString(R.string.yes),
                                            deleteDialogClickListener)
                                    .setNegativeButton(getContext().getResources().getString(R.string.no),
                                            deleteDialogClickListener).show();
                        }

                        return true;
                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.plan_cycle_item_option_menu, popup.getMenu());
                popup.show();
            }
        });

        return item;
    }

    public class PlanClickListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
            System.out.println("위치 " + position);
            if (isPositionIsButton(position)) {
                return;
            }
            PlanType ptype = (PlanType) (adapter.getSelectedItem());

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class EditCategoryDismissListener extends CategorySpinnerAdapter.AbstractPlanCycleDialogDismissListener {

        private Category currentCategory;
        private int position;

        public EditCategoryDismissListener(final Category currentCategory, final int position) {
            this.currentCategory = currentCategory;
            this.position = position;
        }

        @Override
        ArrayList<Category> getNewListForAdapter() {
            Category refreshedPlanType = new Category();
            ArrayList<Category> newList = new ArrayList<>(categoryArrayList);

            try {
                refreshedPlanType =
                        new CategoryRepository.SelectOneByCategory()
                                .execute(currentCategory)
                                .get();
                newList.set(position, refreshedPlanType);
            } catch (Exception e) {
                showDatabaseExceptionToast(e);
            }
            return newList;
        }
    }

    class CreateCategoryDismissListener extends CategorySpinnerAdapter.AbstractPlanCycleDialogDismissListener {

        public CreateCategoryDismissListener() {
            super();
        }

        @Override
        ArrayList<Category> getNewListForAdapter() {
            ArrayList<Category> newCategory = new ArrayList<>();
            try {
                newCategory = (ArrayList<Category>)new CategoryRepository.SelectAllCategory()
                                                    .execute()
                                                    .get();
            } catch (Exception e) {
                showDatabaseExceptionToast(e);
            }
            return newCategory;
        }
    }

    abstract class AbstractPlanCycleDialogDismissListener implements DialogInterface.OnDismissListener {

        abstract ArrayList<Category> getNewListForAdapter();

        protected void showDatabaseExceptionToast(Exception e) {
            Toast.makeText(context,
                    context.getResources()
                            .getString(R.string.system_error_try_again),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ArrayList<Category> newList = getNewListForAdapter();
            categoryArrayList.clear();
            categoryArrayList = newList;
            notifyDataSetChanged();
        }
    }
}

class CategoryItem implements PopupMenu.OnMenuItemClickListener {

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.plan_cycle_item_option_edit) {

        } else {

        }

        return false;
    }
}
