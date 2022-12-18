package com.example.tocare.UIL.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.User;
import com.example.tocare.BLL.Departments.UserModel;
import com.example.tocare.DLL.DataBase;
import com.example.tocare.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tocare.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private ActivityMainBinding binding;
    public static UserModel currentUser;
    private static final String TAG = "MainActivity";


    @Override
    protected void onStart() {
        super.onStart();
        updateUIAdmin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MaterialToolbar materialToolbar = binding.topAppBar;
        BottomNavigationView navView = binding.navView;

        navView.setOnItemSelectedListener(this);
        materialToolbar.setOnMenuItemClickListener(this);
        materialToolbar.setNavigationOnClickListener(this);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile, R.id.navigation_task, R.id.navigation_feed,
                R.id.navigation_notifications, R.id.navigation_calender)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.toString().equals("Profile"))
            binding.topAppBar.setVisibility(View.VISIBLE);
        else
            binding.topAppBar.setVisibility(View.INVISIBLE);
        return false;

    }

    @Override
    public boolean onMenuItemClick(@NonNull MenuItem item) {
        Intent intent;
        switch (item.toString()) {
            case "Log Out":
                intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            case "Manage users":
                intent = new Intent(MainActivity.this, ManageUsersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return false;
        }
    }

    private void nextActivity(Class name) {
        Intent intent = new Intent(MainActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

    }

    public void updateUIAdmin() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            DocumentReference reference = DataBase.getInstance().getFireStore().collection("User")
                    .document(firebaseUser.getUid());
            System.out.println(firebaseUser.getUid());
            System.out.println("-----------------------------------------------------------");
            System.out.println(reference);
            reference.addSnapshotListener((value, error) -> {
                currentUser = value.toObject(Admin.class);
                Log.d(TAG, "DocumentReference:success");
            });
        } else {
            Log.d(TAG, "DocumentReference:failed");
        }
    }

    private void updateUIUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            DocumentReference reference = DataBase.getInstance().getFireStore().collection("User")
                    .document(firebaseUser.getUid());
            System.out.println(firebaseUser.getUid());
            reference.addSnapshotListener((value, error) -> {
                currentUser = value.toObject(User.class);
                Log.d(TAG, "DocumentReference:success");
            });
        } else {
            Log.d(TAG, "DocumentReference:failed");
        }
    }
//where
    public UserModel getCurrentUser() {
        return currentUser;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();

    }
}