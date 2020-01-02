package com.example.evingPlanner.ui.planDialogs.clonePreview;

import com.example.evingPlanner.TSLiveData;
import com.example.evingPlanner.custom.YMD;
import com.example.evingPlanner.custom.types.YMDList;
import com.example.evingPlanner.repository.RootRepository;
import com.example.evingPlanner.repository.SingleDayDialogRepository;

public class ClonePreViewVM {

    private YMD date;
    private int position;
    private SingleDayDialogRepository repository;
    public ClonePreViewVM(int position, YMD ymd) {
        this.position = position;
        this.repository = RootRepository.getSingleDayDialogRepository();
        this.date = ymd;
    }

    public String getDate(){
        return date.getYear()+"년 "+ date.getMonth()+"월 "+date.getDay()+"일";
    }

    public String getCycleInfo(){
        return (position+1) +" / "+getWillBeClonedDateList().getValue().size();
    }

    public void noticeCheckBoxIsChecked(){

        this.repository.setCheckBoxAtChecked(this.position);
    }

    public void noticeCheckBoxCheckUnDone(){

        this.repository.setCheckBoxAtUnChecked(this.position);

    }
    public TSLiveData<YMDList> getWillBeClonedDateList(){
        return this.repository.getClonePreViewList();
    }

    public boolean isMyCheckBoxChecked(){
        return this.repository.getClonePreViewList().getValue().isCheckedAt(this.position);
    }

    public boolean isChecked(){
        return this.repository.isCheckedAt(this.position);
    }





}
