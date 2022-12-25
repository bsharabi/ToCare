package com.example.tocare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.Task;
import com.example.tocare.BLL.Departments.Tasks;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private ActivityMainBinding binding;
    private Map<String, UserModel> users;
    private Map<String, List<Task>> tasks;
    private Map<String, ListenerRegistration> listenerRegistrationTasks;
    private Map<String, ListenerRegistration> listenerRegistrationUser;
    private static final String TAG = "MainActivity";
    private NavController navController;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;

    //לטפל בחיבור של יוזר
    //לטפל בבר עליון
    //טעינת משימות ולחשוב איך לפצל לפונקציות
    // קודם אני מסדר חיבור אחרי זה רישום יוזר ואז יצירת משימות
    //לעשות גם למשימות ההזנות כאלה
    //הכל להפריד לפונקציות
    //כתבתי על הלוח דברים
    //רשימה טלפון
    //מבנה נתונים טוב יותר
    //להוסיף יוצר ועוד דברים למשימות
    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            db = FirebaseFirestore.getInstance();
            updateUI();
        } else {
            Log.d(TAG, "CurrentUser:null");
            reload(LoginActivity.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        MaterialToolbar materialToolbar = binding.topAppBar;
        BottomNavigationView navView = binding.navView;
//        setSupportActionBar(materialToolbar);

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

                iterator =listenerRegistrationTasks.values().iterator();
                while (iterator.hasNext())
                    iterator.next().remove();
                FirebaseAuth.getInstance().signOut();
                reload(LoginActivity.class);
                return true;
            case "Manage users":
                reload(ManageUsersActivity.class,Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
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

    public void reload(Class<?> name,int flag) {
        Intent intent = new Intent(MainActivity.this, name);
        intent.setFlags(flag);
        Gson gson = new Gson();
        Admin currentAdmin = (Admin) users.get(FirebaseAuth.getInstance().getUid());
        String usersToGson = gson.toJson(currentAdmin);
        System.out.println(usersToGson);
        intent.putExtra("currentAdmin",usersToGson);
        startActivity(intent);
    }
    @Override
    public void onClick(View view) {

    }


    public void updateUI() {

        db.collection("User")
                .document(firebaseUser.getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> map = document.getData();
                            if ((boolean) map.get("admin") == true) {
                                users.put(firebaseUser.getUid(), document.toObject(Admin.class));
                                updateAdminUI();
                            } else {
                                users.put(firebaseUser.getUid(), document.toObject(User.class));
                                updateUserUI();
                            }
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });

        db.collection("Task")
                .document(firebaseUser.getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> map = document.getData();
                            tasks.put(task.getResult().getId(), (List) map.get(task.getResult().getId()));
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
    }

    private void updateAdminUI() {
        DocumentReference reference = db
                .collection("User")
                .document(firebaseUser.getUid());
        ListenerRegistration mainReference = reference
                .addSnapshotListener((value, error) -> {
                    users.put(firebaseUser.getUid(), value.toObject(Admin.class));
                    Log.d(TAG, "DocumentReferenceCurrentUser:success");
                    int reload = navController.getCurrentDestination().getId();
                    navController.navigate(reload);
                });

        listenerRegistrationUser.put(firebaseUser.getUid(), mainReference);

        DocumentReference referenceTask = db
                .collection("Task")
                .document(firebaseUser.getUid());
        ListenerRegistration mainReferenceTask = referenceTask
                .addSnapshotListener((value, error) -> {
                    Map taskList = value.getData();
                    tasks = taskList;

                    Log.d(TAG, "DocumentReferenceCurrentUser:success");
                    int reload = navController.getCurrentDestination().getId();
                    navController.navigate(reload);
                });
        listenerRegistrationTasks.put(firebaseUser.getUid(), mainReferenceTask);
    }

    private void updateUserUI() {
        DocumentReference reference = db
                .collection("User")
                .document(firebaseUser.getUid());
        ListenerRegistration mainReference = reference
                .addSnapshotListener((value, error) -> {
                    users.remove(firebaseUser.getUid());
                    users.put(firebaseUser.getUid(), value.toObject(User.class));
                    Log.d(TAG, "DocumentReferenceCurrentUser:success");
                    int reload = navController.getCurrentDestination().getId();
                    navController.navigate(reload);
                });

        listenerRegistrationUser.put(firebaseUser.getUid(), mainReference);


        DocumentReference referenceTask = db
                .collection("Task")
                .document(firebaseUser.getUid());
        ListenerRegistration mainReferenceTask = referenceTask
                .addSnapshotListener((value, error) -> {
//                    tasks.remove(firebaseUser.getUid());
                    if(value.getData()!=null) {
                        Map map = value.toObject(Map.class);
                        System.out.println(map);
                        List l = (List) map.get(0);
                    System.out.println("-------------------------------------------"+l);
                    }
//                    tasks.put(firebaseUser.getUid(), tasksList);
                    Log.d(TAG, "DocumentReferenceCurrentUser:success");
                    int reload = navController.getCurrentDestination().getId();
                    navController.navigate(reload);
                });
        listenerRegistrationTasks.put(firebaseUser.getUid(), mainReferenceTask);
    }

    public UserModel getCurrentUser() {
        return users.get(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public Map<String, List<Task>> getTasks() {
        return tasks;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
        binding = null;

    }
}