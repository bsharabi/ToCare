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
import com.example.tocare.BLL.Model.Admin;
import com.example.tocare.DAL.Data;
import com.example.tocare.BLL.Listener.FirebaseCallback;
import com.example.tocare.BLL.Listener.PhoneCallback;
import com.example.tocare.BLL.Model.User;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Validation.UserValidation;
import com.example.tocare.Controller.ManageUsersActivity;
import com.example.tocare.R;
import com.example.tocare.UIL.users.UsersFragment;
import com.example.tocare.databinding.FragmentPhoneCodeVerificationBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneSignupVerificationFragment extends Fragment implements View.OnClickListener, TextWatcher, PhoneCallback, FirebaseCallback {

    private static final String TAG = "SignupVerification";
    private FragmentPhoneCodeVerificationBinding binding;
    private EditText inputPhoneCode;
    private Button btSubmit;
    private TextView header, sendAgain;
    private ImageView imageView;
    private ProgressDialog dialog;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private Admin admin;
    private Data localData;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentPhoneCodeVerificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inputPhoneCode = binding.etPhoneCode;
        btSubmit = binding.btSubmit;
        header = binding.textPhoneCode;
        sendAgain = binding.tvSendAgain;
        imageView = binding.ivPhoneIcon;
        localData = Data.getInstance();
        mVerificationId = requireArguments().getString("verificationId");

        admin = (Admin) localData.getCurrentUser();

        dialog = new ProgressDialog(root.getContext());


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
                localData.verifyPhoneNumberWithCode(code, mVerificationId, this);
                break;
            case R.id.tv_send_again:
                String phone = "+" + requireArguments().getString("countryCode") + requireArguments().getString("phone").substring(1);
                setDialog("Resend Verification Code");
                localData.resendVerificationCode(phone, mResendToken, getActivity());
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
    public void onCallback(boolean success, Exception e, com.google.android.gms.tasks.Task<AuthResult> task) {
        if (success) {
            dialog.dismiss();
            Log.d(TAG, "signInWithCredential:success");
            FirebaseUser firebaseUser = task.getResult().getUser();

            UserModel userModel = new User(
                    task.getResult().getUser().getUid(),
                    getArguments().getString("userName"),
                    getArguments().getString("name"),
                    getArguments().getString("lastName"),
                    getArguments().getString("phone"),
                    "Welcome ToCare " + getArguments().get("userName"),
                    "https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a",
                    false);
            localData.createUserData(firebaseUser, userModel,this);
        } else {
            Log.w(TAG, "signInWithCredential:failure", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                ((ManageUsersActivity) getActivity()).swapFragmentByFragmentClass(UsersFragment.class, null);
            }
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
        dialog.dismiss();
    }

    @Override
    public void onVerificationCompleted(boolean success, @NonNull PhoneAuthCredential credential) {
        Log.d(TAG, "onVerificationCompleted:" + credential);
        localData.verifyPhoneNumberWithCode(credential.getSmsCode(), mVerificationId, this);
    }

    @Override
    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {

        Log.d(TAG, "onCodeSent:" + verificationId);
        Toast.makeText(getContext(), "Code sent ", Toast.LENGTH_SHORT).show();
        dialog.dismiss();

        mVerificationId = verificationId;
        mResendToken = token;
    }

    @Override
    public void onCallback(boolean success, Exception e,FirebaseUser firebaseUser) {
        if(success){
            admin.addNewUser(firebaseUser.getUid());

            localData.updateChildren(this,firebaseUser.getUid());
            Log.d(TAG, "DocumentReference:success");
            Toast.makeText(getContext(), "The details have been successfully registered", Toast.LENGTH_SHORT).show();

        }else {

            dialog.dismiss();
            Log.d(TAG, "DocumentReference:failed");
            Toast.makeText(getContext(), "The details were not successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onSuccess(boolean success, Exception e) {
        if(success){
            Log.d(TAG, "DocumentSnapshot successfully updated!");
        }else{
            Log.w(TAG, "Error updating document", e);
        }
            ((ManageUsersActivity) getActivity()).swapFragmentByFragmentClass(UsersFragment.class, null);
    }

    @Override
    public void onComplete(boolean success, Exception e) {

    }
}
