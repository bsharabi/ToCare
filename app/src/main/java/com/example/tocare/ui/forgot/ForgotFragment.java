package com.example.tocare.ui.forgot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tocare.databinding.FragmentAdminBinding;
import com.example.tocare.databinding.FragmentForgotBinding;
import com.example.tocare.ui.login.LoginFragment;
import com.example.tocare.ui.signup.SignupFragment;

public class ForgotFragment extends Fragment {

    private FragmentForgotBinding binding;
    private static ForgotFragment single_instance = null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ForgotViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ForgotViewModel.class);

        binding = FragmentForgotBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textForgot;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private ForgotFragment() {
    }

    public static ForgotFragment getInstance() {
        if (single_instance == null)
            single_instance = new ForgotFragment();

        return single_instance;
    }

}