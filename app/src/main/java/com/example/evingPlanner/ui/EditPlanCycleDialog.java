package com.example.evingPlanner.ui;

import com.example.evingPlanner.domain.planTypes.PlanType;
import com.example.evingPlanner.repository.PlanTypeRepository;

import java.util.concurrent.ExecutionException;

public class EditPlanCycleDialog  extends AbstractPlanCycleEditorDialog {

    public EditPlanCycleDialog(PlanType planType) {
        super(planType);
    }

    @Override
    protected void databaseTransaction(PlanType newPlanType) throws ExecutionException, InterruptedException {
        new PlanTypeRepository.UpdatePlanTypeByName().execute(newPlanType).get();
    }
}
