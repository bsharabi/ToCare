package com.example.tocare.UIL.ui.signup;

import android.app.ProgressDialog;
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

import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.UserModel;

import com.example.tocare.LoginActivity;
import com.example.tocare.R;
import com.example.tocare.databinding.FragmentSignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

public class SignupFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "Signup";
    private EditText inputName, inputLastName, inputPhone, inputEmail, inputPassword, inputConformPassword, inputUserName;
    private Button btSignup;
    private ProgressDialog dialog;
    private FragmentSignupBinding binding;
    private static SignupFragment single_instance = null;
    private LoginActivity loginActivity;
    private FirebaseAuth mAuth;
    private CountryCodePicker ccp;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loginActivity = (LoginActivity) getActivity();
        inputUserName = binding.etUserName;
        inputName = binding.etName;
        inputLastName = binding.etLastName;
        inputPhone = binding.etPhone;
        ccp = binding.countryPicker;
        ccp.registerCarrierNumberEditText(inputPhone);
        inputEmail = binding.etEmail;
        inputPassword = binding.etPassword;
        inputConformPassword = binding.etCPassword;
        btSignup = binding.btSignup;
        dialog = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputUserName.setTranslationX(300);
        inputName.setTranslationX(300);
        inputLastName.setTranslationX(300);
        inputPhone.setTranslationX(300);
        ccp.setTranslationX(300);
        inputEmail.setTranslationX(300);
        inputPassword.setTranslationX(300);
        inputConformPassword.setTranslationX(300);
        btSignup.setTranslationY(300);

        final int alpha = 0;

        inputName.setAlpha(alpha);
        inputLastName.setAlpha(alpha);
        inputPhone.setAlpha(alpha);
        ccp.setAlpha(alpha);
        inputEmail.setAlpha(alpha);
        inputPassword.setAlpha(alpha);
        inputConformPassword.setAlpha(alpha);
        btSignup.setAlpha(alpha);
        inputUserName.setAlpha(alpha);

        inputUserName.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(350).start();
        inputName.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        inputLastName.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(450).start();
        inputPhone.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        ccp.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        inputEmail.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        inputPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(700).start();
        inputConformPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        btSignup.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(900).start();

        btSignup.setOnClickListener(this);


    }

    public void createAccountWithEmail(String email, String password, String userName, String phone, String name, String lastName) {
        dialog.setMessage("Please wait while Registration");
        dialog.setTitle("Register");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        task.getResult().getUser().sendEmailVerification();
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(getContext(), "Registration successful Go confirm your email", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//                        firebaseUser.updateEmail(email);
//                        firebaseUser.updatePassword(password);
//                        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
//                        builder.setDisplayName(userName);
//                        firebaseUser.updateProfile(builder.build());
                        UserModel userModel = new Admin(
                                firebaseUser.getUid(),
                                userName,
                                name,
                                lastName,
                                ccp.getFullNumberWithPlus() ,
                                "Hello my name is " + userName,
                                "https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a",
                                true);
                        DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                                .document(firebaseUser.getUid());
                        reference.set(userModel)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        dialog.dismiss();
                                        Log.d(TAG, "DocumentReference:success");
//                                        Toast.makeText(getContext(), "The details have been successfully registered", Toast.LENGTH_SHORT).show();
                                        loginActivity.swapFragmentByPosition(0);
                                    }
                                }).addOnFailureListener(e -> {
                                    dialog.dismiss();
                                    Log.d(TAG, "DocumentReference:failed");
                                    Toast.makeText(getContext(), "The details were not successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    Log.w(TAG, "createUserWithEmail:failure");
                    Toast.makeText(getContext(), "Authentication failed " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
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

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.bt_signup:
                String name = inputName.getText().toString();
                String lastName = inputLastName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String cPassword = inputConformPassword.getText().toString();
                String phone = inputPhone.getText().toString();
                String userName = inputUserName.getText().toString();
//                if (UserValidation.SignUpValidation(email, password, userName, fullName, phone, cPassword) && ccp.isValidFullNumber())
                createAccountWithEmail(email, password, userName, phone, name, lastName);
                loginActivity.hideKeyboard();
                break;
            default:
                break;
        }

    }



    public String getEmail() {
        return inputEmail.getText().toString().trim();
    }
}