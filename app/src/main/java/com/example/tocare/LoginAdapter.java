package com.example.tocare;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAdapter extends FragmentStateAdapter {

    LoginFragment loginFragment;
    SignupFragment signupFragment;
    ForgotFragment forgotFragment;


    public LoginAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        loginFragment = new LoginFragment();

        signupFragment =null;

        forgotFragment = new ForgotFragment();    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        System.out.println(position);
        switch (position) {
            case 0:
                return loginFragment;
            case 1:
                 signupFragment = new SignupFragment();
                return signupFragment;
            case 2:
                return forgotFragment;
            default:
                return null;
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }
}
