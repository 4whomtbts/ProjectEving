package com.example.databinding2.ui.singleDayDialog.dayPlan.planCreateDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.databinding2.R;
import com.example.databinding2.databinding.MakePlanBinding;
import com.example.databinding2.domain.Plan;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MakePlanDialogFragment extends DialogFragment {

    private MakePlanVM vmodel;
    private MakePlanBinding binding;
    private FragmentManager fragmentManager;
    private boolean isEdit;
    public MakePlanDialogFragment(FragmentManager fragmentManager,boolean isEdit){
            this.fragmentManager = fragmentManager;
            this.isEdit = isEdit;

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

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        this.binding.setModel(vmodel);
        this.binding.setLifecycleOwner(this);
        this.setCancelable(true);

        View view = this.binding.getRoot();

        viewInit();
        registerAdapters();
        attachListeners();

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            int width = 960;
            int height=  1280;
            dialog.getWindow().setLayout(width,height);
        }

    }

    private void viewInit(){
        this.binding.planTitleTextInput.setHint("계획 제목");
        this.binding.planTextInputText.setHint("세부 내용");
        this.binding.planTextInputText.setMaxLines(5);
        this.binding.planTextInputText.setMaxHeight(100);
    }

    private void registerAdapters(){
        ArrayList arrayList = new ArrayList<String>();
        arrayList.add("전공");
        arrayList.add("교양");
        arrayList.add("수학");
        arrayList.add("경제학");
        arrayList.add("법학");
        this.binding.groupSelectSpinner.setLayoutMode(Spinner.MODE_DROPDOWN);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.group_spinner_item,arrayList);
        this.binding.groupSelectSpinner.setAdapter(arrayAdapter);

        ArrayList planSpinnerList = new ArrayList<String>();
        planSpinnerList.add("비반복일회성계획");
        planSpinnerList.add("1/4/7/14 계획법");
        planSpinnerList.add("세밀계획법");
        planSpinnerList.add("주간계획법");
        planSpinnerList.add("나만의 계획법");
        this.binding.planModeSelectSpinner.setLayoutMode(Spinner.MODE_DROPDOWN);
        ArrayAdapter<String> planSpinnerAdapter = new ArrayAdapter<String>(getContext(),R.layout.group_spinner_item,planSpinnerList);
        this.binding.planModeSelectSpinner.setAdapter(planSpinnerAdapter);

    }

    private void attachListeners(){

        this.binding.makePlanConfirmButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                    String input = binding.planTextInputLayout.getEditText().getText().toString();
                    vmodel.makeNewPlan(input);


                    dismiss();
                    View v = getActivity().getCurrentFocus();
                    if(v != null){

                }

            }
        });
        this.binding.groupSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.binding.planTextInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }



}
