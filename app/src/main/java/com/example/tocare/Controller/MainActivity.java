package com.example.tocare.Controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.tocare.BLL.Observer.Observe;
import com.example.tocare.R;
import com.example.tocare.DAL.Data;
import com.example.tocare.BLL.Listener.FirebaseCallback;
import com.example.tocare.UIL.Fragment.HomeFragment;
import com.example.tocare.UIL.Fragment.NotificationFragment;
import com.example.tocare.UIL.Fragment.ProfileFragment;
import com.example.tocare.UIL.Fragment.SearchFragment;
import com.example.tocare.UIL.PostActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tocare.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements  FirebaseCallback {

    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment;
    private Data localData;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            initDataStructure();
        } else {
            Log.d(TAG, "CurrentUser:null");
            reload(LoginActivity.class);
        }
        Observe myObserver = Observe.getInstance();
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        selectedFragment = null;

        progressBar = binding.progressBar;
        myObserver.setBottomNavigationView(binding.bottomNavigation);
        bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void initDataStructure() {
        localData = Data.getInstance();
        localData.updateUserUI(this);
    }

    @SuppressLint("NonConstantResourceId")
    private void updateUi() {

        bottomNavigationView.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new HomeFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_heart:
                    selectedFragment = new NotificationFragment();
                    break;
                case R.id.nav_add:
                    selectedFragment = null;
                    reload(PostActivity.class, Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    break;
                case R.id.nav_profile:
                    SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileId", localData.getCurrentUser().getId());
                    editor.putBoolean("fromManage", false);
                    editor.apply();
                    selectedFragment = new ProfileFragment();
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        });
    }


    public void reload(Class<?> name) {
        Intent intent = new Intent(MainActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void reload(Class<?> name, int flag) {
        Intent intent = new Intent(MainActivity.this, name);
        intent.setFlags(flag);
        startActivity(intent);
    }

    @Override
    public void onCallback(boolean success, Exception e, FirebaseUser user) {
        if (success) {
            if (localData.getCurrentUser().isAdmin())
                Observe.getInstance().updateAdmin();
        } else {
            Observe.getInstance().updateChild();
        }
    }

    @Override
    public void onSuccess(boolean success, Exception e) {
        if (success) {
            updateUi();
            progressBar.setVisibility(View.GONE);
        } else {
            reload(LoginActivity.class);
        }
    }

}