package com.example.evingPlanner.repository;

import com.example.evingPlanner.TSLiveData;
import com.example.evingPlanner.custom.types.YMDList;

public class SingleDayDialogRepository {

    private static SingleDayDialogRepository Inst;
    private static TSLiveData<YMDList> clonePreViewList;

    public static synchronized SingleDayDialogRepository get(){

        if(Inst == null){
            Inst = new SingleDayDialogRepository();
        }
        return Inst;
    }

    private SingleDayDialogRepository(){
        clonePreViewList = new TSLiveData<>(new YMDList());
    }
    private YMDList getClonePreViewListValue(){
        return getClonePreViewList().getValue();
    }

    public TSLiveData<YMDList> getClonePreViewList(){
        return clonePreViewList;
    }
    public void setClonePreViewList(YMDList list){
        clonePreViewList.setValue(list);
    }

    public void setCheckBoxAtChecked(int index){
        YMDList newYMDList =  getClonePreViewListValue();
        newYMDList.checkAt(index);
        setClonePreViewList(newYMDList);
    }
    public void setCheckBoxAtUnChecked(int index){
        YMDList newYMDList =  clonePreViewList.getValue();
        newYMDList.unCheckAt(index);
        setClonePreViewList(newYMDList);
    }
    public boolean isCheckedAt(int index){
        return getClonePreViewListValue().isCheckedAt(index);
    }
}
