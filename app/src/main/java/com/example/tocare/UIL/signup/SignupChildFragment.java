package com.example.tocare.UIL.signup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.tocare.DAL.Auth;
import com.example.tocare.BLL.Listener.PhoneCallback;
import com.example.tocare.BLL.Validation.UserValidation;
import com.example.tocare.R;
import com.example.tocare.UIL.Fragment.UsersFragment;
import com.example.tocare.UIL.phone.PhoneSignupVerificationFragment;
import com.example.tocare.databinding.FragmentSignupUserBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Objects;


public class SignupChildFragment extends Fragment implements View.OnClickListener, PhoneCallback {


    private static final String TAG = "SignupUser";

    private EditText inputName, inputLastName, inputPhone, inputUserName;
    private ImageView image_profile, plus;
    private Button btSignup;
    private ProgressDialog dialog;
    private CountryCodePicker ccp;
    private Auth auth;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.example.tocare.databinding.FragmentSignupUserBinding binding = FragmentSignupUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inputUserName = binding.etUserName;
        plus = binding.plus;
        inputName = binding.etName;
        inputLastName = binding.etLastName;
        inputPhone = binding.etPhone;
        ccp = binding.countryPicker;
        btSignup = binding.btSignup;
        image_profile = binding.imProfile;

        ccp.registerCarrierNumberEditText(inputPhone);
        dialog = new ProgressDialog(getContext());
        auth = Auth.getInstance();
        Toolbar toolbar = binding.toolbar;


        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("");
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_manage, new UsersFragment()).commit());


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
        btSignup.setTranslationY(300);
        image_profile.setTranslationX(300);
        plus.setTranslationX(300);

        final float alpha = 0;

        inputName.setAlpha(alpha);
        inputLastName.setAlpha(alpha);
        inputPhone.setAlpha(alpha);
        ccp.setAlpha(alpha);
        btSignup.setAlpha(alpha);
        inputUserName.setAlpha(alpha);
        image_profile.setAlpha(alpha);
        plus.setAlpha(alpha);

        image_profile.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(250).start();
        plus.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(250).start();
        inputUserName.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(350).start();
        inputName.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        inputLastName.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(450).start();
        inputPhone.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        ccp.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        btSignup.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(900).start();

        btSignup.setOnClickListener(this);

        btSignup.setEnabled(false);
        ccp.setOnCountryChangeListener(() -> btSignup.setEnabled(true));

    }

    public void createAccountWithPhone() {
        if (UserValidation.isValidPhone(ccp.getFullNumber())) {
            dialog.setMessage("Please wait...");
            dialog.setTitle("Send code");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            auth.startPhoneNumberVerification(ccp.getFullNumberWithPlus(), getActivity(), this);
        } else
            inputPhone.setError("The phone format is incorrect");
    }

    @Override
    public void onClick(@NonNull View view) {
        if (view.getId() == R.id.bt_signup)
//         if (UserValidation.SignUpValidation(email, password, userName, fullName, phone, cPassword) && ccp.isValidFullNumber())
            createAccountWithPhone();


    }

    @Override
    public void onCallback(boolean success, Exception e, Task<AuthResult> task) {

    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
        Log.w(TAG, "onVerificationFailed", e);
        Toast.makeText(getContext(), "Verification Failed", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onVerificationCompleted(boolean success, PhoneAuthCredential credential) {
        if (success)
            Log.d(TAG, "onVerificationCompleted:" + credential);
    }

    @Override
    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
        Log.d(TAG, "onCodeSent:" + verificationId);
        Toast.makeText(getContext(), "Code sent ", Toast.LENGTH_SHORT).show();
        dialog.dismiss();

        Bundle bundle = new Bundle();
        String name = inputName.getText().toString();
        String lastName = inputLastName.getText().toString();
        String userName = inputUserName.getText().toString();
        bundle.putString("verificationId", verificationId);
        bundle.putString("phone", ccp.getFullNumberWithPlus());
        bundle.putString("countryCode", "+" + ccp.getSelectedCountryCode());
        bundle.putString("name", name);
        bundle.putString("lastName", lastName);
        bundle.putString("userName", userName);
        bundle.putBoolean("newUser", true);

        Fragment fragment = new PhoneSignupVerificationFragment();
        fragment.setArguments(bundle);
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_manage, fragment).commit();

    }


}