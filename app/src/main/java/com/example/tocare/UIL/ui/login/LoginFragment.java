package com.example.tocare.UIL.ui.login;

import android.app.ProgressDialog;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.tocare.BLL.Validation.UserValidation;

import com.example.tocare.LoginActivity;
import com.example.tocare.MainActivity;
import com.example.tocare.R;
import com.example.tocare.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private FragmentLoginBinding binding;
    private EditText inputEmail, inputPassword;
    public TextView forgotPassword, loginHeader;
    private Button btLogin;
    private ProgressDialog dialog;
    private static LoginFragment single_instance = null;
    private static final String TAG = "LoginFragment";
    private LoginActivity loginActivity;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);


        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        View root = binding.getRoot();
        loginActivity = (LoginActivity) getActivity();
        inputEmail = binding.etEmail;
        inputPassword = binding.etPassword;
        forgotPassword = binding.tvForgetPassword;
        btLogin = binding.btLogin;
        loginHeader = binding.textLogin;
        dialog = new ProgressDialog(root.getContext());
        mAuth = FirebaseAuth.getInstance();
        loginViewModel.getText().observe(getViewLifecycleOwner(), loginHeader::setText);

        return root;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                loginActivity.hideKeyboard();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (UserValidation.isValidEmail(email))
                    signInWithEmail(email, password);
                else {
                    inputEmail.setError("The email format is incorrect");
                }
                break;
            case R.id.tv_forgetPassword:
                loginActivity.swapFragmentByPosition(2);
                break;
            default:
                break;
        }
    }

    private LoginFragment() {

    }

    public static LoginFragment getInstance() {
        if (single_instance == null)
            single_instance = new LoginFragment();
        return single_instance;
    }


    private void signInWithEmail(String email, String password) {

        dialog.setMessage("Please wait while Login with email");
        dialog.setTitle("Login...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mAuth
                .signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                if (task.getResult().getUser().isEmailVerified()) {
                    Log.d(TAG, "signInWithEmail:success");
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Login successful", Toast.LENGTH_LONG).show();
                    loginActivity.reload(MainActivity.class);
                } else {
                    dialog.dismiss();
                    btLogin.setEnabled(false);
                    Toast.makeText(getContext(), "Go confirm your email", Toast.LENGTH_LONG).show();
                    task.getResult().getUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                btLogin.setEnabled(true);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            btLogin.setEnabled(true);
                        }
                    });
                }
            }
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Log.d(TAG, "signInWithEmail:failed");
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }


}
