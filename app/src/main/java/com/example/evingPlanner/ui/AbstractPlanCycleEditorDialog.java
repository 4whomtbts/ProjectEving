package com.example.evingPlanner.ui;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.evingPlanner.R;
import com.example.evingPlanner.domain.Plan;
import com.example.evingPlanner.domain.planTypes.PlanType;
import com.example.evingPlanner.repository.PlanTypeRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractPlanCycleEditorDialog extends DialogFragment {
    private static final int MAX_CYCLE_NUMBER = 730;

    @NonNull
    private PlanType planType;
    private TextView saveText;
    private TextInputEditText planCycleTitle;
    private TextInputEditText planCycle;
    private String title = null;
    private String cycles = null;

    private boolean isEdit() {
        return (title != null && cycles != null);
    }

    private int[] verifyPlanCycle(final String newTitle, final String newCycles) {

        // 3/4/5/6 과 같은 형태인지 검증한다.
        // 패턴과 매칭된 문자열과, newCycles와 길이를 비교해서 완벽히 매칭되는지 확인한다.
        String correctPattern = "(-?\\d+/?){1,}";
        Pattern extractNumberPattern = Pattern.compile("-?\\d+");
        Pattern checkInvalidCharacter = Pattern.compile("[^(-?\\d+)|/]");
        Matcher invalidCharacterMatcher = checkInvalidCharacter.matcher(newCycles);

        if(newTitle == null || newTitle.equals("")) {
            throw new IllegalArgumentException(getResources().getString(R.string.plan_cycle_editor_dialog_title_invalid));
        }

        if(newCycles == null || newCycles.equals("")) {
            throw new IllegalArgumentException(getResources().getString(R.string.plan_cycle_editor_dialog_cycle_invalid_empty));
        }

        if(!newCycles.matches(correctPattern)) {
            throw new IllegalArgumentException(getResources().getString(R.string.plan_cycle_editor_dialog_cycle_invalid));
        }

        while(invalidCharacterMatcher.find()) {
            throw new IllegalArgumentException(getResources().getString(R.string.plan_cycle_editor_dialog_cycle_invalid_char));
        }

        ArrayList<Integer> cycleList = new ArrayList<>();
        Matcher matcher = extractNumberPattern.matcher(newCycles);
        int previousCycle = 0;

        while (matcher.find()) {
            int currentCycle;
            try {
                currentCycle = Integer.parseInt(matcher.group());
            }catch (NumberFormatException numberFormatException) {
                throw new IllegalArgumentException(getResources().getString(R.string.plan_cycle_editor_dialog_cycle_invalid_cycle_should_number));
            }

            if (currentCycle > MAX_CYCLE_NUMBER) {
                throw new IllegalArgumentException(getResources().getString(R.string.plan_cycle_editor_dialog_cycle_invalid_big_cycle));
            }

            if (currentCycle <= 0) {
                throw new IllegalArgumentException(getResources().getString(R.string.plan_cycle_editor_dialog_cycle_invalid_neg));
            }

            // 같은 주기를 입력하는 것을 막기 위한 조건문이지만, 이미 오름차순 제한이 있기 때문에
            // 굳이, 모든 요소를 순회하면서 중복 주기가 존재하는지 확인하지 않고 넘어가겠음.
            if (currentCycle == previousCycle) {
                throw new IllegalArgumentException(getResources().getString(R.string.plan_cycle_editor_dialog_cycle_invalid_dup));
            }

            if (currentCycle < previousCycle) {
                throw new IllegalArgumentException(getResources().getString(R.string.plan_cycle_editor_dialog_cycle_invalid_ordering));
            }

            cycleList.add(currentCycle);
            previousCycle = currentCycle;
        }

        int[] result = new int[cycleList.size()];
        for(int i=0; i < cycleList.size(); i++) {
            result[i] = cycleList.get(i);
        }
        return result;
    }

    private boolean savePlanCycle(final String newTitle, final String newCycles) throws IllegalArgumentException{
            verifyPlanCycle(newTitle, newCycles);
            return true;
    }

    protected AbstractPlanCycleEditorDialog() {
        this.planType = new PlanType();
    }

    protected AbstractPlanCycleEditorDialog(PlanType planType) {
        assert(planType != null);
        this.planType = planType;
        this.title = planType.planTypeName;
        this.cycles = planType.getSeparatedCycle();
    }

    abstract protected void databaseTransaction(PlanType newPlan) throws ExecutionException, InterruptedException;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =inflater.inflate(R.layout.create_plan_cycle_dialog, container);
        planCycleTitle = view.findViewById(R.id.create_plan_cycle_dialog_title_input);
        planCycle = view.findViewById(R.id.create_plan_cycle_dialog_content_input);
        saveText = view.findViewById(R.id.create_plan_cycle_dialog_save_text);
        saveText.setOnClickListener(new SaveTextOnClickListener());

        if(isEdit()) {
            planCycleTitle.setText(this.title);
            planCycle.setText(this.cycles);
        }
        return view;
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
            int height = (int) (size.y * 0.8);
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    class SaveTextOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String title = planCycleTitle.getText().toString();
            String cycle = planCycle.getText().toString();

            int[] extractedCycleList;
            try {
                extractedCycleList = verifyPlanCycle(title, cycle);

                planType.planTypeName = title;
                planType.cycles = extractedCycleList;
            }catch(IllegalArgumentException exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            try {
                databaseTransaction(planType);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dismiss();
        }
    }
}
