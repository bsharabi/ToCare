package com.example.tocare.Controller;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import com.example.tocare.R;
import com.example.tocare.UIL.Fragment.UsersFragment;

public class ManageUsersActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_manage, UsersFragment.class, null)
                .commit();
    }


}