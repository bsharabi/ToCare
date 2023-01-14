package com.example.tocare.UIL.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tocare.BLL.Adapters.UserAdapter;

import com.example.tocare.BLL.Model.User;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;
import com.example.tocare.UIL.signup.SignupChildFragment;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> mUser;
    private Data localData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        localData = Data.getInstance();
        mUser = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view_users);
        ImageView addUser = view.findViewById(R.id.add_user);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());

        addUser.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_manage, new SignupChildFragment()).commit());
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new UserAdapter(getContext(), mUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        localData.getAllChildren(mUser, () -> adapter.notifyDataSetChanged());

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        localData.destroyListener("");
    }
}