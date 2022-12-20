package com.example.tocare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.example.tocare.BLL.Adapters.LoginAdapter;
import com.example.tocare.databinding.ActivityLoginBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton facebook, google, github, twitter, phone;
    private ImageView heart, handLeft, handRight, hands;
    private ConstraintLayout constraintLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ProgressDialog dialog;





    @Override
    protected void onStart() {
        super.onStart();
    }

    public void reload(Class<?> name) {
        Intent intent = new Intent(LoginActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (currentUser.isEmailVerified()) {
                reload(MainActivity.class);
            } else {
                FirebaseAuth.getInstance().signOut();
            }
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        constraintLayout = binding.constraintLayout;
        tabLayout = binding.tabLayout;
        viewPager = binding.viewPager;
        facebook = binding.fabFacebook;
        google = binding.fabGoogle;
        github = binding.fabGithub;
        twitter = binding.fabTwitter;
        heart = binding.imgViewHeart;
        handLeft = binding.imgViewLeftHand;
        handRight = binding.imgViewRightHand;
        hands = binding.imgViewHands;
        phone = binding.fabPhone;
        dialog = new ProgressDialog(this);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        viewPager.setUserInputEnabled(false);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


        tabLayout.setTranslationY(300);
        handLeft.setTranslationY(-300);
        handRight.setTranslationX(300);
        heart.setRotation(360);
        hands.setRotation(-360);

        final float alpha = 0;

        hands.setAlpha(alpha);
        heart.setAlpha(alpha);

        tabLayout.setAlpha(alpha);
        handLeft.setAlpha(alpha);
        handRight.setAlpha(alpha);

        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
        hands.animate().rotation(0).alpha(1).setDuration(1000).setStartDelay(350).start();
        heart.animate().rotation(0).alpha(1).setDuration(1000).setStartDelay(350).start();
        handRight.animate().translationX(80).alpha(1).setDuration(1000).setStartDelay(300).start();
        handLeft.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(300).start();

        startAnimationFabUp();

        facebook.setOnClickListener(this);
        google.setOnClickListener(this);
        github.setOnClickListener(this);
        twitter.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(this);
        binding.root.getViewTreeObserver().addOnGlobalLayoutListener(this);
        phone.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("Auth", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void swapFragmentByPosition(int position) {
        viewPager.setCurrentItem(position);
        startAnimationFabDown();
    }

    private void startAnimationFabUp() {

        phone.setTranslationY(300);
        facebook.setTranslationY(300);
        google.setTranslationY(300);
        twitter.setTranslationY(300);

        github.setTranslationY(300);

        final float alpha = 0;


        facebook.setAlpha(alpha);
        google.setAlpha(alpha);
        phone.setAlpha(alpha);
        twitter.setAlpha(alpha);
        github.setAlpha(alpha);

        facebook.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(550).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(650).start();
        phone.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(750).start();
        twitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(850).start();
        github.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(950).start();
    }

    private void startAnimationFabDown() {

        facebook.animate().translationY(300).alpha(1).setDuration(0).setStartDelay(300).start();
        google.animate().translationY(300).alpha(1).setDuration(0).setStartDelay(250).start();
        phone.animate().translationY(300).alpha(1).setDuration(0).setStartDelay(200).start();
        twitter.animate().translationY(300).alpha(1).setDuration(0).setStartDelay(150).start();
        github.animate().translationY(300).alpha(1).setDuration(0).setStartDelay(50).start();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onTabSelected(@NonNull TabLayout.Tab tab) {
        int pos = tab.getPosition();
        hideKeyboard();
        swapFragmentByPosition(pos);
        switch (pos) {
            case 1:
            case 2:
                startAnimationFabDown();
                break;
            default:
                startAnimationFabUp();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.fab_google:
                reload(GoogleLoginActivity.class);
                break;
            case R.id.fab_github:
                reload(GithubLoginActivity.class);
                break;
            case R.id.fab_facebook:
                reload(FacebookLoginActivity.class);
                break;
            case R.id.fab_twitter:
                reload(TwitterLoginActivity.class);
                break;
            case R.id.fab_phone:
                reload(PhoneLoginActivity.class);
                break;
            default:
                break;
        }

    }


    //--------------------------------------------Hide keyBoard -----------------------------------------------------
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

    //------------------------------------------------Getter&&Setter--------------------------------------------

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}