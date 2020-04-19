package com.example.evingPlanner.ui.singleDayDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.evingPlanner.R;
import com.example.evingPlanner.databinding.VocabularyItemBinding;
import com.example.evingPlanner.domain.VocaDayJoinWithVoca;
import com.example.evingPlanner.domain.Vocabulary;

public class VocabularyItem extends LinearLayout {

    final LinearLayout currentView;
    final Context context;
    VocabularyItemBinding binding;
    final VocabularyItemViewModel vmodel;
    final Fragment fragment;
    final VocaDayJoinWithVoca vocaDayJoinWithVoca;
    final VocaListViewAdapter adapter;
    final LayoutInflater inflater;
    private boolean isExpanded = false;
    TextView vocaText;
    TextView cycleText;
    TextView translationText;
    TextView descriptionText;
    LinearLayout view;

    public VocabularyItem(Context context, Fragment fragment, VocaDayJoinWithVoca vocaDayJoinWithVoca, VocaListViewAdapter adapter, int position) {
        super(context);
        this.context = context;

        this.fragment = fragment;
        this.vocaDayJoinWithVoca = vocaDayJoinWithVoca;
        this.currentView = this;
        this.adapter = adapter;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.vocabulary_item, this, true);

        vmodel = ViewModelProviders.of(fragment).get(VocabularyItemViewModel.class);
        vmodel.init(vocaDayJoinWithVoca);

        if (inflater != null) {
            view = (LinearLayout) inflater.inflate(R.layout.vocabulary_shrink_item, binding.vocabularyItemContainer, true);
            vocaText = view.findViewById(R.id.shrink_voca_text);
            cycleText = view.findViewById(R.id.shrink_cycle_text);
            vocaText.setText(vocaDayJoinWithVoca.getVocabulary().getVoca());
            cycleText.setText(getCycleString(vocaDayJoinWithVoca.getVocabulary()));
        }
    }

    public String getCycleString(Vocabulary vocabulary) {
        return vocabulary.getThisCycle()+"/"+vocabulary.getThisCycle();
    }

    public void initView() {

        this.binding.vocabularyItemContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.vocabularyItemContainer.removeAllViews();
                binding.vocabularyItemContainer.removeAllViewsInLayout();

                Vocabulary vocabulary = vocaDayJoinWithVoca.getVocabulary();
                String vocaString = vocabulary.getVoca();
                String cycleString = getCycleString(vocabulary);

                if (!isExpanded) {
                    view = (LinearLayout) inflater.inflate(R.layout.vocabulary_expanded_item, binding.vocabularyItemContainer, true);
                    vocaText = view.findViewById(R.id.expanded_voca_text);
                    cycleText = view.findViewById(R.id.expanded_cycle_text);
                    translationText = view.findViewById(R.id.expanded_translation_text);
                    descriptionText = view.findViewById(R.id.expanded_description_text);
                    vocaText.setText(vocaString);
                    cycleText.setText(cycleString);
                    translationText.setText(vocabulary.getTranslation());
                    descriptionText.setText(vocabulary.getDescription());
                    isExpanded = true;

                } else {
                    view = (LinearLayout) inflater.inflate(R.layout.vocabulary_shrink_item, binding.vocabularyItemContainer, true);
                    vocaText = view.findViewById(R.id.shrink_voca_text);
                    cycleText = view.findViewById(R.id.shrink_cycle_text);
                    vocaText.setText(vocaString);
                    cycleText.setText(cycleString);
                    isExpanded = false;
                }

                ImageButton optionButton = view.findViewById(R.id.voca_item_option_button);
                optionButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(context, v);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.voca_item_modify:
                                        final VocaModifyDialog dialog = new VocaModifyDialog(vocaDayJoinWithVoca.getVocabulary());

                                        VocaModifyDialogDismissListener dismissListener = new VocaModifyDialogDismissListener() {
                                            @Override
                                            public void handleDialogDismiss(VocaModifyDialog dialog) {
                                                Vocabulary modifiedVocabulary = dialog.getModifiedVocabulary();
                                                try {
                                                    vmodel.modify(modifiedVocabulary);
                                                    vocaText.setText(vmodel.voca);
                                                    translationText.setText(vmodel.translation);
                                                    descriptionText.setText(vmodel.description);

                                                } catch (Exception e) {
                                                    Toast.makeText(context, R.string.system_error_try_again, Toast.LENGTH_LONG).show();
                                                    e.printStackTrace();
                                                }
                                            }
                                        };

                                        dialog.dismissListener(dismissListener);
                                        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                                        dialog.show(ft, "VOCA_MODIFY_DIALOG_FRAGMENT");
                                        break;

                                    case R.id.voca_item_delete:
                                        try {
                                            vmodel.delete(vocaDayJoinWithVoca);
                                            adapter.vocaList.remove(vocaDayJoinWithVoca);
                                            adapter.notifyDataSetChanged();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                }
                                return true;
                            }
                        });

                        MenuInflater inflater = popupMenu.getMenuInflater();
                        inflater.inflate(R.menu.voca_option_menu, popupMenu.getMenu());
                        popupMenu.show();
                    }
                });
            }

        });
    }
}
