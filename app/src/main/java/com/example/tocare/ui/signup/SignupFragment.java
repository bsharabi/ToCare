package com.example.tocare.ui.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.tocare.Departments.UserModel;
import com.example.tocare.databinding.ActivityLoginBinding;
import com.example.tocare.databinding.FragmentSignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupFragment extends Fragment {

    private EditText inputFullName, inputPhone, inputEmail, inputPassword, inputConformPassword;
    private Button btSignup;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private int alpha = 0;
    private FirebaseUser mUser;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog dialog;
    private FragmentSignupBinding binding;
    private static SignupFragment single_instance = null;
    private ViewPager2 viewPager2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewPager2 = ActivityLoginBinding.inflate(inflater, container, false).viewPager;

        inputFullName = binding.etFullName;
        inputPhone = binding.etPhone;
        inputEmail = binding.etEmail;
        inputPassword = binding.etPassword;
        inputConformPassword = binding.etCPassword;
        btSignup = binding.btSignup;


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(getContext());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputFullName.setTranslationX(300);
        inputPhone.setTranslationX(300);
        inputEmail.setTranslationX(300);
        inputPassword.setTranslationX(300);
        inputConformPassword.setTranslationX(300);
        btSignup.setTranslationY(300);

        inputFullName.setAlpha(alpha);
        inputPhone.setAlpha(alpha);
        inputEmail.setAlpha(alpha);
        inputPassword.setAlpha(alpha);
        inputConformPassword.setAlpha(alpha);
        btSignup.setAlpha(alpha);

        inputFullName.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        inputPhone.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        inputEmail.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        inputPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(700).start();
        inputConformPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        btSignup.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(900).start();

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid();
            }
        });

    }

    private void valid() {
        String fullName = inputFullName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String cPassword = inputConformPassword.getText().toString();
        String phone = inputPhone.getText().toString();

        if (!email.matches(emailPattern)) {
            inputEmail.setError("Email error");
        } else if (password.isEmpty() || password.length() < 1) {
            inputPassword.setError("Error");
        } else if (!password.equals(cPassword)) {
            inputConformPassword.setError("Error");
        } else {
            dialog.setMessage("Please wait while Registration");
            dialog.setTitle("Register");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseFirestore.collection("User")
                                        .document(mAuth.getUid())
                                        .set(new UserModel(fullName, phone, email, password));
                                viewPager2.setCurrentItem(0);
                                Toast.makeText(binding.getRoot().getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(binding.getRoot().getContext(), "Registration failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

            mUser.sendEmailVerification();


        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private SignupFragment() {
    }

    public static SignupFragment getInstance() {
        if (single_instance == null)
            single_instance = new SignupFragment();

        return single_instance;
    }
}