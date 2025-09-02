package com.example.tellem;

import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class Tab {
    private String name;
    private List<TaskList> lists = new ArrayList<>();

    public Tab(String nameTmp){
        this.name = nameTmp;
    }

    public void setName(String nameTmp){
        name = nameTmp;
    }
    public String getName(){
        return name;
    }

    public void addTaskList(TaskList taskList){
        this.lists.add(taskList);
    }

    public void removeTaskList(int id){
        this.lists.remove(id);
    }

    public int taskListSize(){
        return lists.size();
    }

    public List<TaskList> getTaskLists(){
        return lists;
    }
}
