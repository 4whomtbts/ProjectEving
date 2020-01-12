package com.example.evingPlanner.ui.planDialogs.planEditDialog.originalPlan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evingPlanner.R;
import com.example.evingPlanner.custom.YMD;
import com.example.evingPlanner.custom.PlanTypeSpinnerAdapter;
import com.example.evingPlanner.custom.types.YMDList;
import com.example.evingPlanner.databinding.EditPlanOrgBinding;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.domain.planTypes.PlanType;
import com.example.evingPlanner.repository.PlanRepository;
import com.example.evingPlanner.ui.planDialogs.clonePreview.ClonePreviewAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class EditOrgPlanDialogFragment extends DialogFragment {


    private EditOriginPlanVM vmodel;
    private EditPlanOrgBinding binding;
    private FragmentManager fragmentManager;
    private Plan thisPlan;
    private ClonePreviewAdapter adapter;
    private boolean isInitiated;

    public EditOrgPlanDialogFragment(FragmentManager fragmentManager, Plan thisPlan) {
        this.fragmentManager = fragmentManager;
        this.thisPlan = thisPlan;
        this.isInitiated = false;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.binding = DataBindingUtil.inflate(inflater, R.layout.edit_plan_dialog_org, container, false);
        vmodel = ViewModelProviders.of(this).get(EditOriginPlanVM.class);

        vmodel.setThisPlan(this.thisPlan);

        this.binding.setModel(vmodel);
        this.binding.setLifecycleOwner(this);
        this.setCancelable(true);

        attachListeners();
        View view = this.binding.getRoot();
        this.binding.planTitleInputText.setHint("원래 타이틀");
        this.binding.planContentInputText.setHint("세부 내용");


        initViewData();
        observe(this);
        vmodel.initPreViewListWithDB();
        registerAdapters();
        attachListeners();

        RecyclerView cloneRecyclerView = binding.clonePreviewRecyclerView;
        this.adapter = (ClonePreviewAdapter) cloneRecyclerView.getAdapter();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        ArrayList<Plan> allPlan = new ArrayList<>();

        try {
            allPlan = new PlanRepository.GetAllPlanByPlanParentUID().execute(thisPlan).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final YMDList previewYMDList = new YMDList();

        for(Plan plan : allPlan) {
            previewYMDList.add(new YMD(plan.year, plan.month, plan.day));
        }

        this.adapter = new ClonePreviewAdapter(previewYMDList, null);
        cloneRecyclerView.setLayoutManager(manager);
        cloneRecyclerView.setAdapter(this.adapter);

        return view;
    }

    private void initViewData() {
        this.binding.completePlanCheckBox.setChecked(vmodel.isDone());
        this.binding.planTitleInputText.setText(vmodel.getTitle());
        this.binding.planContentInputText.setText(vmodel.getTextPlan());
        this.binding.currentCycleStateText.setText(vmodel.getCycleState());
        this.binding.currentTotalProgressText.setText(vmodel.getProgress());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);


        if (dialog != null) {
            int width = (int) (size.x * 0.95);
            int height = (int) (size.y * 0.95);
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_ADJUST_RESIZE);
        }

    }


    private void registerAdapters() {
        ArrayList arrayList = new ArrayList<String>();
        arrayList.add("전공");
        arrayList.add("교양");
        arrayList.add("수학");
        arrayList.add("경제학");
        arrayList.add("법학");
        this.binding.groupSelectSpinner.setLayoutMode(Spinner.MODE_DROPDOWN);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.group_spinner_item, arrayList);
        this.binding.groupSelectSpinner.setAdapter(arrayAdapter);

        PlanType[] temp = PlanType.getDefaultPlanTypes();

        this.binding.planModeSelectSpinner.setLayoutMode(Spinner.MODE_DROPDOWN);

        ArrayAdapter<PlanType> planTypeArrayAdapter =
                new PlanTypeSpinnerAdapter(getContext(), fragmentManager, R.layout.group_spinner_item);
        this.binding.planModeSelectSpinner.setAdapter(planTypeArrayAdapter);

    }


    @SuppressLint("ClickableViewAccessibility")
    private void attachListeners() {

        this.binding.completePlanCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                vmodel.isDone = isChecked;

            }
        });
        this.binding.planModeSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(adapterView.getSelectedItem());
                if (isInitiated) {
                    PlanType ptype = (PlanType) (adapterView.getSelectedItem());
                    ptype.print();
                    vmodel.setCurrentSelectedPlanType(ptype);
                } else {

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
                String selected = (String) adapterView.getSelectedItem();
                System.out.println("그룹스피너 선택 : " + selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.binding.selectAllTextButton.setOnTouchListener(new Button.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent e) {

                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    vmodel.checkAll();
                }

                return false;
            }
        });

        this.binding.unselectAllTextButton.setOnTouchListener(new Button.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent e) {

                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    vmodel.unCheckAll();
                }

                return false;
            }
        });

        this.binding.deleteSelectedTextButton.setOnTouchListener(new Button.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    vmodel.deleteCheckPreViewElement();
                }

                return false;
            }
        });

        this.binding.addNewCloneTextButton.setOnTouchListener(new Button.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });


        this.binding.clonePreviewRecyclerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


            }
        });
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(getContext().getResources().getString(R.string.overwrite_content_org)).setPositiveButton("네", dialogClickListener)
                            .setNegativeButton("아니요", dialogClickListener).show();

                } else {
                    dismiss();
                }

                View v = getActivity().getCurrentFocus();
                if (v != null) {

                }
            }
        });

    }

    private void observe(final DialogFragment dialogFragment) {
        this.vmodel.getWillBeClonedDateList().observe(this, new Observer<YMDList>() {

            private ClonePreviewAdapter adapter;

            @Override
            public void onChanged(YMDList ymds) {

            }
        });
    }


}
