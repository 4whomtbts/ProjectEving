package com.example.databinding2.ui.planDialogs.planEditDialog.clonePlan;

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
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.databinding2.R;
import com.example.databinding2.databinding.EditPlanCloneBinding;
import com.example.databinding2.domain.Plan;

public class EditClonePlanDialogFragment extends DialogFragment {


    private EditClonePlanVM vmodel;
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
        vmodel = ViewModelProviders.of(this).get(EditClonePlanVM.class);

        this.binding.setModel(vmodel);
        this.binding.setLifecycleOwner(this);
        this.setCancelable(true);
        vmodel.setCurrentPlan(thisPlan);

        attachListeners();
        View view = this.binding.getRoot();

        initViewDatas();
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

    private void initViewDatas(){
        this.binding.originalPlanText.setText(thisPlan.getParentYMD().toString());
        this.binding.planTitleInputText.setText(thisPlan.getTitle());
        this.binding.planContentInputText.setText(thisPlan.getTextPlan());
        this.binding.currentCycleStateText.setText(thisPlan.getCycleState());
        this.binding.planStudySuggestionText.setMovementMethod(new ScrollingMovementMethod());
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
