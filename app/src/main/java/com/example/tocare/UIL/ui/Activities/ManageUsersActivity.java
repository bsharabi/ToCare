package com.example.tocare.UIL.ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.InflateException;
import android.view.View;
import android.widget.Button;

import com.example.tocare.R;
import com.example.tocare.databinding.ActivityManageUsersBinding;

public class ManageUsersActivity extends AppCompatActivity {

    private ActivityManageUsersBinding binding;
    private Button btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        binding = ActivityManageUsersBinding.inflate(getLayoutInflater());
        btBack = binding.btBack;


        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageUsersActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}