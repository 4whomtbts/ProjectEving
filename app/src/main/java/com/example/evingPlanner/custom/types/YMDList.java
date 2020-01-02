package com.example.evingPlanner.custom.types;

import com.example.evingPlanner.custom.YMD;

import java.util.ArrayList;

public class YMDList extends ArrayList<YMD> {

    private ArrayList<Boolean> checkedList;

    public YMDList(){
        this.checkedList = new ArrayList<>();
    }

    @Override
    public YMDList clone() {
        YMDList result = (YMDList) super.clone();
        for (int i = 0; i < this.size(); i++) {
            result.set(i, this.get(i).clone());
        }
        return result;
    }

    public void checkAll(){
        for(int i=0; i < this.size(); i++){
            this.checkAt(i);
        }
    }

    public void unCheckAll(){
        for(int i=0; i < this.size(); i++){
            this.unCheckAt(i);
        }
    }
    public void checkAt(int index){
        this.get(index).setChecked(true);
    }

    public void unCheckAt(int index){
        this.get(index).setChecked(false);
    }

    public boolean isCheckedAt(int index){

        if(this.size() == 0){
            return false;
        }

        return this.get(index).isChecked();
    }

    public YMDList deleteAllChecked(){
        YMDList newList = new YMDList();

        for(int i=0; i < this.size(); i++){
            if(!this.get(i).isChecked()){
                newList.add(this.get(i));
            }
        }
        return newList;
    }


    public void printCheckedList(){
        for(int i=0 ; i < this.checkedList.size(); i++){
            System.out.print(this.checkedList.get(i)+", ");
        }
    }
    /*  다른 추가 메소드 사용으로 checklist 랑 data-list 의 size가 달라질 수 있다.*/
    @Override

    public boolean add(YMD ymd){
        this.checkedList.add(false);
        return super.add(ymd);
    }

    public boolean isSimilar(YMDList candidate){

        if(this.size() != candidate.size()){
            return false;
        }
        for(int i=0; i < this.size(); i++){
            if(!this.get(i).isSimilar(candidate.get(i))){
                return false;
            }
        }
        return true;
    }







}
