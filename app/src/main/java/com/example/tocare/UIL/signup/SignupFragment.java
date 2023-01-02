package com.example.tocare.UIL.signup;

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
import com.example.tocare.BLL.Model.Admin;
import com.example.tocare.DAL.Login;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Validation.UserValidation;
import com.example.tocare.BLL.Listener.FirebaseCallback;
import com.example.tocare.Controller.LoginActivity;
import com.example.tocare.R;
import com.example.tocare.databinding.FragmentSignupBinding;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;


public class SignupFragment extends Fragment implements View.OnClickListener, FirebaseCallback {


    private static final String TAG = "Signup";
    private EditText inputName, inputLastName, inputPhone, inputEmail, inputPassword, inputConformPassword, inputUserName;
    private Button btSignup;
    private ProgressDialog dialog;
    private FragmentSignupBinding binding;
    private LoginActivity loginActivity;
    private CountryCodePicker ccp;
    private Login login;

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
        login = Login.getInstance();
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

    private void setDialog(String msg) {
        dialog.setMessage("Please wait while Registration");
        dialog.setTitle(msg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void buildUser() {
        String name = inputName.getText().toString();
        String lastName = inputLastName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String cPassword = inputConformPassword.getText().toString();
        String phone = inputPhone.getText().toString();
        String userName = inputUserName.getText().toString();
        UserModel userModel = new Admin(
                "",
                userName,
                name,
                lastName,
                ccp.getFullNumberWithPlus(),
                "Hello my name is " + userName,
                "https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a",
                true);
//        if (UserValidation.SignUpValidation(email, password, userName, lastName, phone, cPassword) && ccp.isValidFullNumber()) {
            login.createAccountWithEmail(email, password, userModel, this);
            loginActivity.hideKeyboard();
//        }
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.bt_signup:
                setDialog("Register");
                buildUser();
                break;
            default:
                break;
        }

    }

    @Override
    public void onCallback(boolean success, Exception e, FirebaseUser user) {
        if (success) {
            Log.w(TAG, "createUserWithEmail:success");
            Toast.makeText(getContext(), "Registration successful Go confirm your email", Toast.LENGTH_SHORT).show();
        } else {
            dialog.dismiss();
            Log.w(TAG, "createUserWithEmail:failure");
            Toast.makeText(getContext(), "Authentication failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(boolean success, Exception e) {
        if (success) {
            dialog.dismiss();
            Log.d(TAG, "DocumentReference::User::success");
            Toast.makeText(getContext(), "The details have been successfully", Toast.LENGTH_SHORT).show();
            loginActivity.swapFragmentByPosition(0);
        } else {
            dialog.dismiss();
            Log.d(TAG, "DocumentReference::User::failed");
            Toast.makeText(getContext(), "The details were not successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}