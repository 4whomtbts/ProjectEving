package com.example.databinding2.ui.planCreateDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.databinding2.R;
import com.example.databinding2.databinding.MakePlanBinding;
import com.example.databinding2.domain.Plan;

public class MakePlanDialogFragment extends DialogFragment {

    private MakePlanVM vmodel;
    private MakePlanBinding binding;
    private FragmentManager fragmentManager;
    public MakePlanDialogFragment(FragmentManager fragmentManager){
            this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        this.binding = DataBindingUtil.inflate(inflater, R.layout.make_plan_dialog,container,false);
        vmodel = ViewModelProviders.of(this).get(MakePlanVM.class);

        this.binding.setModel(vmodel);
        this.binding.setLifecycleOwner(this);
        this.setCancelable(true);

        attachListeners();
        View view = this.binding.getRoot();
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height=  ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width,height);
        }

    }

    private Plan ConstructPlan(){
        Plan plan = new Plan();
        plan.setTextPlan(
                this.binding.makePlanTextInput.getEditText().toString()
        );
        return plan;
    }
    private void attachListeners(){

        this.binding.makePlanConfirmButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                    String input = binding.makePlanTextInput.getEditText().getText().toString();
                    vmodel.makeNewPlan(input);


                    dismiss();
                    View v = getActivity().getCurrentFocus();
                    if(v != null){
                    InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                }

            }
        });
        //this.binding.makePlanTextInput

    }



}
