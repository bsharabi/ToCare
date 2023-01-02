package com.example.tocare.UIL.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tocare.BLL.Adapters.PostAdapter;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.Controller.LoginActivity;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;

import java.util.ArrayList;

import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private ImageView logout, inbox;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private Data localData;
    private List<Task> mTask;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mTask = new ArrayList<>();
        localData = Data.getInstance();
        logout = view.findViewById(R.id.logout);
        inbox = view.findViewById(R.id.inbox);
        recyclerView = view.findViewById(R.id.recycler_view);


        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setHasFixedSize(true);
        adapter = new PostAdapter(getContext(), mTask);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        inbox.setOnClickListener(this);
        logout.setOnClickListener(this);
        localData.getAllPost(mTask, () -> adapter.notifyDataSetChanged());

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.logout:
                Data.getInstance().destroy();
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.inbox:
                Toast.makeText(getActivity(), "Inbox", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}