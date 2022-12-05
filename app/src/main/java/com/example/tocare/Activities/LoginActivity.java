package com.example.tocare.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import android.widget.ImageView;

import com.example.tocare.Adapters.LoginAdapter;
import com.example.tocare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    FloatingActionButton facebook, google, apple,twitter;
    float alpha = 0;
    ImageView heart, handLeft, handRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        facebook = findViewById(R.id.fab_google);
        google = findViewById(R.id.fab_facebook);
        apple = findViewById(R.id.fab_apple);
        twitter = findViewById(R.id.fab_twitter);
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
        apple.setTranslationY(300);

        facebook.setAlpha(alpha);
        google.setAlpha(alpha);
        twitter.setAlpha(alpha);
        tabLayout.setAlpha(alpha);
        apple.setAlpha(alpha);

        facebook.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        apple.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        twitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();

        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();


    }
}