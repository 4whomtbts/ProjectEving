package com.example.evingPlanner.ui.planDialogs.planEditDialog.clonePlan;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.evingPlanner.R;
import com.example.evingPlanner.custom.CategorySpinnerAdapter;
import com.example.evingPlanner.databinding.EditPlanCloneBinding;
import com.example.evingPlanner.domain.Category;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.domain.planTypes.PlanType;

import java.util.ArrayList;

public class EditClonePlanDialogFragment extends DialogFragment {


    private EditClonePlanVM vmodel;
    private EditPlanCloneBinding binding;
    private Plan thisPlan;
    private boolean isInitiated;
    private Category currentCategory;

    public EditClonePlanDialogFragment(FragmentManager fragmentManager, Plan plan){
        this.thisPlan = plan;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        this.binding = DataBindingUtil.inflate(inflater, R.layout.edit_plan_dialog_clone,container,false);
        vmodel = ViewModelProviders.of(this).get(EditClonePlanVM.class);

        this.binding.setModel(vmodel);
        this.binding.setLifecycleOwner(this);
        this.setCancelable(true);
        vmodel.setCurrentPlan(thisPlan);

        View view = this.binding.getRoot();

        initViewDatas();
        registerAdapters();
        attachListeners();
        return view;
    }



    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();

        Window window = getDialog().getWindow();
        Point size =new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);



        if(dialog != null){
            int width = (int)(size.x * 0.95);
            int height = (int)(size.y * 0.95);
            dialog.getWindow().setLayout(width,height);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
            .SOFT_INPUT_ADJUST_PAN);
        }

    }

    private void initViewDatas(){
        this.binding.completePlanCheckBox.setChecked(vmodel.isDone());
        this.binding.originalPlanText.setText(
                thisPlan.getParentYMD().toString() + getResources().getString(R.string.made_from_original_plan));
        this.binding.planTitleInputText.setText(thisPlan.getTitle());
        this.binding.planContentInputText.setText(thisPlan.getTextPlan());
        this.binding.currentCycleStateText.setText(thisPlan.getCycleState());
        this.binding.currentTotalProgressText.setText(vmodel.getProgress());
    }

    private void registerAdapters(){
        this.binding.groupSelectSpinner.setLayoutMode(Spinner.MODE_DROPDOWN);
        CategorySpinnerAdapter categorySpinnerAdapter =
                new CategorySpinnerAdapter(getContext(), getFragmentManager(), R.layout.group_spinner_item);
        this.binding.groupSelectSpinner.setAdapter(categorySpinnerAdapter);


        final ArrayList<Category> categoryList = categorySpinnerAdapter.categoryArrayList;
        for(int i=0; i < categoryList.size(); i++) {
            Category category = categoryList.get(i);
            if(category.getUid() == this.thisPlan.getGroupUid()) {
                this.binding.groupSelectSpinner.setSelection(i);
                currentCategory = category;
                break;
            }
        }
        this.binding.groupSelectSpinner.setClickable(false);
        this.binding.groupSelectSpinner.setEnabled(false);

        ArrayList planValue = new ArrayList();
        planValue.add(this.thisPlan.group);
        this.binding.planModeSelectSpinner.setLayoutMode(Spinner.MODE_DROPDOWN);
        ArrayAdapter<String> planTypeArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.group_spinner_item,planValue);
        this.binding.planModeSelectSpinner.setAdapter(planTypeArrayAdapter);
        this.binding.planModeSelectSpinner.setEnabled(false);
        this.binding.planModeSelectSpinner.setSelected(true);

    }

    private void attachListeners() {


        this.binding.makePlanConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (vmodel.isDataChanged(
                        binding.planTitleInputText.getText().toString(),
                        binding.planContentInputText.getText().toString(),
                        binding.completePlanCheckBox.isChecked()
                )) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes 버튼을 클릭했을때 처리
                                    vmodel.editPlan();
                                    dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No 버튼을 클릭했을때 처리

                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.plan_remove_reask_dialog);
                    builder.setMessage("데이터가 변경되었습니다. 변경된 데이터로  덮어쓰시겠습니까?" +
                            "현재 수정한 계획은 복사본이므로 현재 계획만 수정됩니다.").setPositiveButton("네", dialogClickListener)
                            .setNegativeButton("아니요", dialogClickListener).show();

                } else {
                    dismiss();
                }

                View v = getActivity().getCurrentFocus();
                if (v != null) {

                }
            }
        });

        this.binding.completePlanCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                vmodel.isDone = isChecked;
            }
        });

        this.binding.planModeSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(adapterView.getSelectedItem());
                if(isInitiated){
                    PlanType ptype =  (PlanType)(adapterView.getSelectedItem());
                    ptype.print();
                    vmodel.setCurrentSelectedPlanType(ptype);
                }else{

                    isInitiated = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.binding.groupSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentCategory = (Category) adapterView.getSelectedItem();
                ((TextView)binding.groupSelectSpinner.getSelectedView())
                        .setTypeface(ResourcesCompat.getFont(getContext(),R.font.nanum_gorthic));
                ((TextView)binding.groupSelectSpinner.getSelectedView())
                        .setTextColor(ContextCompat.getColor(getContext(),R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
