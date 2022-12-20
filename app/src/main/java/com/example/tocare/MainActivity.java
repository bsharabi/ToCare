package com.example.tocare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.Task;
import com.example.tocare.BLL.Departments.User;
import com.example.tocare.BLL.Departments.UserModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.tocare.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private Map<String, UserModel> users;
    private Map<String, Task> tasks;
    private Map<String, ListenerRegistration> listenerRegistrationTasks;
    private Map<String, ListenerRegistration> listenerRegistrationUser;

    private static final String TAG = "MainActivity";
    private NavController navController;

    //לטפל בחיבור של יוזר
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth=FirebaseAuth.getInstance();
        MaterialToolbar materialToolbar = binding.topAppBar;
        BottomNavigationView navView = binding.navView;

        navView.setOnItemSelectedListener(this);
        materialToolbar.setOnMenuItemClickListener(this);
        materialToolbar.setNavigationOnClickListener(this);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_profile, R.id.navigation_task, R.id.navigation_feed,
//                R.id.navigation_notifications, R.id.navigation_calender)
//                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        users = new HashMap<>();
        listenerRegistrationUser = new HashMap<>();
        tasks = new HashMap<>();
        listenerRegistrationTasks = new HashMap<>();
        updateUI();
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
        switch (item.toString()) {
            case "Log Out":
                Iterator<ListenerRegistration> iterator = listenerRegistrationUser.values().iterator();
                while (iterator.hasNext())
                    iterator.next().remove();
                FirebaseAuth.getInstance().signOut();
                reload(LoginActivity.class);
                return true;
            case "Manage users":
                reload(ManageUsersActivity.class);
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

    @Override
    public void onClick(View view) {

    }

    //טעינת משימות ולחשוב איך לפצל לפונקציות
    // קודם אני מסדר חיבור אחרי זה רישום יוזר ואז יצירת משימות
    public void updateUI() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        AtomicBoolean isAdmin = new AtomicBoolean(false);


        if (firebaseUser != null) {
            DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                    .document(firebaseUser.getUid());
            ListenerRegistration temp = reference.addSnapshotListener((value, error) -> {
                UserModel userModel = value.toObject(UserModel.class);
                if (userModel != null)
                    isAdmin.set(userModel.isAdmin());
                if (isAdmin.get())
                    users.put(firebaseUser.getUid(), value.toObject(Admin.class));
                else
                    users.put(firebaseUser.getUid(), value.toObject(User.class));
                Log.d(TAG, "DocumentReferenceCurrentUser:success");
                int reload = navController.getCurrentDestination().getId();
                navController.navigate(reload);
            });
            System.out.println(temp);
            listenerRegistrationUser.put(firebaseUser.getUid(), temp);


        } else {
            Log.d(TAG, "DocumentReferenceCurrentUser:failed");
        }
        if (isAdmin.get()) {

            Iterator<String> iterator = ((Admin) users.get(0)).getChildrenId().keySet().iterator();
            while (iterator.hasNext()) {
                DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                        .document(iterator.next());
                if (reference != null) {
                    ListenerRegistration temp = reference.addSnapshotListener((value, error) -> {
                        users.put(iterator.next(), value.toObject(User.class));
                        Log.d(TAG, "DocumentReferenceUser:success");
                        int reload = navController.getCurrentDestination().getId();
                        navController.navigate(reload);
                    });
                    listenerRegistrationUser.put(iterator.next(), temp);
                } else {
                    Log.d(TAG, "DocumentReferenceUser:failed");
                }
            }
        } else {
            //this is user
        }
        //לעשות גם למשימות ההזנות כאלה
        //הכל להפריד לפונקציות
        //כתבתי על הלוח דברים
        //רשימה טלפון
        //מבנה נתונים טוב יותר
        //להוסיף יוצר ועוד דברים למשימות

    }

    public UserModel getCurrentUser() {
        return users.get(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
        binding = null;

    }
}