package com.example.evingPlanner.ui.singleDayDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import static com.google.common.base.Preconditions.checkNotNull;

public class VocabularyItem extends LinearLayout {

    private final Context context;
    private final VocabularyItemBinding binding;
    private final Fragment fragment;
    private final VocaDayJoinWithVoca vocaDayJoinWithVoca;
    private final VocaListViewAdapter adapter;
    private final VocabularyItemViewModel vmodel;
    private boolean isExpanded = false;

    private TextView vocaText;
    private TextView cycleText;
    private TextView translationText;
    private TextView descriptionText;
    private LinearLayout view;
    private ImageButton optionButton;

    public VocabularyItem(Context context, Fragment fragment, VocaDayJoinWithVoca vocaDayJoinWithVoca, VocaListViewAdapter adapter) {
        super(context);
        checkNotNull(vocaDayJoinWithVoca, "vocaDayJoinWithVoca cannot be null");

        this.context = context;
        this.fragment = fragment;
        this.vocaDayJoinWithVoca = vocaDayJoinWithVoca;
        this.adapter = adapter;

        final LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.vocabulary_item, this, true);

        vmodel = ViewModelProviders.of(fragment).get(VocabularyItemViewModel.class);
        vmodel.init(vocaDayJoinWithVoca);

        if (inflater != null) {
            view = (LinearLayout) inflater.inflate(R.layout.vocabulary_expanded_item, binding.vocabularyItemContainer, true);
            vocaText = view.findViewById(R.id.expanded_voca_text);
            cycleText = view.findViewById(R.id.expanded_cycle_text);
            translationText = view.findViewById(R.id.expanded_translation_text);
            descriptionText = view.findViewById(R.id.expanded_description_text);
            vocaText.setText(vocaDayJoinWithVoca.getVocabulary().getVoca());
            cycleText.setText(getCycleString(vocaDayJoinWithVoca.getVocabulary()));
            translationText.setText("");
            descriptionText.setText("");
            optionButton = view.findViewById(R.id.voca_item_option_button);
            optionButton.setOnClickListener(new VocaOptionButtonListener());
        }
    }

    public String getCycleString(Vocabulary vocabulary) {
        return vocabulary.getThisCycle() + "/" + vocabulary.getTotalCycle();
    }

    public void initView() {

        this.binding.vocabularyItemContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Vocabulary vocabulary = vocaDayJoinWithVoca.getVocabulary();

                if (!isExpanded) {
                    translationText.setText(vocabulary.getTranslation());
                    descriptionText.setText(vocabulary.getDescription());
                    isExpanded = true;

                } else {
                    translationText.setText("");
                    descriptionText.setText("");
                    isExpanded = false;
                }

            }

        });
    }

    class VocaOptionButtonListener implements View.OnClickListener {

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
                            Toast.makeText(context, "개발중인 기능입니다. 1.3.1 버전에서 추가됩니다.", Toast.LENGTH_LONG).show();
                            /*
                            dialog.dismissListener(dismissListener);
                            FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                            dialog.show(ft, "VOCA_MODIFY_DIALOG_FRAGMENT");

                             */
                            break;

                        case R.id.voca_item_delete:
                            try {
                                vmodel.delete(vocaDayJoinWithVoca);
                                adapter.removeItem(vocaDayJoinWithVoca);
                            } catch (Exception e) {
                                Toast.makeText(context, R.string.system_error_try_again, Toast.LENGTH_LONG).show();
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
    }
}
