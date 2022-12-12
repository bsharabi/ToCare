package com.example.tocare.UIL.ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.tocare.BLL.Adapters.LoginAdapter;
import com.example.tocare.R;
import com.example.tocare.databinding.ActivityLoginBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class LoginActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "LoginActivity";
    private static LoginActivity single_instance = null;
    private ActivityLoginBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton facebook, google, apple, twitter;
    private ImageView heart, handLeft, handRight, hands;
    private LinearLayout linearLayoutFabButtons;
    private ConstraintLayout constraintLayout;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    private void reload() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        linearLayoutFabButtons = binding.fabButtons;
        hands = binding.imgViewHands;


        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

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
        handLeft.setTranslationY(-300);
        handRight.setTranslationX(300);
        heart.setRotation(360);
        hands.setRotation(-360);

        final float alpha = 0;

        hands.setAlpha(alpha);
        heart.setAlpha(alpha);
        facebook.setAlpha(alpha);
        google.setAlpha(alpha);
        twitter.setAlpha(alpha);
        tabLayout.setAlpha(alpha);
        apple.setAlpha(alpha);
        handLeft.setAlpha(alpha);
        handRight.setAlpha(alpha);

        hands.animate().rotation(0).alpha(1).setDuration(1000).setStartDelay(350).start();
        heart.animate().rotation(0).alpha(1).setDuration(1000).setStartDelay(350).start();
        handRight.animate().translationX(80).alpha(1).setDuration(1000).setStartDelay(300).start();
        handLeft.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        facebook.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        apple.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        twitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

        facebook.setOnClickListener(this);
        google.setOnClickListener(this);
        apple.setOnClickListener(this);
        twitter.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(this);
        binding.root.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    @Override
    public void onTabSelected(@NonNull TabLayout.Tab tab) {
        int pos = tab.getPosition();
        viewPager.setCurrentItem(pos);
        switch (pos) {
            case 1:
            case 2:
//                linearLayoutFabButtons.setTranslationY(300);
//                linearLayoutFabButtons.setAlpha(0);
                break;
            default:
                linearLayoutFabButtons.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

                break;
        }
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
    public void onClick(@NonNull View view) {

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

    public static void hideKeyboard(@NonNull Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        final View activityRootView = binding.root;
        activityRootView.getWindowVisibleDisplayFrame(rect);

        int heightDiff = binding.getRoot().getHeight() - (rect.bottom - rect.top);
        if (heightDiff > 100) {
            constraintLayout.setTranslationY(-370);
        } else {
            constraintLayout.setTranslationY(0);
        }
    }

    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }


}