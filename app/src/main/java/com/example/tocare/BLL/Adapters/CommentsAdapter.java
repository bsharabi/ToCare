package com.example.tocare.BLL.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tocare.BLL.Model.Comment;

import com.example.tocare.DAL.Data;
import com.example.tocare.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Comment> mComments;
    private final Data localData;


    public CommentsAdapter(Context mContext, List<Comment> mComments) {
        this.mContext = mContext;
        this.mComments = (mComments == null) ? new ArrayList<>() : mComments;
        localData = Data.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Comment comment = mComments.get(position);

        if (comment.getAuthor().equals(localData.getCurrentUserId())) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }

        holder.comment.setText(comment.getComment());

        localData.getUserById(comment.getAuthor(), (success, userModel) -> {
            if (success) {
                holder.username.setText(userModel.getUserName());
                Picasso.get().load(userModel.getImageUrl()).into(holder.image_profile);
            }
        });

        holder.delete.setOnClickListener(v -> localData.deleteComment(comment.getPostId(), comment.getCommentId()));


    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image_profile, delete;
        private final TextView username, comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            delete = itemView.findViewById(R.id.bt_delete);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);


        }
    }

}