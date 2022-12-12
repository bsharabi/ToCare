package com.example.tocare.UIL.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tocare.R;
import com.example.tocare.databinding.ActivityManageUsersBinding;

public class ManageUsersActivity extends AppCompatActivity {

    private ActivityManageUsersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
    }
}