package com.example.tocare;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tocare.ui.profile.tabs.ProfileAdvanceTabFragment;
import com.example.tocare.ui.profile.tabs.ProfileBasicTabFragment;

public class ProfileAdapter extends FragmentStateAdapter {

    ProfileAdvanceTabFragment profileAdvanceTabFragment;
    ProfileBasicTabFragment profileBasicTabFragment;


    public ProfileAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        System.out.println(position);
        switch (position) {
            case 0:
                return new ProfileBasicTabFragment();
            case 1:
                return new ProfileAdvanceTabFragment();

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
