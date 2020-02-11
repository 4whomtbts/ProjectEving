package com.example.evingPlanner.ui.main;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.evingPlanner.R;

public class VersionReleaseDialog extends DialogFragment {
    TextView releaseText;
    Button confirmButton;
    String titleRes;
    String contentRes;

    public VersionReleaseDialog(String titleRes, String contentRes) {
        this.titleRes = titleRes;
        this.contentRes = contentRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().setTitle(this.titleRes);
        View view = inflater.inflate(R.layout.version_release_dialog, container);
        releaseText = view.findViewById(R.id.release_content_text);
        confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        releaseText.setText(this.contentRes);
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
            int height = (int) (size.y * 0.70);
            dialog.getWindow().setLayout(width, height);
        }
    }
}

