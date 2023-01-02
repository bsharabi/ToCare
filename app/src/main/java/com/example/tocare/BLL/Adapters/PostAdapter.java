package com.example.tocare.BLL.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tocare.BLL.Model.Task;

import com.example.tocare.DAL.Data;
import com.example.tocare.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task task = mTask.get(position);

        Picasso.get().load(task.getImagesUrl().get(0)).into(holder.post_image);


        if (task.getAuthor().equals(localData.getCurrentUserId())) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }


        if (holder.description.getText().equals("")) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setText(task.getDescription());
        }

        localData.getUserById(task.getAuthor(), (success, userModel) -> {
            if (success) {
                holder.username.setText(userModel.getUserName());
                holder.publisher.setText(userModel.getName());
                Picasso.get().load(userModel.getImageUrl()).into(holder.image_profile);
            }
        });


        holder.like.setOnClickListener(v -> {
            if (holder.like.getTag().equals("like")) {
                localData.addLikeToTask(task.getTaskId());
            } else {
                localData.deleteLikeFromTask(task.getTaskId());
            }
        });

        holder.save.setOnClickListener(v -> {
            if (holder.save.getTag().equals("save")) {
                localData.addSavedItem(task.getTaskId());
            } else {
                localData.deleteSavedItem(task.getTaskId());
            }
        });

        holder.comment.setOnClickListener(v -> {
//        holder.comments.setText(task.getDescription());

        });
        holder.delete.setOnClickListener(v -> {
            localData.deletePost(task.getTaskId());
        });
        localData.getLikeByPostId(holder.count_likes, task.getTaskId());
        localData.isLiked(holder.like, task.getTaskId());
        localData.isSaved(holder.save, task.getTaskId());

    }

    @Override
    public int getItemCount() {
        return mTask.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image_profile, post_image, like, comment, save, delete;
        private final TextView username, count_likes, description, publisher, comments;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            delete = itemView.findViewById(R.id.bt_delete);

            username = itemView.findViewById(R.id.username);
            count_likes = itemView.findViewById(R.id.count_like);
            description = itemView.findViewById(R.id.description);
            publisher = itemView.findViewById(R.id.publisher);
            comments = itemView.findViewById(R.id.comments);


        }
    }

}