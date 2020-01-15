package com.example.evingPlanner.custom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.evingPlanner.domain.planTypes.PlanType;

import java.util.ArrayList;

public abstract class AbstractSpinnerAdapter<T> extends ArrayAdapter<T>  {

    @NonNull
    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<T> spinnerItemList = new ArrayList<>();

    public abstract View getView(int position, View convertView, ViewGroup parent);
    protected abstract ArrayList<T> getAllSpinnerItemFromDataBase() throws Exception;
    protected abstract void insertNewSpinnerItemToDataBase() throws Exception;
    protected abstract void deleteSpinnerItemFromDataBase() throws Exception;

    public AbstractSpinnerAdapter(@NonNull Context context, FragmentManager fragmentManager, int resource) {
        super(context, resource);
        this.context = context;
        try {
            spinnerItemList = getAllSpinnerItemFromDataBase();
        }catch(Exception e) {
            e.printStackTrace();
        }

        this.fragmentManager = fragmentManager;
    }

    @Override
    final public int getCount() {
        if (spinnerItemList != null) {
            return spinnerItemList.size() + 1;
        } else {
            return 0;
        }
    }

    @Override
    public T getItem(int position) {
        return spinnerItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }






    private boolean isPositionIsButton(int position) {
        return position == spinnerItemList.size();
    }


}
