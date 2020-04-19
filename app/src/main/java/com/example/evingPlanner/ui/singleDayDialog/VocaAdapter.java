package com.example.evingPlanner.ui.singleDayDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evingPlanner.R;
import com.example.evingPlanner.UIUtils;
import com.example.evingPlanner.custom.PlanTypeSpinnerAdapter;
import com.example.evingPlanner.databinding.VocaListContainerBinding;
import com.example.evingPlanner.domain.VocaDayJoinWithVoca;
import com.example.evingPlanner.domain.planTypes.PlanType;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import lombok.RequiredArgsConstructor;

import static com.google.common.base.Preconditions.checkNotNull;

@RequiredArgsConstructor
public class VocaAdapter extends RecyclerView.Adapter {

    private final Fragment fragment;
    private final FragmentManager fragmentManager;
    private final int year;
    private final int month;
    private final int day;
    private VocaAdapterViewModel vmodel;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final VocaListContainerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.voca_list_container, parent, false);
        vmodel = ViewModelProviders.of(fragment).get(VocaAdapterViewModel.class);
        binding.setLifecycleOwner(fragment);
        return new VocaAdapterViewHolder(parent.getContext(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    class VocaAdapterViewHolder extends RecyclerView.ViewHolder {

        final Context context;
        final VocaListContainerBinding binding;
        final LayoutInflater inflater;

        VocaAdapterViewHolder(Context context, final VocaListContainerBinding binding) {
            super(binding.getRoot());

            this.context = context;
            this.binding = binding;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (inflater != null) {
                LinearLayout vocaCreatorExpandText =
                        (LinearLayout) inflater.inflate(R.layout.voca_create_shrink,
                                binding.vocaCreateWrapper,
                                true);
                vocaCreatorExpandText.setOnClickListener(new ExpandTextOnClickListener(context, fragment, binding, inflater));
            }

            try {
                vmodel.fetchVocas(year, month, day);
                applyVocaLists();
            } catch (Exception e) {
                Toast.makeText(context, context.getResources().getString(R.string.system_error_try_again), Toast.LENGTH_LONG).show();
            }
        }

        private ConstraintLayout.LayoutParams getResizedConstraintLayoutParam(ConstraintLayout.LayoutParams param, int dpSize) {
            param.height = dpSize;
            return param;
        }

        private void applyVocaLists() {
            List<VocaDayJoinWithVoca> todayVocaList = vmodel.todayVocabulary.getValue();
            List<VocaDayJoinWithVoca> reviewVocaList = vmodel.reviewVocabulary.getValue();
            binding.todayVocaList.setAdapter(
                    new VocaListViewAdapter(context, fragment, binding.todayVocaList, todayVocaList));
            UIUtils.setListViewHeightBasedOnItems(binding.todayVocaList);
            binding.reviewVocaList.setAdapter(
                    new VocaListViewAdapter(context, fragment, binding.reviewVocaList, reviewVocaList));
            UIUtils.setListViewHeightBasedOnItems(binding.reviewVocaList);
        }

        /*
        private void buildVocaList(final ViewGroup layout, final List<VocaDayJoinWithVoca> list) {
            checkNotNull(layout, "layout couldn't null");
            checkNotNull(list, "list couldn't null");

            layout.removeAllViewsInLayout();
            for (VocaDayJoinWithVoca vocaDayJoin : list) {
                VocabularyItem item = new VocabularyItem(context, fragment, layout, vocaDayJoin);
                item.initView();
                layout.addView(item);
            }
        }
        */

        private class ExpandTextOnClickListener implements View.OnClickListener {

            private final Context context;
            private final Fragment fragment;
            private final VocaListContainerBinding binding;
            private final LayoutInflater inflater;
            private Spinner cycleSelector;
            private PlanType selectedPlanType;
            private TextInputEditText vocaInput;
            private TextInputEditText vocaTransInput;
            private TextInputEditText vocaDescInput;

            ExpandTextOnClickListener(final Context context, final Fragment fragment, final VocaListContainerBinding binding, final LayoutInflater inflater) {
                this.binding = binding;
                this.inflater = inflater;
                this.context = context;
                this.fragment = fragment;
                //setObservers();
            }

            @Override
            public void onClick(View v) {

                binding.vocaCreateWrapper.removeAllViewsInLayout();

                binding.vocaCreateWrapper.setLayoutParams
                        (getResizedConstraintLayoutParam(
                                (ConstraintLayout.LayoutParams) binding.vocaCreateWrapper.getLayoutParams(), 500));

                LinearLayout expandedView =
                        (LinearLayout) inflater.inflate(R.layout.voca_create_expanded,
                                binding.vocaCreateWrapper,
                                true);

                TextView addNewVocaText = expandedView.findViewById(R.id.add_new_voca_text);

                addNewVocaText.setOnClickListener(new AddNewVocaListener());
                this.cycleSelector = expandedView.findViewById(R.id.cycle_selector);
                this.vocaInput = expandedView.findViewById(R.id.voca_input);
                this.vocaTransInput = expandedView.findViewById(R.id.voca_trans_input);
                this.vocaDescInput = expandedView.findViewById(R.id.voca_desc_input);
                TextView collapseText = expandedView.findViewById(R.id.collapse_text);

                this.cycleSelector.setLayoutMode(Spinner.MODE_DROPDOWN);
                PlanTypeSpinnerAdapter planTypeArrayAdapter =
                        new PlanTypeSpinnerAdapter(context, fragmentManager, R.layout.group_spinner_item);
                this.cycleSelector.setAdapter(planTypeArrayAdapter);
                this.cycleSelector.setOnItemSelectedListener(new PlanTypeSpinnerListener());
                collapseText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.vocaCreateWrapper.removeAllViewsInLayout();
                        binding.vocaCreateWrapper.setLayoutParams
                                (getResizedConstraintLayoutParam(
                                        (ConstraintLayout.LayoutParams) binding.vocaCreateWrapper.getLayoutParams(), 38));

                        LinearLayout expandedView =
                                (LinearLayout) inflater.inflate(R.layout.voca_create_shrink,
                                        binding.vocaCreateWrapper,
                                        true);
                        expandedView.setOnClickListener(new ExpandTextOnClickListener(context, fragment, binding, inflater));
                    }
                });
            }

            private class PlanTypeSpinnerListener implements AdapterView.OnItemSelectedListener {

                @Override
                public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
                    System.out.println(adapter.getSelectedItem());
                    ((TextView) cycleSelector.getSelectedView())
                            .setTypeface(ResourcesCompat.getFont(context,
                                    R.font.nanum_gorthic));
                    ((TextView) cycleSelector.getSelectedView())
                            .setTextColor(ContextCompat.getColor(context, R.color.black));
                    selectedPlanType = (PlanType) (adapter.getSelectedItem());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {


                }
            }

            private class AddNewVocaListener implements View.OnClickListener {

                @Override
                public void onClick(View v) {
                    try {
                        vmodel.addVoca(
                                vocaInput.getText().toString(), vocaTransInput.getText().toString(),
                                vocaDescInput.getText().toString(), selectedPlanType, year, month, day);
                        applyVocaLists();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }
}
