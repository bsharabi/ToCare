package com.example.tocare.BLL.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tocare.UIL.Fragment.LoginCardFragment;
import com.example.tocare.UIL.signup.SignupFragment;


public class LoginAdapter extends FragmentStateAdapter {

    public LoginAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1)
            return new SignupFragment();

        return new LoginCardFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
