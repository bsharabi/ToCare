package com.example.tocare.UIL.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tocare.BLL.Observer.Observe;
import com.example.tocare.Controller.ManageUsersActivity;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private TextView userName, tasks, followers, following, fullName, bio;
    private Button bt_follow;
    private ImageButton posts, saved;
    private ImageView options, manage, image_profile, plus;
    private RecyclerView recyclerView_post, recyclerView_save;
    private Data localData;
    private ScrollView scrollView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        localData = Data.getInstance();
        userName = view.findViewById(R.id.tv_username);
        scrollView = view.findViewById(R.id.scrollView);
        progressBar = view.findViewById(R.id.progress_bar_p);
        plus = view.findViewById(R.id.plus);
        tasks = view.findViewById(R.id.count_tasks);
        followers = view.findViewById(R.id.count_followers);
        following = view.findViewById(R.id.count_following);
        fullName = view.findViewById(R.id.full_name_profile);
        bio = view.findViewById(R.id.bio_profile);
        bt_follow = view.findViewById(R.id.bt_follow);
        posts = view.findViewById(R.id.all_post);
        saved = view.findViewById(R.id.saved);
        options = view.findViewById(R.id.im_options);
        manage = view.findViewById(R.id.im_manage);
        image_profile = view.findViewById(R.id.im_profile);
        recyclerView_post = view.findViewById(R.id.recycler_view_post);
        recyclerView_save = view.findViewById(R.id.recycler_view_saved);
        scrollView.setVisibility(View.GONE);

        Observe.getInstance().add(manage);

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String profileId = sharedPreferences.getString("profileId", null);

        boolean isChild = sharedPreferences.getBoolean("isChile", false);

        followers.setText("0");
        following.setText("0");

        if (profileId.equals(localData.getCurrentUser().getId())) {
            bt_follow.setText("Edit Profile");
            followers.setText(localData.getFollowers().size() + "");
            following.setText(localData.getFollowing().size() - 1 + "");
            tasks.setText("10");

        } else {
            manage.setVisibility(View.GONE);
            plus.setVisibility(View.GONE);
            image_profile.setEnabled(false);
        }


        localData.getUserById(profileId, (success, userModel) -> {
            if (success) {
                userName.setText(userModel.getUserName());
                fullName.setText(userModel.getFullName());
                bio.setText(userModel.getBio());
                Picasso.get().load(userModel.getImageUrl()).into(image_profile);
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

        if (!bt_follow.getText().toString().equals("Edit Profile")) {
            Boolean isFollowing = localData.getFollowing().containsKey(profileId);
            if (isFollowing) {
                bt_follow.setText(R.string.Following);
            } else {
                bt_follow.setText(R.string.Follow);
            }
        }

        manage.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ManageUsersActivity.class);
            startActivity(intent);
        });

        bt_follow.setOnClickListener(v -> {

            if (bt_follow.getText().toString().equals("Edit Profile")) {


            } else if (bt_follow.getText().toString().equals("Follow")) {
                localData.addFollow(profileId, bt_follow);
            } else {
                localData.deleteFollow(profileId, bt_follow);
            }
        });
    }
}