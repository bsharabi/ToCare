package com.example.tocare;

import static com.example.tocare.BLL.Validation.UserValidation.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.UserModel;

import com.example.tocare.databinding.ActivityGithubLoginBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;


import java.util.ArrayList;
import java.util.List;

public class GithubLoginActivity extends AppCompatActivity {

    private ActivityGithubLoginBinding binding;
    private static final String TAG = "GithubLoginActivity";
    private ProgressDialog dialog;
    private EditText inputEmail, inputName, inputLastName, inputUserName;
    private CountryCodePicker ccp;
    private Button btLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGithubLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inputEmail = binding.etEmail;
        inputName = binding.etName;
        inputUserName = binding.etUserName;
        inputLastName = binding.etLastName;
        ccp = binding.countryPicker;
        ccp.registerCarrierNumberEditText(binding.etPhone);

        btLogin = binding.btLogin;
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        btLogin.setOnClickListener(view -> {
            String name, lastName, phone, email, userName;
            name = inputName.getText().toString().trim();
            lastName = inputLastName.getText().toString().trim();
            email = inputEmail.getText().toString().trim();
            phone = ccp.getFullNumberWithPlus();
            userName = inputUserName.getText().toString().trim();
            if (isValidEmail(email) &&isValidUserName(userName))
                SignInWithGitHub(email);
            else {
                inputEmail.setError("Email error");
                Toast.makeText(GithubLoginActivity.this, "Email error", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void SignInWithGitHub(String email) {
        dialog.setMessage("Please wait..");
        dialog.setTitle("Login with Github");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        handleSignInResult(email);

    }

    private void handleSignInResult(String email) {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
        provider.addCustomParameter("login", email);

        List<String> scopes =
                new ArrayList<String>() {
                    {
                        add("user:email");
                    }
                };
        provider.setScopes(scopes);


        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask
                    .addOnSuccessListener(
                            authResult -> {
                                Log.d(TAG, "signInWithCredential:success");
                                if (authResult.getAdditionalUserInfo().isNewUser())
                                    buildGithubData(authResult.getUser());
                                reload(MainActivity.class);

                            })
                    .addOnFailureListener(
                            e -> {
                                // Handle failure.
                                Log.w(TAG, "signInWithCredential:failure", e);
                                Toast.makeText(GithubLoginActivity.this, " error", Toast.LENGTH_LONG).show();
                                reload(LoginActivity.class);
                            });
        } else {
            mAuth
                    .startActivityForSignInWithProvider(/* activity= */ GithubLoginActivity.this, provider.build())
                    .addOnSuccessListener(
                            authResult -> {

                                Log.d(TAG, "signInWithCredential:success");
                                if (authResult.getAdditionalUserInfo().isNewUser())
                                    buildGithubData(authResult.getUser());
                                reload(MainActivity.class);

                            })
                    .addOnFailureListener(
                            e -> {
                                // Handle failure.
                                Log.w(TAG, "signInWithCredential:failure", e);
                                Toast.makeText(GithubLoginActivity.this, " error", Toast.LENGTH_LONG).show();
                                reload(LoginActivity.class);
                            });
        }
        dialog.dismiss();
    }

    private void buildGithubData(FirebaseUser firebaseUser) {

        UserModel userModel = new Admin(
                firebaseUser.getUid(),
                inputUserName.getText().toString().trim(),
                inputName.getText().toString().trim(),
                inputLastName.getText().toString().trim(),
                ccp.getFullNumberWithPlus(),
                "Hello my name is     " + inputUserName.getText().toString(),
                "https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a",
                true);
        DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                .document(firebaseUser.getUid());
        reference.set(userModel)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        dialog.dismiss();
                        Log.d(TAG, "DocumentReference:success");
                        Toast.makeText(this, "The details have been successfully registered", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    Log.d(TAG, "DocumentReference:failed");
                    Toast.makeText(this, "The details were not successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }

    public void reload(Class<?> name) {
        Intent intent = new Intent(GithubLoginActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}