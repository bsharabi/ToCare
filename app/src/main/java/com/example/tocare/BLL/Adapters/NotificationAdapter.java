package com.example.tocare.BLL.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tocare.BLL.Model.Notification;
import com.example.tocare.BLL.Model.User;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;
import com.example.tocare.UIL.Fragment.ProfileFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Notification> mNotification;
    private final Data localData;


    public NotificationAdapter(Context mContext, List<Notification> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
        localData = Data.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item_manage, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Notification note = mNotification.get(position);
        UserModel user = new User();

        String imageURL = user.getImageUrl();
        Picasso.get().load(imageURL).into(holder.userImage);

        holder.username.setText(user.getUserName());
        holder.fullName.setText(user.getName() + " " + user.getLastName());


        holder.itemView.setOnClickListener(v -> goToProfile(user.getId()));

        holder.fullName.setOnClickListener(v -> goToProfile(user.getId()));

        holder.username.setOnClickListener(v -> goToProfile(user.getId()));

        holder.notificationMsg.setOnClickListener(v -> goToPost(note.getPostId()));

    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    private void goToProfile(String userId) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        editor.putString("profileId", userId);
        editor.putBoolean("fromManage", true);
        editor.apply();
        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_manage, new ProfileFragment()).commit();
    }

    private void goToPost(String userId) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        editor.putString("profileId", userId);
        editor.putBoolean("fromManage", true);
        editor.apply();
        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_manage, new ProfileFragment()).commit();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView username, fullName, notificationMsg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.image_profile);
            notificationMsg = itemView.findViewById(R.id.tv_notification);
            username = itemView.findViewById(R.id.username);
            fullName = itemView.findViewById(R.id.full_name);

        }
    }
}