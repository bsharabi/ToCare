package com.example.tocare.BLL.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tocare.UIL.ui.profile.tabs.profileAdvance.ProfileAdvanceFragment;
import com.example.tocare.UIL.ui.profile.tabs.profileBasic.ProfileBasicFragment;

public class ProfileAdapter extends FragmentStateAdapter {


    public ProfileAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        System.out.println(position);
        switch (position) {
            case 0:
                return ProfileBasicFragment.getInstance();
            case 1:
                return ProfileAdvanceFragment.getInstance();

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}