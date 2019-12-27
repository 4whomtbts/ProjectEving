package com.example.databinding2.ui.planDialogs.planCreateDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databinding2.R;
import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.custom.planTypeSpinnerAdapter;
import com.example.databinding2.custom.types.YMDList;
import com.example.databinding2.databinding.MakePlanBinding;
import com.example.databinding2.domain.planTypes.PlanType;
import com.example.databinding2.ui.planDialogs.clonePreview.ClonePreviewAdapter;

import java.util.ArrayList;

public class MakePlanDialogFragment extends DialogFragment {

    private MakePlanVM vmodel;
    private MakePlanBinding binding;
    private ClonePreviewAdapter adapter;
    public MakePlanDialogFragment(FragmentManager fragmentManager,boolean isEdit){}


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
        observe(this);
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
            int width = (int)(size.x * 0.80);
            int height=  (int)(size.y * 0.95);
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

        PlanType[] temp = PlanType.getDefaultPlanTypes();

        this.binding.planModeSelectSpinner.setLayoutMode(Spinner.MODE_DROPDOWN);
        ArrayAdapter<PlanType> planTypeArrayAdapter = new planTypeSpinnerAdapter(getContext(),R.layout.group_spinner_item,temp);
        this.binding.planModeSelectSpinner.setAdapter(planTypeArrayAdapter);

    }

    private void observe(final DialogFragment dialogFragment){
        this.vmodel.getWillBeClonedDateList().observe(this, new Observer<YMDList>() {

            private ClonePreviewAdapter adapter;

            @Override
            public void onChanged(YMDList ymds) {
                RecyclerView view = binding.clonePreviewRecyclerView;
                this.adapter =  (ClonePreviewAdapter)view.getAdapter();


                if(adapter != null ){
                    this.adapter.refreshPreViewAdapter(vmodel.getWillBePlannedDatesArrWithCurrentPlan());
                }else{
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
                    this.adapter = new ClonePreviewAdapter(vmodel.getWillBePlannedDatesArrWithCurrentPlan(),dialogFragment);
                    view.setLayoutManager(manager);
                    view.setAdapter(this.adapter);
                }


            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void attachListeners(){

        this.binding.clonePreviewRecyclerView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {



            }
        });
        this.binding.makePlanConfirmButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                    String title = binding.planTitleTextInput.getEditableText().toString();
                    String input = binding.planTextInputLayout.getEditText().getText().toString();
                    String group = binding.groupSelectSpinner.getSelectedItem().toString();
                    vmodel.makeNewPlan(vmodel.getWillBePlannedDatesArrWithCurrentPlan(),title,input,group);


                    dismiss();
                    View v = getActivity().getCurrentFocus();
                    if(v != null){

                }

            }
        });

        this.binding.selectAllTextButton.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent e) {

                if(e.getAction() == MotionEvent.ACTION_DOWN){
                    vmodel.checkAll();
                    if(adapter != null){
                        adapter.refreshPreViewAdapter(vmodel.getWillBePlannedDatesArrWithCurrentPlan());
                    }

                }

                return false;
            }
        });

        this.binding.unselectAllTextButton.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent e) {

                if(e.getAction() == MotionEvent.ACTION_DOWN){
                    vmodel.unCheckAll();
                    if(adapter != null){
                        adapter.refreshPreViewAdapter(vmodel.getWillBePlannedDatesArrWithCurrentPlan());
                    }
                }

                return false;
            }
    });

        this.binding.deleteSelectedTextButton.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent e) {

                if(e.getAction() == MotionEvent.ACTION_DOWN){
                    vmodel.deleteCheckPreViewElement();
                    if(adapter != null){
                        adapter.refreshPreViewAdapter(vmodel.getWillBePlannedDatesArrWithCurrentPlan());
                    }
                }

                return false;
            }
        });
        this.binding.planModeSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(adapterView.getSelectedItem());
                PlanType ptype =  (PlanType)(adapterView.getSelectedItem());
                ptype.print();
                vmodel.setCurrentSelectedPlanType(ptype);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        this.binding.groupSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 vmodel.currentSelectedGroup = (String)adapterView.getSelectedItem();

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
