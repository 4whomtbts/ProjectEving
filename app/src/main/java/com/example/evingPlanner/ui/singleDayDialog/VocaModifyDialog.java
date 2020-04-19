package com.example.evingPlanner.ui.singleDayDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.evingPlanner.R;
import com.example.evingPlanner.domain.Vocabulary;
import com.example.evingPlanner.ui.singleDayDialog.movePlanDialog.MovePlanDialogDismissListener;
import com.google.android.material.textfield.TextInputEditText;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VocaModifyDialog extends DialogFragment {

    private VocaModifyDialogDismissListener dismissListener;

    private final Vocabulary vocabulary;
    private TextView saveText;
    private TextInputEditText voca;
    private TextInputEditText translation;
    private TextInputEditText description;


    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();

        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        if(dialog != null){
            int width = (int)(size.x * 0.80);
            int height = (int)(size.y * 0.80);
            dialog.getWindow().setLayout(width,height);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.voca_modify_dialog, container);
        voca = view.findViewById(R.id.voca_modify_input);
        translation = view.findViewById(R.id.voca_modify_translation_input);
        description = view.findViewById(R.id.voca_modify_description_input);
        saveText = view.findViewById(R.id.voca_modify_save_text);
        saveText.setOnClickListener(new SaveTextOnClickListener());
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if(dismissListener != null) {
            dismissListener.handleDialogDismiss(this);
        }
    }

    public Vocabulary getModifiedVocabulary() {
        vocabulary.setVoca(voca.getText().toString());
        vocabulary.setVoca(translation.getText().toString());
        vocabulary.setVoca(description.getText().toString());
        return vocabulary;
    }

    public void dismissListener(VocaModifyDialogDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    class SaveTextOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    }


}
