package com.example.databinding2.domain.planTypes;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.databinding2.custom.YMD;
import com.example.databinding2.custom.types.YMDList;
import com.example.databinding2.domain.PlanCreator;

import java.util.ArrayList;

@Entity(tableName = "table_planTypes")
public class PlanType extends BaseObservable {

    @NonNull
    @PrimaryKey
    public String planTypeName;
    public boolean isDefault;
    public boolean isStudyPlan;
    public int[] cycles;
    public String[] suggestions;

    public PlanType(String planTypeName, boolean isDefault, int[] cycles, String[] suggestions) {
        this.planTypeName = planTypeName;
        this.isDefault = isDefault;
        this.cycles = cycles;

        if(suggestions == null){
            this.suggestions = new String[1];
        }
        this.suggestions = suggestions;

    }

    public String getPlanTypeName() {
        return planTypeName;
    }

    public PlanType setPlanTypeName(String planTypeName) {
        this.planTypeName = planTypeName;
        return this;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public PlanType setDefault(boolean aDefault) {
        isDefault = aDefault;
        return this;
    }

    public int[] getCycles() {
        return cycles;
    }

    public PlanType setCycles(int[] cycles) {
        this.cycles = cycles;
        return this;
    }

    public String[] getSuggestions() {
        return suggestions;
    }

    public PlanType setSuggestions(String[] suggestions) {
        this.suggestions = suggestions;
        return this;
    }

    public boolean isStudyPlan(){
        return this.isStudyPlan;
    }

    public PlanType setIsStudyPlan(boolean bool){
        this.isStudyPlan = bool;
        return this;
    }

    public YMDList getPlanDatesFromNowArray(YMD now){
        return PlanCreator.genericMakePlanByMode(now,this.cycles);
    }


   // private static ArrayList<Integer> TYPICAL_MODE = {2,5,8,15,31,91,121};
    // private static ArrayList<Integer> DENSE_MODE = {2,5,8,15,22,29,41,61,91,121};

    public static PlanType[] getDefaultPlanTypes(){

         ArrayList<Integer> typicalModeList = new ArrayList<>();
         ArrayList<Integer> denseModeList = new ArrayList<>();

        int[] typicalModeArr = {1,2,5,8,15,31,91,121};
        for(int i=0; i < typicalModeArr.length; i++){
            typicalModeList.add(typicalModeArr[i]);
        }

        int[] denseModeArr = {1,2,5,8,15,22,29,41,61,91,121};
        for(int i=0; i < denseModeArr.length; i++){
            denseModeList.add(denseModeArr[i]);
        }


        PlanType[] array = new PlanType[3];



        String[] firstSuggestions = new String[typicalModeList.size()];
        for(int i=0; i < typicalModeList.size(); i++){
            firstSuggestions[i] = Integer.toString(typicalModeList.get(i));
        }
        array[0] = new PlanType("1/4/7/14",true,typicalModeArr,firstSuggestions).setIsStudyPlan(true);

        String[] secondSuggestions = new String[denseModeList.size()];
        for(int i=0; i < denseModeList.size(); i++){
            secondSuggestions[i] = Integer.toString(denseModeList.get(i));
        }

        array[1] = new PlanType("세밀계획",true,denseModeArr,secondSuggestions).setIsStudyPlan(true);


        array[2] = new PlanType("할 일",true,null,null).setIsStudyPlan(false);
        return array;
    }

    @Override
    public String toString(){

        if(isStudyPlan){
            return "계획 이름 : "+planTypeName+"\n"+
                    "기본계획여부 : "+isDefault+"\n"+
                    "반복계획여부 : "+isStudyPlan+"\n"+
                    "총 사이클 횟수 : "+cycles.length+"\n";
        }else{
            return "계획 이름 : "+planTypeName+"\n"+
                    "기본계획여부 : "+isDefault+"\n"+
                    "반복계획여부 : "+isStudyPlan+"\n";
        }
    }

    public void print(){

        if(cycles == null){
            return;
        }

        for(int i=0; i < cycles.length; i++){
            System.out.print(cycles[i]+", ");
        }
    }
}

