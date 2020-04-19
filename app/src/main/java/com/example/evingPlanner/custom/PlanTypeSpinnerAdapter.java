package com.example.evingPlanner.custom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.example.evingPlanner.domain.planTypes.PlanType;
import com.example.evingPlanner.repository.PlanTypeRepository;
import com.example.evingPlanner.ui.CreatePlanCycleDialog;
import com.example.evingPlanner.ui.EditPlanCycleDialog;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PlanTypeSpinnerAdapter extends ArrayAdapter<PlanType> {

    private Context context;
    private FragmentManager fragmentManager;
    public ArrayList<PlanType> planTypeArrayList;
    public PlanTypeSpinnerAdapter.PlanClickListener planTypeSelectedListener;

    private boolean isPositionIsButton(int position) {
        return position == planTypeArrayList.size();
    }

    public PlanTypeSpinnerAdapter(Context context, FragmentManager fragmentManager,
                                  int resource) {
        super(context, resource);
        this.context = context;
        try {
            this.planTypeArrayList = new PlanTypeRepository.SelectAll().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.fragmentManager = fragmentManager;
        planTypeSelectedListener = new PlanClickListener();
    }

    @Override
    public int getCount() {
        if (planTypeArrayList != null) {
            return planTypeArrayList.size() + 1;
        } else {
            return 0;
        }
    }

    @Override
    public PlanType getItem(int position) {
        return planTypeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position < planTypeArrayList.size() && planTypeArrayList.get(position) != null) {
            TextView label;
            label = (TextView) super.getView(position, convertView, parent);
            label.setText(planTypeArrayList.get(position).getPlanTypeName());
            return label;
        } else {
            TextView label = new TextView(context);
            label.setText("hello");
            return label;
        }
    }

    /**
     * 복습 주기 추가를 위해서 맨 아래의 요소는 추가버튼 요소로 그린다.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {

        @NonNull
        View item;
        if (position == planTypeArrayList.size()) {
            item = LayoutInflater.from(context).inflate(R.layout.plan_spinner_button_layout, parent, false);
            LinearLayout wrapper = item.findViewById(R.id.plan_spinner_button_layout_wrapper);
            ImageView plusButton = item.findViewById(R.id.plan_spinner_plus_image);
            TextView label = item.findViewById(R.id.plan_spinner_new_cycle_text);

            wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment createPlanCycleDialog = new CreatePlanCycleDialog();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    createPlanCycleDialog.show(transaction, "CREATE_PLAN_CYCLE_DIALOG");
                    fragmentManager.executePendingTransactions();
                    createPlanCycleDialog.getDialog()
                            .setOnDismissListener(
                                    new CreatePlanCycleDialogDismissListener());
                }
            });
            return item;
        }

        final PlanType currentPlanType = planTypeArrayList.get(position);

        item = LayoutInflater.from(context).inflate(R.layout.plan_cycle_item, parent, false);
        ConstraintLayout container = item.findViewById(R.id.plan_cycle_item_container);
        final String title = planTypeArrayList.get(position).getPlanTypeName();
        final String cycles = planTypeArrayList.get(position).getSeparatedCycle();
        TextView titleTextView = item.findViewById(R.id.plan_spinner_plan_title);
        TextView cyclesTextView = item.findViewById(R.id.plan_spinner_plan_cycle);
        titleTextView.setText(title);
        cyclesTextView.setText(cycles);
        ImageButton optionButton = item.findViewById(R.id.plan_spinner_plan_option_button);
        optionButton.setFocusable(false);
        optionButton.setFocusableInTouchMode(false);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.plan_cycle_item_option_edit) {
                            DialogFragment EditPlanCycleDialog =
                                    new EditPlanCycleDialog(currentPlanType);
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            EditPlanCycleDialog.show(transaction, "EDIT_PLAN_CYCLE_DIALOG");
                            fragmentManager.executePendingTransactions();

                            EditPlanCycleDialog.getDialog()
                                    .setOnDismissListener(
                                            new EditPlanCycleDialogDismissListener(currentPlanType, position));

                        }

                        if (item.getItemId() == R.id.plan_cycle_item_option_delete) { // 삭제모드

                            if(planTypeArrayList.size() == 1) {
                                Toast.makeText(context,
                                               context.getResources().getString(R.string.plan_type_minimum),
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
                                                        new PlanTypeRepository
                                                                .DeleteOnePlanByUID()
                                                                .execute(currentPlanType)
                                                                .get();
                                                        ArrayList<PlanType> newList = new ArrayList<>(planTypeArrayList);
                                                        planTypeArrayList.clear();
                                                        newList.remove(position);
                                                        planTypeArrayList = newList;
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

    class EditPlanCycleDialogDismissListener extends AbstractPlanCycleDialogDismissListener {

        private PlanType currentPlanType;
        private int position;

        public EditPlanCycleDialogDismissListener(final PlanType currentPlanType, final int position) {
            this.currentPlanType = currentPlanType;
            this.position = position;
        }

        @Override
        ArrayList<PlanType> getNewListForAdapter() {
            PlanType refreshedPlanType = new PlanType();
            ArrayList<PlanType> newList = new ArrayList<>(planTypeArrayList);

            try {
                refreshedPlanType =
                        new PlanTypeRepository
                                .SelectOnePlanTypeByUID()
                                .execute(currentPlanType)
                                .get();
                newList.set(position, refreshedPlanType);
            } catch (Exception e) {
               showDatabaseExceptionToast(e);
            }
            return newList;
        }
    }

    class CreatePlanCycleDialogDismissListener extends AbstractPlanCycleDialogDismissListener {

        public CreatePlanCycleDialogDismissListener() {
            super();
        }

        @Override
        ArrayList<PlanType> getNewListForAdapter() {
            ArrayList<PlanType> newPlanType = new ArrayList<>();
            try {
                newPlanType = new PlanTypeRepository.SelectAll()
                                                    .execute()
                                                    .get();
            } catch (Exception e) {
                showDatabaseExceptionToast(e);
            }
            return newPlanType;
        }
    }

    abstract class AbstractPlanCycleDialogDismissListener implements DialogInterface.OnDismissListener {

        abstract ArrayList<PlanType> getNewListForAdapter();

        protected void showDatabaseExceptionToast(Exception e) {
            Toast.makeText(context,
                    context.getResources()
                            .getString(R.string.system_error_try_again),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ArrayList<PlanType> newList = getNewListForAdapter();
            planTypeArrayList.clear();
            planTypeArrayList = newList;
            notifyDataSetChanged();
        }
    }
}

class PlanCycleItem implements PopupMenu.OnMenuItemClickListener {

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.plan_cycle_item_option_edit) {

        } else {

        }

        return false;
    }
}
