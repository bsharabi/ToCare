package com.example.tocare.BLL.Departments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Tasks {

    private List<String> list;

    public Tasks(List<String> list) {
        this.list = list;
    }
    public Tasks() {
        this.list = new ArrayList<>();
    }
    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
    public void add(String item){
        list.add(item);
    }

    public Iterator<String> getIterator(){
        return list.iterator();
    }
}
