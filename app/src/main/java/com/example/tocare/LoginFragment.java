package com.example.tocare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

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


public class LoginFragment extends Fragment {

    private EditText inputEmail, inputPassword;
    private int v=0;
    public TextView forgotPassword;
    private Button btLogin;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String passwordPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog dialog;

    ViewPager2 viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.login_tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputEmail=view.findViewById(R.id.et_email);
        inputPassword=view.findViewById(R.id.et_password);
        forgotPassword=view.findViewById(R.id.tv_forgetPassword);
        btLogin=view.findViewById(R.id.bt_login);
        viewPager=view.findViewById(R.id.view_pager);
        dialog = new ProgressDialog(view.getContext());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        inputEmail.setTranslationY(300);
        inputPassword.setTranslationY(300);
        forgotPassword.setTranslationY(300);
        btLogin.setTranslationY(300);

        inputEmail.setAlpha(v);
        inputPassword.setAlpha(v);
        forgotPassword.setAlpha(v);
        btLogin.setAlpha(v);


        inputEmail.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        inputPassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        forgotPassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        btLogin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid();
            }
        });



    }
    private void valid() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();


        if (!email.matches(emailPattern)) {
            inputEmail.setError("Error");
        } else if (password.isEmpty() || password.length() < 8) {
            inputPassword.setError("Error");
        } else {
            dialog.setMessage("Please wait while Login");
            dialog.setTitle("Login...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            System.out.println(email);
            System.out.println(password);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Login successful", Toast.LENGTH_LONG).show();
                        nextActivity();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}