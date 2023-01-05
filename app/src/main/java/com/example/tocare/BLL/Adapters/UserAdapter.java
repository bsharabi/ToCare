package com.example.tocare.BLL.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.Controller.ManageUsersActivity;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;
import com.example.tocare.UIL.Fragment.ProfileFragment;
import com.example.tocare.UIL.signup.SignupUserFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context mContext;
    private List<UserModel> mUser;
    private Data localData;


    public UserAdapter(Context mContext, List<UserModel> mUser) {
        this.mContext = mContext;
        this.mUser = mUser;
        localData=Data.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item_manage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserModel user = mUser.get(position);

        String imageURL = user.getImageUrl();
        Picasso.get().load(imageURL).into(holder.userImage);

        holder.username.setText(user.getUserName());
        holder.fullName.setText(user.getFullName());

        holder.edit.setOnClickListener(v -> {
            SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
            editor.putString("profileId", user.getId());
            editor.putBoolean("isChild", true);
            editor.apply();
            ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_manage, new ProfileFragment()).commit();
        });

        holder.itemView.setOnClickListener(v -> {
            SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
            editor.putString("profileId", user.getId());
            editor.putBoolean("isChild", true);
            editor.apply();
            ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_manage, new ProfileFragment()).commit();
        });

        holder.delete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(mContext)
                    .setTitle("Delete post")
                    .setMessage("Are you sure you want to delete the photo?")
                    .setPositiveButton("Continue", (dialogInterface, i) -> localData.deleteUserById(user.getId()))
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {

                    })
                    .show();
        });


    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView username, fullName, edit, delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.image_profile_item);
            edit = itemView.findViewById(R.id.btn_edit);
            delete = itemView.findViewById(R.id.btn_delete);
            username = itemView.findViewById(R.id.username);
            fullName = itemView.findViewById(R.id.full_name);

        }
    }
}