package com.example.tocare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    FloatingActionButton facebook, google, twitter;
    float v = 0;
    ImageView heart, handLeft, handRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        facebook = findViewById(R.id.fab_google);
        google = findViewById(R.id.fab_facebook);
        twitter = findViewById(R.id.fab_instagram);
        heart = findViewById(R.id.imgView_heart);
        handLeft = findViewById(R.id.imgView_left_hand);
        handRight = findViewById(R.id.imgView_right_hand);


        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }


        });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


        facebook.setTranslationY(300);
        google.setTranslationY(300);
        twitter.setTranslationY(300);
        tabLayout.setTranslationY(300);


        facebook.setAlpha(v);
        google.setAlpha(v);
        twitter.setAlpha(v);
        tabLayout.setAlpha(v);


        facebook.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        twitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();


    }
}