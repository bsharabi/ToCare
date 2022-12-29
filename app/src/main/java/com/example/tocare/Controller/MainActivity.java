package com.example.tocare.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tocare.R;
import com.example.tocare.DAL.Data;
import com.example.tocare.BLL.Listener.FirebaseCallback;
import com.example.tocare.BLL.Listener.Refresh;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.tocare.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, FirebaseCallback , Refresh {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private NavController navController;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private BottomNavigationView navView;
    private Data localData;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            initDataStructure();
        } else {
            Log.d(TAG, "CurrentUser:null");
            reload(LoginActivity.class);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = binding.progressBar;
        progressBar.setVisibility(View.VISIBLE);

        MaterialToolbar materialToolbar = binding.topAppBar;
        navView = binding.navView;
//      setSupportActionBar(materialToolbar);
//      navView.setOnItemSelectedListener(this);

        navView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> System.out.println("layout"));
        materialToolbar.setOnMenuItemClickListener(this);
//      materialToolbar.setNavigationOnClickListener(this);
//      Passing each menu ID as a set of Ids because each
//      menu should be considered as top level destinations.
//      AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_profile, R.id.navigation_task, R.id.navigation_feed,
//                R.id.navigation_notifications, R.id.navigation_calender)
//                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//      NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    private void initDataStructure() {
        try {
            localData = Data.getInstance();
            localData.buildUser(this);
            localData.setRefresh(this);
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onMenuItemClick(@NonNull MenuItem item) {
        switch (item.toString()) {
            case "Log Out":
                localData.destroyListenerRegistration();
                reload(LoginActivity.class);
                return true;
            case "Manage users":
                if (localData.isAdmin()) {
                    reload(ManageUsersActivity.class, Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                } else {
                    Toast.makeText(this, "Just Admin", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return false;
        }
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
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("-----------------------onDestroy------------");
        localData.destroyListenerRegistration();
    }

    @Override
    public void onCallback(boolean success, Exception e, FirebaseUser user) {
        if (success) {
            progressBar.setVisibility(View.GONE);
            System.out.println(Data.getInstance());
        } else {
            reload(LoginActivity.class);
        }
    }

    @Override
    public void onSuccess(boolean success, Exception e) {

    }

    @Override
    public void onComplete(boolean success, Exception e) {

    }

    @Override
    public void refresh() {
//        int reload = navController.getCurrentDestination().getId();
//        navController.navigate(reload);
    }
}