package com.example.tocare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.User;
import com.example.tocare.BLL.Departments.UserModel;
import com.example.tocare.UIL.ui.signup.SignupUserFragment;
import com.example.tocare.UIL.ui.users.UsersFragment;
import com.example.tocare.databinding.ActivityManageUsersBinding;
import com.example.tocare.databinding.FragmentSignupUserBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class ManageUsersActivity extends AppCompatActivity {


    private static final String TAG = "ManageUsersActivity";
    private ActivityManageUsersBinding binding;
    private Admin currentAdmin;
    public List<UserModel> mUserObject = new ArrayList<>();


    // בחזרה מפרגמנט לחזור למצב נוכחי
    // סינון יוזריםע ם סרגל שםם משימות וכו
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        binding = ActivityManageUsersBinding.inflate(getLayoutInflater());
        Gson gson = new Gson();
        String usersToObject = getIntent().getStringExtra("currentAdmin");
        currentAdmin = gson.fromJson(usersToObject, Admin.class);
        List<String> mUserString = new ArrayList<String>(currentAdmin.getChildrenId());
        mUserObject = new ArrayList<>();

        for (String userID : mUserString) {

            FirebaseFirestore.getInstance().collection("User")
                    .document(userID).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                mUserObject.add(document.toObject(User.class));
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    });

        }



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, UsersFragment.class, null)
                    .commit();
        }

    }


    public void swapFragmentByFragmentClass(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, fragmentClass, bundle)
                .setReorderingAllowed(true)
                .addToBackStack(fragmentClass.getName())
                .commit();
    }

    public void reload(Class<?> name) {
        Intent intent = new Intent(ManageUsersActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void reload(Class<?> name, int flag) {
        Intent intent = new Intent(ManageUsersActivity.this, name);
        intent.setFlags(flag);
        startActivity(intent);
    }

    public Admin getCurrentAdmin() {
        return currentAdmin;
    }


}