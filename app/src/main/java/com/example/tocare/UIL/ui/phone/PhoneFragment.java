package com.example.tocare.UIL.ui.phone;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.example.tocare.databinding.FragmentPhoneLoginBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import java.util.concurrent.TimeUnit;

public class PhoneFragment extends Fragment implements View.OnClickListener {

    private TextView header;
    private FragmentPhoneLoginBinding binding;
    private EditText inputPhone;
    private Button btSubmit;
    private ImageView imageView;
    private ProgressDialog dialog;
    private String Phone;
    private static PhoneFragment single_instance = null;
    private static final String TAG = "PhoneFragment";
    private String mVerificationId;
    private String countryCode;
    private CountryCodePicker countryCodePicker;
    private PhoneLoginActivity phoneLoginActivity;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            Log.d(TAG, "onVerificationCompleted:" + credential);
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
            dialog.dismiss();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId, token);
            Log.d(TAG, "onCodeSent:" + verificationId);
            Toast.makeText(getContext(), "Code sent ", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

            mVerificationId = verificationId;
            Bundle bundle = new Bundle();
            bundle.putString("verificationId", mVerificationId);
            bundle.putString("phone", Phone);
            bundle.putString("countryCode",countryCode);
            phoneLoginActivity.swapFragmentByFragmentClass(PhoneCodeFragment.class, bundle);
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PhoneViewModel phoneViewModel = new ViewModelProvider(this).get(PhoneViewModel.class);

        binding = FragmentPhoneLoginBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        phoneLoginActivity = (PhoneLoginActivity) getActivity();
        inputPhone = binding.etPhone;
        btSubmit = binding.btSubmit;
        imageView = binding.ivPhoneIcon;
        header = binding.textPhone;
        countryCodePicker = binding.countryCode;
        countryCodePicker.registerCarrierNumberEditText(inputPhone);

        dialog = new ProgressDialog(root.getContext());

        phoneViewModel.getText().observe(getViewLifecycleOwner(), header::setText);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputPhone.setTranslationY(300);
        btSubmit.setTranslationY(300);
        header.setTranslationY(300);
        imageView.setTranslationX(300);
        countryCodePicker.setTranslationY(300);

        final float alpha = 0;

        inputPhone.setAlpha(alpha);
        btSubmit.setAlpha(alpha);
        header.setAlpha(alpha);
        imageView.setAlpha(alpha);
        countryCodePicker.setAlpha(alpha);

        imageView.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(350).start();
        header.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(450).start();
        countryCodePicker.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        inputPhone.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(550).start();
        btSubmit.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(650).start();

        btSubmit.setOnClickListener(this);
        btSubmit.setEnabled(false);
        countryCodePicker.setOnCountryChangeListener(() -> {
            countryCode = "+"+ countryCodePicker.getSelectedCountryCode();
            btSubmit.setEnabled(true);
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
                Phone = inputPhone.getText().toString().trim();
                SignInWithPhone(Phone);
                break;
            default:
                break;
        }
    }


    public boolean SignInWithPhone(String phoneNumber) {
        if (UserValidation.isValidPhone(phoneNumber)) {
            dialog.setMessage("Please wait...");
            dialog.setTitle("Send code");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            startPhoneNumberVerification(phoneNumber);
        } else
            inputPhone.setError("The phone format is incorrect");
        return true;
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(countryCode + phoneNumber.substring(1))       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

}
