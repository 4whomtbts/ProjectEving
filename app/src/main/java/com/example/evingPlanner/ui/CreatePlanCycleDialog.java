package com.example.evingPlanner.ui;

import com.example.evingPlanner.domain.planTypes.PlanType;
import com.example.evingPlanner.repository.PlanTypeRepository;

import java.util.concurrent.ExecutionException;

public class CreatePlanCycleDialog extends AbstractPlanCycleEditorDialog {

    public CreatePlanCycleDialog() {
        super();
    }

    @Override
    protected void databaseTransaction(PlanType newPlanType) throws ExecutionException, InterruptedException {
        new PlanTypeRepository.InsertOnePlanType().execute(newPlanType).get();
    }
}
