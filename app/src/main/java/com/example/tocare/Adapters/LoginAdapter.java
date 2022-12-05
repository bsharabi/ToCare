package com.example.tocare.Adapters;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.tocare.ui.forgot.ForgotFragment;
import com.example.tocare.ui.login.*;
import com.example.tocare.ui.signup.SignupFragment;


public class LoginAdapter extends FragmentStateAdapter {

    public LoginAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        System.out.println(position);
        switch (position) {
            case 0:
                return LoginFragment.getInstance();
            case 1:
                return SignupFragment.getInstance();
            case 2:
                return ForgotFragment.getInstance();
            default:
                return null;
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }
}
