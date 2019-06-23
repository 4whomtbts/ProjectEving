package com.example.databinding2.ui.presenter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.databinding2.R;
import com.example.databinding2.databinding.MakePlanBinding;
import com.example.databinding2.domain.Plan;
import com.example.databinding2.ui.viewmodel.MakePlanVM;
import com.google.android.material.textfield.TextInputEditText;

public class MakePlanDialogFragment extends DialogFragment {

    private MakePlanVM vmodel;
    private MakePlanBinding binding;
    private FragmentManager fragmentManager;
    MakePlanDialogFragment(FragmentManager fragmentManager){
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

        attachListeners();
        View view = this.binding.getRoot();
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height=  ViewGroup.LayoutParams.WRAP_CONTENT;
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

                    String input = binding.makePlanTextInput.getEditText().toString();
                    vmodel.makeNewPlan("가나다라마바사");

            }
        });
        //this.binding.makePlanTextInput

    }



}
