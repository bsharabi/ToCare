package com.example.tocare.UIL;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tocare.R;
import com.example.tocare.UIL.Fragment.GalleryFragment;


public class PostActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_post, GalleryFragment.class, null)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}