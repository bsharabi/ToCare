package com.example.tocare.ui.forgot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tocare.databinding.FragmentAdminBinding;
import com.example.tocare.databinding.FragmentForgotBinding;
import com.example.tocare.ui.login.LoginFragment;
import com.example.tocare.ui.signup.SignupFragment;

public class ForgotFragment extends Fragment implements View.OnClickListener {

    private FragmentForgotBinding binding;
    private static ForgotFragment single_instance = null;
    TextView inputEmail;
    Button btSubmit;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ForgotViewModel forgotViewModel = new ViewModelProvider(this).get(ForgotViewModel.class);

        binding = FragmentForgotBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textForgot;
        forgotViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        inputEmail = binding.etEmail;
        btSubmit=binding.btSubmit;
        btSubmit.setOnClickListener(this);

        return root;
    }

    private void forgotPassword() {
        System.out.println("Forgot password listener");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        System.out.println("onDestroyView Function");
    }

    private ForgotFragment() {
    }

    public static ForgotFragment getInstance() {
        if (single_instance == null)
            single_instance = new ForgotFragment();
        return single_instance;
    }

    @Override
    public void onClick(View view) {
        forgotPassword();
    }
}