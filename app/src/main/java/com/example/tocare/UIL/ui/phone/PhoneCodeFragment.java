package com.example.tocare.UIL.ui.phone;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.tocare.BLL.Validation.UserValidation;
import com.example.tocare.PhoneLoginActivity;
import com.example.tocare.R;
import com.example.tocare.MainActivity;
import com.example.tocare.databinding.FragmentPhoneCodeVerificationBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneCodeFragment extends Fragment implements View.OnClickListener {

    private FragmentPhoneCodeVerificationBinding binding;
    private EditText inputPhoneCode;
    private Button btSubmit;
    private TextView header, sendAgain;
    private ImageView imageView;
    private ProgressDialog dialog;
    private static final String TAG = "PhoneCodeFragment";
    private String mVerificationId;
    private PhoneLoginActivity phoneLoginActivity ;
//שינוי המקלדת בעת הזנת קוד
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            Log.d(TAG, "onVerificationCompleted:" + credential);
            System.out.println(credential.getSmsCode());
            verifyPhoneNumberWithCode(credential.getSmsCode());

//                signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w(TAG, "onVerificationFailed", e);
            Toast.makeText(getContext(), "Verification Failed", Toast.LENGTH_SHORT).show();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId, token);
            Log.d(TAG, "onCodeSent:" + verificationId);
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        PhoneCodeViewModel phoneViewModel = new ViewModelProvider(this).get(PhoneCodeViewModel.class);

        binding = FragmentPhoneCodeVerificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inputPhoneCode = binding.etPhoneCode;
        btSubmit = binding.btSubmit;
        header = binding.textPhoneCode;
        sendAgain = binding.tvSendAgain;
        imageView = binding.ivPhoneIcon;
        phoneLoginActivity = (PhoneLoginActivity) getActivity();

        dialog = new ProgressDialog(root.getContext());

        phoneViewModel.getText().observe(getViewLifecycleOwner(), header::setText);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputPhoneCode.setTranslationY(300);
        btSubmit.setTranslationY(300);
        header.setTranslationY(300);
        sendAgain.setTranslationY(300);
        imageView.setTranslationX(300);

        final float alpha = 0;

        inputPhoneCode.setAlpha(alpha);
        btSubmit.setAlpha(alpha);
        header.setAlpha(alpha);
        sendAgain.setAlpha(alpha);
        imageView.setAlpha(alpha);

        imageView.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(350).start();
        header.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(450).start();
        inputPhoneCode.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(550).start();
        sendAgain.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(650).start();
        btSubmit.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(750).start();

        btSubmit.setOnClickListener(this);
        sendAgain.setOnClickListener(this);
        btSubmit.setEnabled(false);
        inputPhoneCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println(UserValidation.isValidCode(inputPhoneCode.getText().toString().trim()));
                if (UserValidation.isValidCode(inputPhoneCode.getText().toString().trim()))
                    btSubmit.setEnabled(true);
                else
                    btSubmit.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                String Code = inputPhoneCode.getText().toString().trim();
                mVerificationId = requireArguments().getString("verificationId");
                verifyPhoneNumberWithCode(Code);
                break;
            case R.id.tv_send_again:
                resendVerificationCode(requireArguments().getString("phone"), mResendToken);
                Toast.makeText(getContext(), "re send Verification Code", Toast.LENGTH_LONG).show();
            default:
                break;
        }
    }

    public PhoneCodeFragment() {
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        phoneLoginActivity.reload(MainActivity.class);
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+972" + phoneNumber)       // Phone number to verify
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


}
