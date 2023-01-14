package com.example.tocare.UIL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tocare.R;
import com.example.tocare.UIL.Fragment.TaskDetailsFragment;
import com.example.tocare.UIL.Fragment.UsersFragment;

public class TaskDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_task, TaskDetailsFragment.class, null)
                .commit();
    }
}