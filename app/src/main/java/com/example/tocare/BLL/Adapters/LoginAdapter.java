package com.example.tocare.BLL.Adapters;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.tocare.UIL.Fragment.ForgotFragment;
import com.example.tocare.UIL.Fragment.LoginFragment;
import com.example.tocare.UIL.signup.SignupFragment;


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
                return new LoginFragment();
            case 1:
                return new  SignupFragment();
            case 2:
                return new ForgotFragment();
            default:
                return null;
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }
}
