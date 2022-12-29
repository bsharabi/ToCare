package com.example.tocare.Controller;

import static com.example.tocare.BLL.Validation.UserValidation.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tocare.BLL.Model.Admin;
import com.example.tocare.DAL.Login;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Listener.Callback;
import com.example.tocare.databinding.ActivityGithubLoginBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

public class GithubLoginActivity extends AppCompatActivity implements Callback {

    private static final String TAG = "GithubLoginActivity";

    private ActivityGithubLoginBinding binding;
    private ProgressDialog dialog;
    private EditText inputEmail, inputName, inputLastName, inputUserName;
    private CountryCodePicker ccp;
    private Button btLogin;
    private Login login;

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
        btLogin = binding.btLogin;

        ccp.registerCarrierNumberEditText(binding.etPhone);

        login = Login.getInstance();
        dialog = new ProgressDialog(this);

        btLogin.setOnClickListener(view -> {
            String name, lastName, email, userName;
            name = inputName.getText().toString().trim();
            lastName = inputLastName.getText().toString().trim();
            email = inputEmail.getText().toString().trim();
            userName = inputUserName.getText().toString().trim();
            UserModel userModel = new Admin(
                    "",
                    userName,
                    name,
                    lastName,
                    ccp.getFullNumberWithPlus(),
                    "Hello my name is " + userName,
                    "https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a",
                    true);
            if (isValidEmail(email) && isValidUserName(userName))
                SignInWithGitHub(email, userModel);
            else {
                inputEmail.setError("Email error");
                Toast.makeText(GithubLoginActivity.this, "Email error", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void SignInWithGitHub(String email, UserModel userModel) {
        dialog.setMessage("Please wait..");
        dialog.setTitle("Login with Github");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
        provider.addCustomParameter("login", email);
        List<String> scopes =
                new ArrayList<String>() {
                    {
                        add("user:email");
                    }
                };
        provider.setScopes(scopes);
        login.handleSignInResult(provider, this, this, userModel);
    }


    public void reload(Class<?> name) {
        Intent intent = new Intent(GithubLoginActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCallback(boolean success, Exception e, FirebaseUser firebaseUser) {
        if (success) {
            Log.d(TAG, "signInWithCredential:success");
            Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            reload(LoginActivity.class);
        } else {
            dialog.dismiss();
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            reload(LoginActivity.class);

        }
    }

    @Override
    public void onSuccess(boolean success, Exception e) {
        if (success) {
            Log.d(TAG, "DocumentReference::User::success");
            Toast.makeText(this, "The details have been successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "DocumentReference::User::failed");
            Toast.makeText(this, "The details were not successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();
            reload(LoginActivity.class);
        }
    }

    @Override
    public void onComplete(boolean success, Exception e) {
        if ((success)) {
            Log.d(TAG, "DocumentReference::Task::success");
            Toast.makeText(this, "The details have been successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();
            reload(MainActivity.class);
        } else {
            Log.d(TAG, "DocumentReference::Task::failed");
            Toast.makeText(this, "The details were not successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();
            reload(LoginActivity.class);
        }
        dialog.dismiss();
    }
}