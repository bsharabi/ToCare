package com.example.tocare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Activity_Splash_Login extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String passwordPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog dialog;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_splash_login);
        inputEmail = findViewById(R.id.et_email);
        inputPassword = findViewById(R.id.et_password);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dialog = new ProgressDialog(this);


        TextView tv_register = findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Splash_Login.this, Activity_Splash_Register.class);
                startActivity(intent);
            }
        });


        btLogin = findViewById(R.id.bt_login);
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
            dialog.setMessage("Done");
            dialog.setTitle("Register");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            System.out.println(email);
            System.out.println(password);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        Toast.makeText(Activity_Splash_Login.this, "Login successful", Toast.LENGTH_LONG).show();
                        nextActivity();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(Activity_Splash_Login.this, "" + task.getException(), Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(Activity_Splash_Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}