package com.example.tocare.BLL.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tocare.BLL.Listener.FollowCallback;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;
import com.example.tocare.UIL.Fragment.ProfileFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> {

    private final Context mContext;
    private final List<UserModel> mUser;
    private final Data localData;

    public UserSearchAdapter(Context mContext, List<UserModel> users) {
        this.mContext = mContext;
        this.mUser = users;
        localData = Data.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserModel user = mUser.get(position);

        if (user.getId().equals(localData.getCurrentUser().getId()))
            holder.tv_follow.setVisibility(View.GONE);
        else
            holder.tv_follow.setVisibility(View.VISIBLE);

        holder.tv_username.setText(user.getUserName());
        holder.tv_full_name.setText(user.getFullName());

        Picasso.get().load(user.getImageUrl()).into(holder.profileImage);

        Boolean isFollowing = localData.getFollowing().containsKey(user.getId());
        if (isFollowing) {
            holder.tv_follow.setText(R.string.Following);
        } else {
            holder.tv_follow.setText(R.string.Follow);
        }

        holder.itemView.setOnClickListener(v -> {
            SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
            editor.putString("profileId", user.getId());
            editor.apply();
            ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        });

        holder.tv_follow.setOnClickListener(v -> {
            if (holder.tv_follow.getText().toString().equals("Follow")) {
                localData.addFollow(user.getId(), holder.tv_follow);
            } else {
                localData.deleteFollow( user.getId(), holder.tv_follow);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView profileImage;
        private final TextView tv_username;
        private final TextView tv_full_name;
        private final TextView tv_follow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.image_profile);
            tv_username = itemView.findViewById(R.id.username);
            tv_full_name = itemView.findViewById(R.id.full_name);
            tv_follow = itemView.findViewById(R.id.btn_follow);
        }
    }
}