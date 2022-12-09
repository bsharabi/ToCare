package com.example.tocare.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tocare.Adapters.LoginAdapter;
import com.example.tocare.R;
import com.example.tocare.databinding.ActivityLoginBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    private static LoginActivity single_instance = null;

    private ActivityLoginBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton facebook, google, apple, twitter;
    private ImageView heart, handLeft, handRight;
    private ConstraintLayout constraintLayout;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        }

        constraintLayout = binding.constraintLayout;
        tabLayout = binding.tabLayout;
        viewPager = binding.viewPager;
        facebook = binding.fabFacebook;
        google = binding.fabGoogle;
        apple = binding.fabApple;
        twitter = binding.fabTwitter;
        heart = binding.imgViewHeart;
        handLeft = binding.imgViewLeftHand;
        handRight = binding.imgViewRightHand;

        final View activityRootView = findViewById(R.id.root);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();

                activityRootView.getWindowVisibleDisplayFrame(rect);

                int heightDiff = binding.getRoot().getHeight() - (rect.bottom - rect.top);
                if (heightDiff > 100) {
                    constraintLayout.setTranslationY(-500);
                } else {
                    constraintLayout.setTranslationY(0);
                }
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(this);

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

        final float alpha = 0;

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

        facebook.setOnClickListener(this);
        google.setOnClickListener(this);
        apple.setOnClickListener(this);
        twitter.setOnClickListener(this);

    }

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

    public ViewPager2 getViewPager() {
        return viewPager;
    }

    public LoginActivity() {
        if (single_instance == null)
            single_instance = this;
    }

    public static LoginActivity getInstance() {
        if (single_instance == null)
            single_instance = new LoginActivity();

        return single_instance;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fab_google:
                Toast.makeText(LoginActivity.this, "Google button is not available", Toast.LENGTH_LONG).show();
                break;
            case R.id.fab_apple:
                Toast.makeText(LoginActivity.this, "Apple button is not available", Toast.LENGTH_LONG).show();
                break;
            case R.id.fab_facebook:
                Toast.makeText(LoginActivity.this, "Facebook button is not available", Toast.LENGTH_LONG).show();
                break;
            case R.id.fab_twitter:
                Toast.makeText(LoginActivity.this, "Twitter button is not available", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

    }
}