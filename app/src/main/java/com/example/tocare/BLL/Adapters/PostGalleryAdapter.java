package com.example.tocare.BLL.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tocare.BLL.Model.Task;
import com.example.tocare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostGalleryAdapter extends RecyclerView.Adapter<PostGalleryAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Task> mPost;



    public PostGalleryAdapter(Context mContext, List<Task> mTask) {
        this.mContext = mContext;
        this.mPost = (mTask == null) ? new ArrayList<>() : mTask;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_profile_item, parent, false);
        return new PostGalleryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task task = mPost.get(position);
        Picasso.get().load(task.getImagesUrl().get(0)).into(holder.image_profile);


    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image_profile;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.post_image);

        }
    }

}