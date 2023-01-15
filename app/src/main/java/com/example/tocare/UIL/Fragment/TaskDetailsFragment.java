package com.example.tocare.UIL.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.tocare.BLL.Model.Message;
import com.example.tocare.BLL.Model.Notification;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;
import com.example.tocare.UIL.CommentsActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class TaskDetailsFragment extends Fragment {

    private ImageView image_profile, post_image, like, comment, save, delete, statusLamp, btTask, currentUserImage;
    private TextView username, count_likes, description, publisher, comments, userTake, statusText, timeAgo, newComment, edit, cancel, done;
    private ViewFlipper viewFlipper;
    private Data localData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_details, container, false);
        // Inflate the layout for this fragment
        Toolbar toolbar = view.findViewById(R.id.toolbar_task);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String postId = sharedPreferences.getString("postId", null);

        localData = Data.getInstance();
        localData.getPostById(postId, (success, task) -> {
            if (success) {
                uploadUi(task);
            } else {
                requireActivity().finish();
            }
        });

        assert actionBar != null;
        actionBar.setTitle("Edit task");
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());

        image_profile = view.findViewById(R.id.image_profile);
        post_image = view.findViewById(R.id.post_image);
        like = view.findViewById(R.id.like);
        comment = view.findViewById(R.id.comment);
        save = view.findViewById(R.id.save);
        delete = view.findViewById(R.id.bt_delete);
        statusLamp = view.findViewById(R.id.status_lamp);
        btTask = view.findViewById(R.id.take_task);
        currentUserImage = view.findViewById(R.id.current_image_profile);

        username = view.findViewById(R.id.username);
        count_likes = view.findViewById(R.id.count_like);
        description = view.findViewById(R.id.description);
        publisher = view.findViewById(R.id.publisher);
        comments = view.findViewById(R.id.comments);
        viewFlipper = view.findViewById(R.id.viewFlipper);
        userTake = view.findViewById(R.id.tv_user_name_take);
        statusText = view.findViewById(R.id.status_text);
        timeAgo = view.findViewById(R.id.time_ago);
        newComment = view.findViewById(R.id.add_a_comment);
        edit = view.findViewById(R.id.bt_Edit);
        cancel = view.findViewById(R.id.bt_cancel);
        done = view.findViewById(R.id.bt_done);


        return view;
    }

    private void uploadUi(@NonNull Task task) {
        Picasso.get().load(task.getImagesUrl().get(0)).fit().into(post_image);
        Picasso.get().load(localData.getCurrentUser().getImageUrl()).fit().into(currentUserImage);


        if (task.getImagesUrl().size() > 1)
            viewFlipper.setOnClickListener(new View.OnClickListener() {
                int index = 1;

                @Override
                public void onClick(View view) {
                    Picasso.get().load(task.getImagesUrl().get(index)).fit().into(post_image);
                    index = (index + 1) % task.getImagesUrl().size();
                }
            });

        if (task.getDescription().equals("")) {
            description.setVisibility(View.GONE);
        } else {
            description.setVisibility(View.VISIBLE);
            description.setText(task.getDescription());
        }


        localData.getUserById(task.getAuthor(), (success, userModel) -> {
            if (success) {
                username.setText(userModel.getUserName());
                publisher.setText(userModel.getName());
                Picasso.get().load(userModel.getImageUrl()).into(image_profile);
            }
        });

        username.setOnClickListener(v -> goToProfile(task.getAuthor()));

        image_profile.setOnClickListener(v -> goToProfile(task.getAuthor()));

        like.setOnClickListener(v -> {
            if (like.getTag().equals("like")) {
                localData.addLikeToPost(task.getTaskId());
                addNotification("addLike", task.getTaskId(),
                        Message.like, task.getAuthor());
            } else {
                localData.deleteLikeFromPost(task.getTaskId());
            }
        });


        comment.setOnClickListener(v -> goToCommentsPost(task.getAuthor(), task.getTaskId()));
        comments.setOnClickListener(v -> goToCommentsPost(task.getAuthor(), task.getTaskId()));
        newComment.setOnClickListener(v -> goToCommentsPost(task.getAuthor(), task.getTaskId()));

        currentUserImage.setOnClickListener(v -> goToProfile(localData.getCurrentUserId()));

        timeAgo.setText(task.getCreated());
        if(task.getTakenByUserId().equals(localData.getCurrentUserId())){
            cancel.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);
        }


        if (task.getAuthor().equals(localData.getCurrentUserId())) {

            cancel.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);

            delete.setVisibility(View.VISIBLE);
            btTask.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            delete.setOnClickListener(v -> new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete post")
                    .setMessage("Are you sure you want to delete the photo?")
                    .setPositiveButton("Continue", (dialogInterface, i) -> localData.deletePost(task.getTaskId()))
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    })
                    .show());
        } else {
            save.setOnClickListener(v -> {
                if (save.getTag().equals("save")) {
                    localData.addSavedItem(task.getTaskId());
                    addNotification("addSave", task.getTaskId(),
                            Message.save, task.getAuthor());
                } else {
                    localData.deleteSavedItem(task.getTaskId());
                }
            });
            delete.setVisibility(View.GONE);
            btTask.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
        }

        if (task.getStatus().equals("Active")) {
            statusLamp.setImageResource(R.drawable.ic_active);
            statusText.setText(R.string.active);
            statusText.setTag(R.string.active);
        } else {

            btTask.setImageResource(R.drawable.ic_take_process);
            btTask.setEnabled(false);
            userTake.setVisibility(View.VISIBLE);
            localData.getUserById(task.getTakenByUserId(), (success, userModel) -> userTake.setText(userModel.getUserName()));


            userTake.setOnClickListener(v -> goToProfile(task.getTakenByUserId()));

            if (task.getStatus().equals("In Process")) {
                statusLamp.setImageResource(R.drawable.ic_process);
                statusLamp.setTag(R.string.In_Process);
                statusText.setText(R.string.In_Process);
                statusText.setTag(R.string.In_Process);
            } else {
                statusLamp.setImageResource(R.drawable.ic_done);
                statusText.setText(R.string.done);
                statusLamp.setTag(R.string.done);
                statusText.setTag(R.string.done);
            }
        }

        if (task.getTakenByUserId().equals(""))
            btTask.setOnClickListener(v ->
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Performing a task")
                            .setMessage("Do you want to take the task in exchange for " + task.getBid() + " coins?")
                            .setPositiveButton("Continue", (dialogInterface, i) -> {
                                localData.takeATaskByUser(task.getTaskId(), new HashMap<String, Object>() {{
                                    put("takenByUserName", localData.getCurrentUser().getName() + " " + localData.getCurrentUser().getLastName());
                                    put("takenByUserId", localData.getCurrentUserId());
                                    put("status", "In Process");
                                }});
                                addNotification("takeTask", task.getTaskId(),
                                        Message.getTask, task.getAuthor());
                            })
                            .setNegativeButton("Cancel", (dialogInterface, i) -> {
                            })
                            .show());

        localData.getLikeByPostId(count_likes, task.getTaskId());
        localData.isLiked(like, task.getTaskId());
        localData.isSaved(save, task.getTaskId());
        localData.getCommentByPostId(comments, task.getTaskId());
    }

    private void addNotification(String type, String postId, String msg, String author) {

        String notificationId = localData.getRandomIdByCollectionName("Notification");
        localData.addNotification(new Notification(
                notificationId,
                type,
                postId,
                msg,
                false,
                "",
                localData.getCurrentUserId(),
                author));
    }

    private void goToProfile(String userId) {
        SharedPreferences.Editor editor = requireContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        editor.putString("profileId", userId);
        editor.putBoolean("fromManage", false);
        editor.apply();
        ((FragmentActivity) requireContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
    }

    private void goToCommentsPost(String publisher, String postId) {
        Intent intent = new Intent(requireContext(), CommentsActivity.class);
        intent.putExtra("postId", postId);
        intent.putExtra("publish", publisher);
        requireContext().startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        localData.destroyListener("2");
    }
}