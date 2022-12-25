package com.example.tocare.UIL.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tocare.BLL.Adapters.UserAdapter;
import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.User;
import com.example.tocare.BLL.Departments.UserModel;
import com.example.tocare.MainActivity;
import com.example.tocare.ManageUsersActivity;
import com.example.tocare.UIL.ui.signup.SignupUserFragment;
import com.example.tocare.databinding.FragmentUsersBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UsersFragment extends Fragment {

    private static final String TAG = "UsersFragment";
    private FragmentUsersBinding binding;
    private ManageUsersActivity manageUsersActivity;
    private FloatingActionButton addUser;
    private MaterialToolbar materialToolbar;
    private RecyclerView rvUsers;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        manageUsersActivity = (ManageUsersActivity) getActivity();
        rvUsers = binding.idRVUser;
        addUser = binding.fabAddUser;
        materialToolbar = binding.topAppBar;

        materialToolbar.setNavigationOnClickListener(v -> {
            System.out.println("Hello");
            manageUsersActivity.reload(MainActivity.class, Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        });
        addUser.setOnClickListener(v -> {
            System.out.println("Hello");
            manageUsersActivity.swapFragmentByFragmentClass(SignupUserFragment.class, null);
        });


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        UserAdapter usersAdapter = new UserAdapter(getContext(), manageUsersActivity.mUserObject, manageUsersActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvUsers.setLayoutManager(linearLayoutManager);
        rvUsers.setAdapter(usersAdapter);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}