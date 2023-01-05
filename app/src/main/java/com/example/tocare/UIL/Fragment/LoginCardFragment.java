package com.example.tocare.UIL.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tocare.DAL.Auth;
import com.example.tocare.BLL.Validation.UserValidation;
import com.example.tocare.BLL.Listener.FirebaseLoginCallback;
import com.example.tocare.Controller.MainActivity;
import com.example.tocare.R;
import com.example.tocare.databinding.FragmentLoginCardBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.gson.Gson;

import java.util.Objects;


public class LoginCardFragment extends Fragment implements View.OnClickListener, FirebaseLoginCallback {

    private static final String TAG = "LoginCardFragment";
    private Auth auth;
    private EditText inputEmail, inputPassword;
    public TextView forgotPassword, loginHeader;
    private Button btLogin;
    private ProgressDialog dialog;
    private String email;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentLoginCardBinding binding = FragmentLoginCardBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        inputEmail = binding.etEmail;
        inputPassword = binding.etPassword;
        forgotPassword = binding.tvForgetPassword;
        btLogin = binding.btLogin;
        loginHeader = binding.textLogin;
        dialog = new ProgressDialog(root.getContext());

        auth = Auth.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmail.setTranslationY(300);
        inputPassword.setTranslationY(300);
        forgotPassword.setTranslationY(300);
        btLogin.setTranslationY(300);
        loginHeader.setTranslationX(300);

        final float alpha = 0;

        inputEmail.setAlpha(alpha);
        inputPassword.setAlpha(alpha);
        forgotPassword.setAlpha(alpha);
        btLogin.setAlpha(alpha);
        loginHeader.setAlpha(alpha);

        inputEmail.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        inputPassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        forgotPassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        btLogin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();
        loginHeader.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        btLogin.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (UserValidation.isValidEmail(email))
                    signInWithEmail(email, password);
                else {
                    inputEmail.setError("The email format is incorrect");
                }
                break;
            case R.id.tv_forgetPassword:
                Bundle bundle = new Bundle();
                bundle.putString("Email", email);
                Fragment fragment = new ForgotFragment();
                fragment.setArguments(bundle);
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_login, fragment).commit();
                break;
            default:
                break;
        }
    }

    private void signInWithEmail(String email, String password) {
        dialog.setMessage("Please wait while Login with email");
        dialog.setTitle("Login...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        auth.signInWithEmail(email, password, this);
    }

    @Override
    public void onSuccess(boolean success, Exception e,AuthCredential credential) {
        dialog.dismiss();
        if (success) {
                Log.d(TAG, "signInWithEmail:success");
                Toast.makeText(getContext(), "Login successful", Toast.LENGTH_LONG).show();
                saveCredential(credential);
                reload();
        } else {
            if (e != null && Objects.equals(e.getMessage(), "not confirm")) {
                Log.d(TAG, "signInWithEmail:Email confirm");
                Toast.makeText(getContext(), "Go confirm your email", Toast.LENGTH_LONG).show();
            } else {

                Log.d(TAG, "signInWithEmail:failed");
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void saveCredential(AuthCredential credential) {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String serializedCredential = gson.toJson(credential);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("credential", serializedCredential);
        editor.apply();
    }

    public void reload() {
        Log.d(TAG, "Reload:nextScreen");
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
