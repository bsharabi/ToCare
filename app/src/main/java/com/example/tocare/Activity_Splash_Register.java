package com.example.tocare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
// Test for register two same users
//Tests
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Splash_Register extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputConformPassword, inputPhone;
    private Button btRegister;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String phonePattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String passwordPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_register);

        inputEmail = findViewById(R.id.et_email);
        inputPassword = findViewById(R.id.et_password);
        inputConformPassword = findViewById(R.id.et_c_password);
        inputPhone = findViewById(R.id.et_phone);
        btRegister = findViewById(R.id.bt_register);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        dialog = new ProgressDialog(this);
        TextView tv = findViewById(R.id.tv_backLogin);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               nextActivity();
            }
        });


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
        } else if (password.isEmpty() || password.length() < 8) {
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
                        nextActivity();
                        Toast.makeText(Activity_Splash_Register.this,"Registration successful",Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(Activity_Splash_Register.this,""+task.getException(),Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(Activity_Splash_Register.this, Activity_Splash_Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    
}
