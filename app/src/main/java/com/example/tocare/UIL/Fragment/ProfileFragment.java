package com.example.tocare.UIL.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tocare.BLL.Adapters.PostGalleryAdapter;

import com.example.tocare.BLL.Adapters.SavedGalleryAdapter;
import com.example.tocare.BLL.Adapters.TasksGalleryAdapter;
import com.example.tocare.BLL.Listener.UserCallback;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.Controller.ManageUsersActivity;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;
import com.example.tocare.UIL.EditProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment implements UserCallback {

    private TextView userName, tasksCount, followers, following, fullName, bio;
    private Button bt_follow;
    private ImageButton posts, saved, tasks;
    private ImageView options, manage, image_profile, plus;
    private RecyclerView recyclerView_post, recyclerView_saved, recyclerView_tasks;
    private Data localData;
    private ScrollView scrollView;
    private ProgressBar progressBar;
    private String profileId;
    private LinearLayout getFollowing, getFollowers;
    private boolean fromManage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        localData = Data.getInstance();
        userName = view.findViewById(R.id.tv_username);
        scrollView = view.findViewById(R.id.scrollView);
        progressBar = view.findViewById(R.id.progress_bar_p);
        plus = view.findViewById(R.id.plus);
        tasksCount = view.findViewById(R.id.count_tasks);
        followers = view.findViewById(R.id.count_followers);
        following = view.findViewById(R.id.count_following);
        fullName = view.findViewById(R.id.full_name_profile);
        bio = view.findViewById(R.id.bio_profile);
        bt_follow = view.findViewById(R.id.bt_follow);
        posts = view.findViewById(R.id.all_post);
        saved = view.findViewById(R.id.saved);
        tasks = view.findViewById(R.id.all_task);
        options = view.findViewById(R.id.im_options);
        manage = view.findViewById(R.id.im_manage);
        image_profile = view.findViewById(R.id.im_profile);
        getFollowing = view.findViewById(R.id.get_following);
        getFollowers = view.findViewById(R.id.get_followers);
        recyclerView_post = view.findViewById(R.id.recycler_view_post);
        recyclerView_tasks = view.findViewById(R.id.recycler_view_tasks);
        recyclerView_saved = view.findViewById(R.id.recycler_view_saved);
        scrollView.setVisibility(View.GONE);

        return view;
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = sharedPreferences.getString("profileId", null);
        fromManage = sharedPreferences.getBoolean("fromManage", false);

        if (fromManage) {
            options.setImageResource(R.drawable.ic_back);
            options.setOnClickListener(v -> requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_manage, UsersFragment.class,null).commit());
        }

        localData.getUserById(profileId, this);

        tasks.setOnClickListener(v -> setRecyclerViewVisibility(recyclerView_tasks));
        saved.setOnClickListener(v -> setRecyclerViewVisibility(recyclerView_saved));
        posts.setOnClickListener(v -> setRecyclerViewVisibility(recyclerView_post));

        getFollowing.setOnClickListener(v -> goToFollow("Following"));
        getFollowers.setOnClickListener(v -> goToFollow("Followers"));

        List<Task> mTask = new ArrayList<>();
        List<Task> mPost = new ArrayList<>();
        List<Task> mSave = new ArrayList<>();


        localData.getAllPostsByUserId(profileId, mPost, tasksCount, () -> Objects.requireNonNull(recyclerView_post.getAdapter()).notifyDataSetChanged());
        localData.getAllSavedByUserId(profileId, mSave, () -> Objects.requireNonNull(recyclerView_saved.getAdapter()).notifyDataSetChanged());
        localData.getAllTasksByUserId(profileId, mTask, () -> Objects.requireNonNull(recyclerView_tasks.getAdapter()).notifyDataSetChanged());

        localData.getAllFollowingByUserId(profileId, following);
        localData.getAllFollowersByUserId(profileId, followers);

        setRecyclerView(recyclerView_saved, new SavedGalleryAdapter(getContext(), mSave));
        setRecyclerView(recyclerView_tasks, new TasksGalleryAdapter(getContext(), mTask));
        setRecyclerView(recyclerView_post, new PostGalleryAdapter(getContext(), mPost));

    }

    private void setRecyclerViewVisibility(RecyclerView recyclerView) {
        recyclerView_saved.setVisibility(View.GONE);
        recyclerView_post.setVisibility(View.GONE);
        recyclerView_tasks.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void setRecyclerView(@NonNull RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);
    }

    private void goToFollow(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("clickOn", str);
        bundle.putString("name", userName.getText().toString());
        bundle.putBoolean("fromManage", fromManage);
        final int fragment_container = (fromManage) ? R.id.fragment_container_manage : R.id.fragment_container;
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(fragment_container, FollowFragment.class, bundle).commit();
    }

    public void reload(Class<?> name) {
        Intent intent = new Intent(getContext(), name);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localData.destroyListener("");
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void result(boolean success, UserModel userModel) {

        //if this is current user
        if (success) {
            userName.setText(userModel.getUserName());
            fullName.setText(userModel.getName() + " " + userModel.getLastName());
            bio.setText(userModel.getBio());
            Picasso.get().load(userModel.getImageUrl()).into(image_profile);
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

            boolean isCurrentUser = localData.getCurrentUserId().equals(profileId);
            boolean isMyChild = localData.isMyChild(profileId);
            if (isCurrentUser || isMyChild) {
                if (isCurrentUser && localData.getCurrentUser().isAdmin()) {
                    manage.setOnClickListener(v -> reload(ManageUsersActivity.class));
                    manage.setVisibility(View.VISIBLE);
                }
                plus.setVisibility(View.VISIBLE);
                bt_follow.setText(R.string.editProfile);
                bt_follow.setOnClickListener(v -> reload(EditProfileActivity.class));
            } else {
                boolean isFollowing = localData.getFollowing().containsKey(profileId);
                if (isFollowing) {
                    bt_follow.setText(R.string.Following);
                } else {
                    bt_follow.setText(R.string.Follow);
                }
                plus.setVisibility(View.GONE);
                image_profile.setEnabled(false);
                saved.setVisibility(View.GONE);
                bt_follow.setOnClickListener(v -> {
                    if (bt_follow.getText().toString().equals("Follow")) {
                        localData.addFollow(profileId, bt_follow);
                    } else {
                        localData.deleteFollow(profileId, bt_follow);
                    }
                });
            }
        } else {
            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        }
    }

}