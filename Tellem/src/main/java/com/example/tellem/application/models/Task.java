package com.example.tellem.application.models;

import java.time.LocalDate;

public class Task {
    private String name;
    private String desc;
    private LocalDate added;
    private boolean isItDue;
    private LocalDate due;

    public Task(String nameTmp, String descTmp, LocalDate date1, LocalDate date2){
        this.name = nameTmp;
        this.desc = descTmp;
        this.added = date1;
        this.due = date2;
        this.isItDue = true;
    }

    public Task(String nameTmp, LocalDate date1, LocalDate date2){
        this.name = nameTmp;
        this.desc = "";
        this.added = date1;
        this.due = date2;
        this.isItDue = true;
    }

    public Task(String nameTmp, String descTmp, LocalDate date1){
        this.name = nameTmp;
        this.desc = descTmp;
        this.added = date1;
        this.isItDue = false;
        this.due = null;
    }
    public Task(String nameTmp, LocalDate date1){
        this.name = nameTmp;
        this.desc = "";
        this.added = date1;
        this.isItDue = false;
        this.due = null;
    }
    //setters
    public void setName(String nameTmp){
        name = nameTmp;
    }
    public void setDesc(String descTmp){
        desc = descTmp;
    }
    public void setAdded(LocalDate date){
        added = date;
    }
    public void setDue(LocalDate date){
        if(date == null){
            isItDue = false;
        } else {
            isItDue = true;
            due = date;
        }
    }

    //getters
    public String getName(){
        return name;
    }
    public String getDesc(){
        return desc;
    }
    public LocalDate getAdded(){
        return added;
    }
    public boolean getIsItDue(){
        return isItDue;
    }
    public LocalDate getDue(){
        return due;
    }

}
