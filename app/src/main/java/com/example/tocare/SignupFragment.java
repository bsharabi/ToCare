package com.example.tocare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignupFragment extends Fragment {

    private EditText inputEmail, inputPassword, inputConformPassword, inputPhone;
    private Button btRegister;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog dialog;
    private TextView tv_back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register_tab_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmail = view.findViewById(R.id.et_email);
        inputPassword = view.findViewById(R.id.et_password);
        inputConformPassword = view.findViewById(R.id.et_c_password);
        inputPhone = view.findViewById(R.id.et_phone);
        btRegister = view.findViewById(R.id.bt_register);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dialog = new ProgressDialog(getContext());


        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid();
            }
        });

    }
    private void valid() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String cPassword = inputConformPassword.getText().toString();
        String phone = inputPhone.getText().toString();

        if (!email.matches(emailPattern)) {
            inputEmail.setError("Error");
        } else if (password.isEmpty() || password.length() < 1) {
            inputPassword.setError("Error");
        } else if (!password.equals(cPassword)) {
            inputConformPassword.setError("Error");
        } else {
            dialog.setMessage("Please wait while Registration");
            dialog.setTitle("Register");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            System.out.println(email);
            System.out.println(password);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();

                        Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
            mUser.sendEmailVerification();

        }
    }
    private void nextActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}