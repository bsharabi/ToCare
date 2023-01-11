package com.example.tocare.UIL.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.R;

import java.util.ArrayList;
import java.util.List;


public class FollowFragment extends Fragment {

    private RecyclerView recycler_view_following, recycler_view_followers;
    private TextView all_following;
    private TextView all_followers;
    private List<UserModel> mFollowing;
    private List<UserModel> mFollowers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        assert getArguments() != null;
        String clickOn = getArguments().getString("clickOn");
        String name = getArguments().getString("name");
        boolean fromManage = getArguments().getBoolean("fromManage");

        final int fragment_container = (fromManage) ? R.id.fragment_container_manage : R.id.fragment_container;

        recycler_view_following = view.findViewById(R.id.recycler_view_following);
        recycler_view_followers = view.findViewById(R.id.recycler_view_followers);
        TextView userName = view.findViewById(R.id.userName);
        all_following = view.findViewById(R.id.all_following);
        all_followers = view.findViewById(R.id.all_followers);
        mFollowing = new ArrayList<>();
        mFollowers = new ArrayList<>();

        userName.setText(name);

        if (clickOn.equals("Followers")) {
            all_followers.setTextColor(getResources().getColor(R.color.orange_500));
            recycler_view_followers.setVisibility(View.VISIBLE);
        } else {
            all_following.setTextColor(getResources().getColor(R.color.orange_500));
            recycler_view_following.setVisibility(View.VISIBLE);
        }


        Toolbar toolbar = view.findViewById(R.id.toolbar_follow);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(fragment_container, new ProfileFragment()).commit());


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        all_followers.setOnClickListener(v -> changeSelect());
        all_following.setOnClickListener(v -> changeSelect());

    }

    private void changeSelect() {
        int colorInt = all_followers.getCurrentTextColor();
        all_followers.setTextColor(all_following.getCurrentTextColor());
        all_following.setTextColor(colorInt);

        int visibility = recycler_view_followers.getVisibility();
        recycler_view_followers.setVisibility(recycler_view_following.getVisibility());
        recycler_view_following.setVisibility(visibility);

    }
}