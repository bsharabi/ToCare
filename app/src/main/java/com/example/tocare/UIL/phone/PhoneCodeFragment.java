package com.example.tocare.UIL.phone;

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
import com.example.tocare.DAL.Login;
import com.example.tocare.BLL.Listener.PhoneCallback;
import com.example.tocare.BLL.Validation.UserValidation;
import com.example.tocare.Controller.LoginActivity;
import com.example.tocare.Controller.PhoneLoginActivity;
import com.example.tocare.R;
import com.example.tocare.Controller.MainActivity;
import com.example.tocare.databinding.FragmentPhoneCodeVerificationBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneCodeFragment extends Fragment implements View.OnClickListener, TextWatcher, PhoneCallback {

    private static final String TAG = "PhoneCodeFragment";

    private FragmentPhoneCodeVerificationBinding binding;
    private EditText inputPhoneCode;
    private Button btSubmit;
    private TextView header, sendAgain;
    private ImageView imageView;
    private ProgressDialog dialog;
    private String mVerificationId;
    private PhoneLoginActivity phoneLoginActivity;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Login login;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentPhoneCodeVerificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inputPhoneCode = binding.etPhoneCode;
        btSubmit = binding.btSubmit;
        header = binding.textPhoneCode;
        sendAgain = binding.tvSendAgain;
        imageView = binding.ivPhoneIcon;
        mVerificationId = requireArguments().getString("verificationId");
        phoneLoginActivity = (PhoneLoginActivity) getActivity();

        dialog = new ProgressDialog(root.getContext());
        login = Login.getInstance();

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
        inputPhoneCode.addTextChangedListener(this);


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (UserValidation.isValidCode(inputPhoneCode.getText().toString().trim()))
            btSubmit.setEnabled(true);
        else
            btSubmit.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                String code = inputPhoneCode.getText().toString().trim();
                setDialog("Verify Phone Number With Code");
                login.verifyPhoneNumberWithCode(code, mVerificationId, this);
                break;
            case R.id.tv_send_again:
                String phone = "+" + requireArguments().getString("countryCode") + requireArguments().getString("phone").substring(1);
                setDialog("Resend Verification Code");
                login.resendVerificationCode(phone, mResendToken, getActivity());
                Toast.makeText(getContext(), "re send Verification Code", Toast.LENGTH_LONG).show();
            default:
                break;
        }
    }

    private void setDialog(String title) {
        dialog.setMessage("Please wait...");
        dialog.setTitle(title);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onCallback(boolean success, Exception e, Task<AuthResult> task) {
        if (success) {
            Log.d(TAG, "signInWithCredential:success");
            phoneLoginActivity.reload(MainActivity.class);
        } else {
            if (e.getMessage().equals("not registered"))
                Toast.makeText(getContext(), "This phone number is not registered in the system", Toast.LENGTH_LONG).show();
            Log.w(TAG, "signInWithCredential:failure", e);
            System.out.println(e);
            phoneLoginActivity.reload(LoginActivity.class);
        }
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
        phoneLoginActivity.reload(LoginActivity.class);
        dialog.dismiss();
    }

    @Override
    public void onVerificationCompleted(boolean success, PhoneAuthCredential credential) {
        if (success) {
            Log.d(TAG, "onVerificationCompleted:" + credential);
            login.verifyPhoneNumberWithCode(credential.getSmsCode(), mVerificationId, this);
        }
    }

    @Override
    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
        Log.d(TAG, "onCodeSent:" + verificationId);
        Toast.makeText(getContext(), "Code sent ", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        mVerificationId = verificationId;
        mResendToken = token;
    }
}
