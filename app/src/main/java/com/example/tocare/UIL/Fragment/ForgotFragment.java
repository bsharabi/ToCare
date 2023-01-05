package com.example.tocare.UIL.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.tocare.BLL.Listener.Callback;
import com.example.tocare.BLL.Validation.UserValidation;

import com.example.tocare.DAL.Auth;
import com.example.tocare.R;
import com.example.tocare.databinding.FragmentForgotBinding;

import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ForgotFragment extends Fragment implements View.OnClickListener {

    private TextView inputEmail;
    private Button btSubmit;
    private ProgressDialog dialog;
    private Auth auth;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentForgotBinding  binding = FragmentForgotBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        auth = Auth.getInstance();

        inputEmail = binding.etEmail;
        btSubmit = binding.btSubmit;

        btSubmit.setOnClickListener(this);
        dialog = new ProgressDialog(root.getContext());

        assert getArguments() != null;
        String email = getArguments().getString("Email", "");
        inputEmail.setText(email);

        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_login, new LoginFragment()).commit());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btSubmit.setEnabled(UserValidation.isValidEmail(inputEmail.getText().toString()));
    }

    private void forgotPassword(String email) {
        dialog.setMessage("Please wait while the link is sent to your email");
        dialog.setTitle("Password Reset...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        auth.sendPasswordResetEmail(email, new Callback() {
            @Override
            public void onCallback(boolean success, Exception e, FirebaseUser user) {}

            @Override
            public void onSuccess(boolean success, Exception e) {
                if (success) {
                    Toast.makeText(getContext(), "A password reset email has been sent to you", Toast.LENGTH_LONG).show();
                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_login, new LoginFragment()).commit();
                } else {
                    Toast.makeText(getContext(), "send link failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View view) {
        String email = inputEmail.getText().toString().trim();
        forgotPassword(email);
    }


}