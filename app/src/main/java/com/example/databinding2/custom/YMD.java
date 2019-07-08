package com.example.databinding2.custom;

public class YMD {
    private int year;
    private int month;
    private int day;
    private boolean isChecked;

    public YMD(int year, int month, int day) {
        // day 0 도 허용해서 에러 날 수 도 있음
        if(year<0 || month < 1 || month > 12 || day < 0 || day > 31 ){
            throw new Error("Improper Arguments for YMD class");
        }

        this.year = year;
        this.month = month;
        this.day = day;
        this.isChecked = false;
    }

    public YMD(int year, int month){
        this.year = year;
        this.month = month;
        this.day = 0;
    }

    public YMD nextMonth(){
        if(this.month==12){
            return new YMD(this.year+1,1,this.day);
        }else{
            return new YMD(this.year,this.month+1,this.day);
        }

    }
    public YMD prevMonth(){
        if(this.month==1){
            return new YMD(this.year-1,12,this.day);
        }else {
            return new YMD(this.year, this.month - 1, this.day);
        }
    }

    public YMD nextYear(){
            return new YMD(this.year+1,this.month,this.day);
    }

    public YMD prevYear(){
        if(year==0){
            throw new Error("Year should be positive value");
        }
        return new YMD(this.year-1,this.month,this.day);
    }

    public void setYMD(YMD newYMD) {
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setChecked(boolean check){
        this.isChecked = check;
    }

    public boolean isChecked(){
        return this.isChecked;
    }

    @Override
    public String toString(){
       return this.year+"년"+this.month+"월"+this.day+"일";
    }

    @Override
    public YMD clone() {
        return new YMD(this.getYear(),this.getMonth(),this.getDay());
    }

    @Override
    public boolean equals(Object obj){
        YMD cmp = (YMD)obj;
        return (cmp.getYear()==this.getYear())
                &&(cmp.getMonth()==this.getMonth())
                &&(cmp.getDay()==this.getDay());
    }

    public boolean isSimilar(YMD candidate){
        return (this.getYear()==candidate.getYear())&&(this.getMonth()==candidate.getMonth())
                &&(this.getDay()==candidate.getDay());
    }
    /*
    private int year;
    private int month;
    private int day;
    private boolean isChecked;
    */
    public static YMD deepCopy(YMD target){
        return new YMD(target.getYear(),target.getMonth(),target.getDay());
    }
}
