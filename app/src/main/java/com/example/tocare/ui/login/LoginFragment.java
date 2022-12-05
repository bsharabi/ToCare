package com.example.tocare.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.viewpager2.widget.ViewPager2;

import com.example.tocare.Activities.MainActivity;
import com.example.tocare.R;
import com.example.tocare.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private EditText inputEmail, inputPassword;
    private int v = 0;
    public TextView forgotPassword;
    private Button btLogin;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String passwordPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog dialog;
    private static LoginFragment single_instance = null;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inputEmail =binding.etEmail;
        inputPassword = binding.etPassword;
        forgotPassword = binding.tvForgetPassword;
        btLogin =binding.btLogin;
        dialog = new ProgressDialog(root.getContext());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();


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

            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Login successful", Toast.LENGTH_LONG).show();
                    nextActivity();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private LoginFragment() {
    }

    public static LoginFragment getInstance() {
        if (single_instance == null)
            single_instance = new LoginFragment();

        return single_instance;
    }

}