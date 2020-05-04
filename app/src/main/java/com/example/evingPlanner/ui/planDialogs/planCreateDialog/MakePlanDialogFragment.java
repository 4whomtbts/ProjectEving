package com.example.evingPlanner.ui.planDialogs.planCreateDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evingPlanner.R;
import com.example.evingPlanner.custom.CategorySpinnerAdapter;
import com.example.evingPlanner.custom.PlanTypeSpinnerAdapter;
import com.example.evingPlanner.custom.types.YMDList;
import com.example.evingPlanner.databinding.MakePlanBinding;
import com.example.evingPlanner.domain.Category;
import com.example.evingPlanner.domain.planTypes.PlanType;
import com.example.evingPlanner.ui.planDialogs.clonePreview.ClonePreviewAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class MakePlanDialogFragment extends DialogFragment {

    private View mainView;
    private MakePlanVM vmodel;
    private MakePlanBinding binding;
    private ClonePreviewAdapter adapter;
    private PlanTypeSpinnerAdapter.PlanClickListener planTypeSpinnerListener;
    private FirebaseAnalytics mFirebaseAnalytics;

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

        this.binding.setModel(vmodel);
        this.binding.setLifecycleOwner(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
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
        this.binding.planTitleTextInput.setHint(getContext().getString(R.string.repeat_plan_title));
        this.binding.planTextInputText.setHint(getContext().getString(R.string.plan_detail));
        this.binding.planTextInputText.setMaxLines(5);
        this.binding.planTextInputText.setMaxHeight(100);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void registerAdapters(){

        this.binding.groupSelectSpinner.setLayoutMode(Spinner.MODE_DROPDOWN);
        CategorySpinnerAdapter categorySpinnerAdapter =
                new CategorySpinnerAdapter(getContext(), getFragmentManager(), R.layout.group_spinner_item);
        this.binding.groupSelectSpinner.setAdapter(categorySpinnerAdapter);

        this.binding.planModeSelectSpinner.setLayoutMode(Spinner.MODE_DROPDOWN);
        PlanTypeSpinnerAdapter planTypeArrayAdapter =
                new PlanTypeSpinnerAdapter(getContext(), getFragmentManager(), R.layout.group_spinner_item);
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

        this.vmodel.planTypeMutableLiveData.observe(getViewLifecycleOwner(), new Observer<PlanType>() {
            @Override
            public void onChanged(PlanType planType) {

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
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null) imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                    Bundle bundle = new Bundle();
                    bundle.putString("new_plan", "제목 [ "+title+" ], 내용 [ "+input+" ], 그룹 [ "+group+" ]");
                    mFirebaseAnalytics.logEvent("new_plan", bundle);
                    dismiss();

            }
        });

        this.binding.selectAllTextButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                vmodel.checkAll();
                if(adapter != null){
                    adapter.refreshPreViewAdapter(vmodel.getWillBePlannedDatesArrWithCurrentPlan());
                }
            }
        });

        this.binding.unselectAllTextButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                vmodel.unCheckAll();
                if(adapter != null){
                    adapter.refreshPreViewAdapter(vmodel.getWillBePlannedDatesArrWithCurrentPlan());
                }
            }
    });

        this.binding.deleteSelectedTextButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                vmodel.deleteCheckPreViewElement();
                if(adapter != null){
                    adapter.refreshPreViewAdapter(vmodel.getWillBePlannedDatesArrWithCurrentPlan());
                }
            }
        });

        this.binding.addNewCloneTextButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"개발중인 기능입니다", Toast.LENGTH_LONG).show();
            }
        });


        this.binding.planModeSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
                System.out.println(adapter.getSelectedItem());
                ((TextView)binding.planModeSelectSpinner.getSelectedView())
                        .setTypeface(ResourcesCompat.getFont(getContext(),
                                R.font.nanum_gorthic));
                ((TextView)binding.planModeSelectSpinner.getSelectedView())
                                                     .setTextColor(ContextCompat.getColor(getContext(),R.color.black));
                PlanType ptype =  (PlanType)(adapter.getSelectedItem());
                vmodel.setCurrentSelectedPlanType(ptype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        this.binding.groupSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = (Category) adapterView.getSelectedItem();
                vmodel.currentSelectedCategory = category;
                ((TextView)binding.groupSelectSpinner.getSelectedView())
                        .setTypeface(ResourcesCompat.getFont(getContext(),R.font.nanum_gorthic));
                ((TextView)binding.groupSelectSpinner.getSelectedView())
                        .setTextColor(ContextCompat.getColor(getContext(),R.color.black));

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
