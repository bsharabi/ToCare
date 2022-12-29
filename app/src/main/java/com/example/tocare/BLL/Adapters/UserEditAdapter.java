package com.example.tocare.BLL.Adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.UIL.profile.tabs.profileBasic.ProfileBasicFragment;
import com.example.tocare.UIL.task.TaskFragment;

public class UserEditAdapter extends FragmentStateAdapter {

    private UserModel user;

    public UserEditAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,UserModel userModel) {
        super(fragmentManager, lifecycle);
        user=userModel;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        System.out.println(position);
        switch (position) {
            case 0:
                return new ProfileBasicFragment(user);
            case 1:
                return new TaskFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
