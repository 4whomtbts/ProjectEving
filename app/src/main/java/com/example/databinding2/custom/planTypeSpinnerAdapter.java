package com.example.databinding2.custom;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.databinding2.domain.planTypes.PlanType;

public class planTypeSpinnerAdapter extends ArrayAdapter<PlanType> {

    private Context context;
    private PlanType[] planTypes;

    public planTypeSpinnerAdapter(Context context, int resource, PlanType[] planTypes) {
        super(context, resource, planTypes);
        this.context = context;
        this.planTypes = planTypes;
    }

    @Override
    public int getCount(){
        return planTypes.length;
    }

    @Override
    public PlanType getItem(int position){
        return planTypes[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label=null;


        if(planTypes[position] != null){

            label = (TextView) super.getView(position, convertView, parent);

            label.setTextColor(Color.BLACK);
            // Then you can get the current item using the values array (Users array) and the current position
            // You can NOW reference each method you has created in your bean object (User class)
            label.setText(planTypes[position].getPlanTypeName());

            // And finally return your dynamic (or custom) view for each spinner item
        }
        return label;

    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label=null;

        if(planTypes[position] != null){
            label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(planTypes[position].getPlanTypeName());
        }


        return label;
    }

}
