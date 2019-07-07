package com.example.databinding2.ui.planDialogs.clonePreview;

import com.example.databinding2.TSLiveData;
import com.example.databinding2.custom.YMD;
import com.example.databinding2.custom.types.YMDList;
import com.example.databinding2.repository.RootRepository;
import com.example.databinding2.repository.SingleDayDialogRepository;

public class ClonePreViewVM {

    private YMD _date;
    private int position;
    private SingleDayDialogRepository repository;
    public ClonePreViewVM(int position) {
        this.position = position;
        this.repository = RootRepository.getSingleDayDialogRepository();
    }

    private YMD getMyYMD(){
        return getWillBeClonedDateList().getValue().get(this.position);
    }

    public String getDate(){
        return getMyYMD().getYear()+"년 "+ getMyYMD().getMonth()+"월 "+getMyYMD().getDay()+"일";
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
