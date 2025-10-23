package com.example.tellem.application.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    public String name;
    public List<Task> tasks = new ArrayList<>();

    public TaskList(){
        name = "lol";
    }

    public TaskList(String nameTmp){
        name = nameTmp;
    }

    public void addTask(Task task){
        tasks.add(task);
    }
    public void addTask(String name,String desc, LocalDate date1, LocalDate date2){
        tasks.add(new Task(name, desc, date1, date2));
    }
    public void addTask(String name, LocalDate date1, LocalDate date2){
        tasks.add(new Task(name, date1, date2));
    }
    public void addTask(String name, String desc, LocalDate date1){
        tasks.add(new Task(name, desc, date1));
    }
    public void addTask(String name, LocalDate date1){
        tasks.add(new Task(name, date1));
    }

    public void removeTask(int id){
        tasks.set(id, null);
        tasks.remove(id);
    }
}
