package com.example.databinding2.ui.presenter;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.databinding2.R;
import com.example.databinding2.databinding.DayDialogBinding;
import com.example.databinding2.domain.DayClass;
import com.example.databinding2.repository.CalendarRepository;
import com.example.databinding2.ui.viewmodel.CalendarDayDetailVM;
import com.google.android.material.textfield.TextInputEditText;


public class DayDialogFragment extends DialogFragment {

    private int currDay;
    private CalendarDayDetailVM vmodel;
    private DayDialogBinding binding;
    private TextView DayText;
    private TextInputEditText DayContentText;
    private Button DayContentConfirm;


    public static DayDialogFragment getInstance(String mainMsg){
        Bundle bundle = new Bundle();
        bundle.putString("1234",mainMsg);
        DayDialogFragment e =  new DayDialogFragment();
        e.setArguments(bundle);

        return e;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if (getArguments() != null) {

             String mMainMsg = getArguments().getString("1234");
         }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.binding = DataBindingUtil.inflate(inflater,R.layout.day_dialog,container,false);

        vmodel = ViewModelProviders.of(this).get(CalendarDayDetailVM.class);
        this.binding.setModel(vmodel);
        this.binding.setLifecycleOwner(this);

        this.DayText = this.binding.dayText;
        this.DayContentText = this.binding.dayContentText;
        this.DayContentConfirm = this.binding.dayContentConfirm;
        DayText.setText(
                String.valueOf(vmodel.getGlobalCurrentCalendarDay()));
        View view = this.binding.getRoot();

        attachListeners();

        return view;
    }


    private void attachListeners(){

        this.DayContentConfirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String content = DayContentText.getText().toString();
                DayText.setText(content);
                DayClass newDay = new DayClass(vmodel.getGlobalCurrentYMD());
                vmodel.insertDays(newDay);

                DayClass result =
                        vmodel.getDayByDay(newDay);
                DayText.setText(String.valueOf(result.day));
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);

        }
        CalendarRepository repo = CalendarRepository.get();
        String year = Integer.toString(repo.getGlobalCurrentCalendarMonth());


        this.DayText = binding.dayText;
        Log.e("데이텍스트 : ",year+", "+this.DayText.getText());

        String result = "현재 일 : "+String.valueOf(vmodel.getGlobalCurrentCalendarDay());
        this.DayText.setText(result);

    }

    public void setDay(String day){
        this.currDay = Integer.parseInt(day);
    }


    public void observe(){
    }

}
