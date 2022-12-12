package com.example.tocare.UIL.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.UserModel;
import com.example.tocare.BLL.Validation.UserValidation;
import com.example.tocare.DLL.Auth;
import com.example.tocare.DLL.DataBase;
import com.example.tocare.UIL.ui.Activities.LoginActivity;
import com.example.tocare.UIL.ui.Activities.MainActivity;
import com.example.tocare.R;
import com.example.tocare.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;

import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment implements View.OnClickListener,TextWatcher {

    private FragmentLoginBinding binding;
    private EditText inputEmailOrPhone, inputPassword;
    public TextView forgotPassword;
    private Button btLogin;
    private ProgressDialog dialog;
    private static LoginFragment single_instance = null;
    private static final String TAG = "LoginFragment";
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.

            Log.d(TAG, "onVerificationCompleted:" + credential);
            System.out.println(credential.getSmsCode());
            verifyPhoneNumberWithCode(credential.getSmsCode());
//                signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);
            Toast.makeText(getContext(), "Verification Failed", Toast.LENGTH_SHORT).show();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);
            super.onCodeSent(verificationId, token);
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inputEmailOrPhone = binding.etEmail;
        inputPassword = binding.etPassword;
        forgotPassword = binding.tvForgetPassword;
        btLogin = binding.btLogin;
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(root.getContext());


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmailOrPhone.setTranslationY(300);
        inputPassword.setTranslationY(300);
        forgotPassword.setTranslationY(300);
        btLogin.setTranslationY(300);

        final float alpha = 0;

        inputEmailOrPhone.setAlpha(alpha);
        inputPassword.setAlpha(alpha);
        forgotPassword.setAlpha(alpha);
        btLogin.setAlpha(alpha);

        inputEmailOrPhone.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        inputPassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        forgotPassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        btLogin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();

        btLogin.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        inputEmailOrPhone.addTextChangedListener(this);

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
                String emailOrPhone = inputEmailOrPhone.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                signInController(emailOrPhone, password);
                break;
            case R.id.tv_forgetPassword:
                LoginActivity.getInstance().getViewPager().setCurrentItem(2);
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

    public void nextActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void signInController(String emailOrPhone, String password) {

        String ans = UserValidation.checkIsEmailOrPhoneNumber(emailOrPhone);

        switch (ans) {
            case "Email":
                signInWithEmail(emailOrPhone, password);
                break;
            case "Phone":
                SignInWithPhone(emailOrPhone);
                break;
            default:
                inputEmailOrPhone.setError("Invalid argument");
                break;
        }
    }

    private void signInWithEmail(String email, String password) {

        dialog.setMessage("Please wait while Login with email");
        dialog.setTitle("Login...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Auth.getInstance().getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success");
                dialog.dismiss();
                Toast.makeText(getContext(), "Login successful", Toast.LENGTH_LONG).show();
                LoginFragment.getInstance().nextActivity();
            }
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Log.d(TAG, "signInWithEmail:failed");
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    public boolean SignInWithPhone(String phoneNumber) {


        startPhoneNumberVerification(phoneNumber);
//        LoginFragment.getInstance().verifyPhoneNumberWithCode(inputEmail.getText().toString().trim());
        return true;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        LoginFragment.getInstance().nextActivity();
                        // Update UI
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void verifyPhoneNumberWithCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+972" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
