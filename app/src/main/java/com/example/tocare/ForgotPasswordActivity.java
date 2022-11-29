package com.example.tocare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tocare.login.Activity_Splash_Login;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private TextView tvBackToLogin;
    private Button btSubmit;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Bundle extras = getIntent().getExtras();
        inputEmail = findViewById(R.id.et_email);
        inputEmail.setText(extras.getString("email"));
        email = inputEmail.getText().toString().trim();

        tvBackToLogin = findViewById(R.id.tv_backLogin);
        tvBackToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, Activity_Splash_Login.class);
            startActivity(intent);
        });
        btSubmit = findViewById(R.id.bt_submit);
        btSubmit.setOnClickListener(view -> {
            email = inputEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
            } else {

                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        "Email sent successfully to rest your password!",
                                        Toast.LENGTH_SHORT
                                ).show();
                                finish();
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        task.getException().toString(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
            }
        });

    }
}