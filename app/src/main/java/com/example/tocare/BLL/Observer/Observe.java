package com.example.tocare.BLL.Observer;

import android.view.View;

import com.example.tocare.DAL.Data;

import java.util.ArrayList;

public class Observe {

    private static Observe single_instance = null;

    private ArrayList<View> views;

    private Observe() {
        views = new ArrayList<>();
    }

    public static Observe getInstance() {
        if (single_instance == null)
            single_instance = new Observe();
        return single_instance;
    }

    public boolean add(View Item) {
        return views.add(Item);
    }

    public boolean remove(View Item) {
        return views.remove(Item);
    }

    public  void setDisabled() {
        for (View v : views)
            v.setEnabled(false);
    }

    public  void setEnabled() {
        for (View v : views)
            v.setEnabled(true);
    }

    public  void updateAdmin() {

    }

    public void updateChild() {

    }

    public  void updateUser() {

    }

}
