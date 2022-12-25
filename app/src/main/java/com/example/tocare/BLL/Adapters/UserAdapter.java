package com.example.tocare.BLL.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.User;
import com.example.tocare.BLL.Departments.UserModel;
import com.example.tocare.ManageUsersActivity;
import com.example.tocare.R;
import com.example.tocare.UIL.ui.users.UserCardFragment;
import com.example.tocare.databinding.FragmentCardUserBinding;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context mContext;
    private List<UserModel> mUser;
    private ManageUsersActivity manageUsersActivity;

    public UserAdapter(Context mContext, List<UserModel> mUser, ManageUsersActivity mUA) {
        this.mContext = mContext;
        this.mUser = (mUser == null)
                ? new ArrayList<>()
                : mUser;
        manageUsersActivity=mUA;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( mUser.get(position) instanceof Admin)
            return;
        UserModel user = mUser.get(position);
        holder.bioTV.setText(user.getBio());
        holder.tvFullName.setText(user.getName() + " " + user.getLastName());
        String imageURL = user.getImageUrl();
        Picasso.get().load(imageURL).into(holder.userImage);

        holder.edit.setOnClickListener(v -> {
            System.out.println(user.getUserName());
            Bundle bundle= new Bundle();
            bundle.putSerializable("User",user);
            manageUsersActivity.swapFragmentByFragmentClass(UserCardFragment.class,bundle);
        });
        holder.delete.setOnClickListener(v -> {
            System.out.println(user.getUserName());
        });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView bioTV, tvFullName;
        private MaterialButton edit,delete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.iv_user_image);
            bioTV = itemView.findViewById(R.id.tv_bio);
            tvFullName = itemView.findViewById(R.id.tv_user_name);
            edit = itemView.findViewById(R.id.bt_edit);
            delete = itemView.findViewById(R.id.bt_delete);


        }
    }
}