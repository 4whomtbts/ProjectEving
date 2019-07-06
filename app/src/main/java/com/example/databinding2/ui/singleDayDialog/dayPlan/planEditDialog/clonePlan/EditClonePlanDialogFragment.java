package com.example.databinding2.ui.singleDayDialog.dayPlan.planEditDialog.clonePlan;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.databinding2.R;
import com.example.databinding2.databinding.EditPlanCloneBinding;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.singleDayDialog.dayPlan.planEditDialog.EditPlanVM;

public class EditClonePlanDialogFragment extends DialogFragment {


    private EditPlanVM vmodel;
    private EditPlanCloneBinding binding;
    private FragmentManager fragmentManager;
    private Plan thisPlan;
    private boolean isEdit;
    public EditClonePlanDialogFragment(FragmentManager fragmentManager, Plan plan){
        this.fragmentManager = fragmentManager;
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
        vmodel = ViewModelProviders.of(this).get(EditPlanVM.class);

        this.binding.setModel(vmodel);
        this.binding.setLifecycleOwner(this);
        this.setCancelable(true);

        attachListeners();
        View view = this.binding.getRoot();
        this.binding.planTitleInputText.setHint("원래 타이틀");
        this.binding.planContentInputText.setHint("세부 내용");
        this.binding.planStudySuggestionText.setMovementMethod(new ScrollingMovementMethod());
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
            .SOFT_INPUT_ADJUST_RESIZE);
        }

    }

    private Plan ConstructPlan(){
        Plan plan = new Plan();
        plan.setTextPlan("helllo"
        );
        return plan;
    }
    private void attachListeners(){

        this.binding.makePlanConfirmButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {



                dismiss();
                View v = getActivity().getCurrentFocus();
                if(v != null){
                    InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                }
            }
        });

    }

}
