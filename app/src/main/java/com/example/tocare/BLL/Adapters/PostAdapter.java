package com.example.tocare.BLL.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tocare.BLL.Model.Task;

import com.example.tocare.DAL.Data;
import com.example.tocare.R;

import com.example.tocare.UIL.CommentsActivity;
import com.example.tocare.UIL.Fragment.ProfileFragment;
import com.example.tocare.UIL.TaskDetailsActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Task> mTask;
    private final Data localData;


    public PostAdapter(Context mContext, List<Task> mTask) {
        this.mContext = mContext;
        this.mTask = (mTask == null) ? new ArrayList<>() : mTask;
        localData = Data.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task task = mTask.get(position);


        Picasso.get().load(task.getImagesUrl().get(0)).fit().into(holder.post_image);
        Picasso.get().load(localData.getCurrentUser().getImageUrl()).fit().into(holder.currentUserImage);

        Animation slideInRight = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
        Animation slideOutLeft = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_left);

        holder.viewFlipper.setInAnimation(slideInRight);
        holder.viewFlipper.setOutAnimation(slideOutLeft);

        if (task.getImagesUrl().size() > 1)
            holder.viewFlipper.setOnClickListener(new View.OnClickListener() {
                int index = 1;

                @Override
                public void onClick(View view) {
                    Picasso.get().load(task.getImagesUrl().get(index)).fit().into(holder.post_image);
                    index = (index + 1) % task.getImagesUrl().size();
                }
            });

        if (task.getDescription().equals("")) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(task.getDescription());
        }

        holder.details.setOnClickListener(v -> {

            SharedPreferences.Editor editor = mContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit();
            editor.putString("postId", task.getTaskId());
            editor.apply();
            Intent intent = new Intent(mContext, TaskDetailsActivity.class);
            mContext.startActivity(intent);
        });

        localData.getUserById(task.getAuthor(), (success, userModel) -> {
            if (success) {
                holder.username.setText(userModel.getUserName());
                holder.publisher.setText(userModel.getName());
                Picasso.get().load(userModel.getImageUrl()).into(holder.image_profile);
            }
        });

        holder.username.setOnClickListener(v -> goToProfile(task.getAuthor()));

        holder.image_profile.setOnClickListener(v -> goToProfile(task.getAuthor()));

        holder.like.setOnClickListener(v -> {
            if (holder.like.getTag().equals("like")) {
                localData.addLikeToPost(task.getTaskId());
            } else {
                localData.deleteLikeFromPost(task.getTaskId());
            }
        });


        holder.comment.setOnClickListener(v -> goToCommentsPost(task.getAuthor(), task.getTaskId()));
        holder.comments.setOnClickListener(v -> goToCommentsPost(task.getAuthor(), task.getTaskId()));
        holder.newComment.setOnClickListener(v -> goToCommentsPost(task.getAuthor(), task.getTaskId()));

        holder.currentUserImage.setOnClickListener(v -> goToProfile(localData.getCurrentUserId()));

        holder.timeAgo.setText(task.getCreated());

        if (task.getAuthor().equals(localData.getCurrentUserId())) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.btTask.setVisibility(View.GONE);
            holder.save.setVisibility(View.GONE);
            holder.delete.setOnClickListener(v -> new MaterialAlertDialogBuilder(mContext)
                    .setTitle("Delete post")
                    .setMessage("Are you sure you want to delete the photo?")
                    .setPositiveButton("Continue", (dialogInterface, i) -> localData.deletePost(task.getTaskId()))
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    })
                    .show());
        } else {
            holder.save.setOnClickListener(v -> {
                if (holder.save.getTag().equals("save")) {
                    localData.addSavedItem(task.getTaskId());
                } else {
                    localData.deleteSavedItem(task.getTaskId());
                }
            });
            holder.delete.setVisibility(View.GONE);
            holder.btTask.setVisibility(View.VISIBLE);
            holder.save.setVisibility(View.VISIBLE);
        }

        if (task.getStatus().equals("Active")) {
            holder.statusLamp.setImageResource(R.drawable.ic_active);
            holder.statusText.setText(R.string.active);
            holder.statusText.setTag(R.string.active);
        } else {

            holder.btTask.setImageResource(R.drawable.ic_take_process);
            holder.btTask.setEnabled(false);
            holder.userTake.setVisibility(View.VISIBLE);
            localData.getUserById(task.getTakenByUserId(), (success, userModel) -> holder.userTake.setText(userModel.getUserName()));


            holder.userTake.setOnClickListener(v -> goToProfile(task.getTakenByUserId()));

            if (task.getStatus().equals("In Process")) {
                holder.statusLamp.setImageResource(R.drawable.ic_process);
                holder.statusLamp.setTag(R.string.In_Process);
                holder.statusText.setText(R.string.In_Process);
                holder.statusText.setTag(R.string.In_Process);
            } else {
                holder.statusLamp.setImageResource(R.drawable.ic_done);
                holder.statusText.setText(R.string.done);
                holder.statusLamp.setTag(R.string.done);
                holder.statusText.setTag(R.string.done);
            }
        }

        if (task.getTakenByUserId().equals(""))
            holder.btTask.setOnClickListener(v ->
                    new MaterialAlertDialogBuilder(mContext)
                            .setTitle("Performing a task")
                            .setMessage("Do you want to take the task in exchange for " + task.getBid() + " coins?")
                            .setPositiveButton("Continue", (dialogInterface, i) ->
                                    localData.takeATaskByUser(task.getTaskId(), new HashMap<String, Object>() {{
                                        put("takenByUserName", localData.getCurrentUser().getName() + " " + localData.getCurrentUser().getLastName());
                                        put("takenByUserId", localData.getCurrentUserId());
                                        put("status", "In Process");
                                    }}))
                            .setNegativeButton("Cancel", (dialogInterface, i) -> {
                            })
                            .show());

        localData.getLikeByPostId(holder.count_likes, task.getTaskId());
        localData.isLiked(holder.like, task.getTaskId());
        localData.isSaved(holder.save, task.getTaskId());
        localData.getCommentByPostId(holder.comments, task.getTaskId());

    }

    private void goToProfile(String userId) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        editor.putString("profileId", userId);
        editor.putBoolean("fromManage", false);
        editor.apply();
        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
    }

    private void goToCommentsPost(String publisher, String postId) {
        Intent intent = new Intent(mContext, CommentsActivity.class);
        intent.putExtra("postId", postId);
        intent.putExtra("publish", publisher);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mTask.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image_profile, post_image, like, comment, save, delete, statusLamp, btTask, currentUserImage;
        private final TextView username, count_likes, description, publisher, comments, userTake, statusText, timeAgo, newComment,details;
        private final ViewFlipper viewFlipper;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            delete = itemView.findViewById(R.id.bt_delete);
            statusLamp = itemView.findViewById(R.id.status_lamp);
            btTask = itemView.findViewById(R.id.take_task);
            currentUserImage = itemView.findViewById(R.id.current_image_profile);

            username = itemView.findViewById(R.id.username);
            count_likes = itemView.findViewById(R.id.count_like);
            description = itemView.findViewById(R.id.description);
            publisher = itemView.findViewById(R.id.publisher);
            comments = itemView.findViewById(R.id.comments);
            viewFlipper = itemView.findViewById(R.id.viewFlipper);
            userTake = itemView.findViewById(R.id.tv_user_name_take);
            statusText = itemView.findViewById(R.id.status_text);
            timeAgo = itemView.findViewById(R.id.time_ago);
            newComment = itemView.findViewById(R.id.add_a_comment);
            details = itemView.findViewById(R.id.tv_details);


        }
    }

}