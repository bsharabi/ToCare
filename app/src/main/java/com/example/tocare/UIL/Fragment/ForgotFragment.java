package com.example.tocare.UIL.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tocare.BLL.Validation.UserValidation;
import com.example.tocare.Controller.LoginActivity;
import com.example.tocare.databinding.FragmentForgotBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotFragment extends Fragment implements View.OnClickListener {

    private FragmentForgotBinding binding;
    private static final String TAG = "ForgotFragment";
    private TextView inputEmail;
    private Button btSubmit;
    private ProgressDialog dialog;
    private LoginActivity loginActivity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentForgotBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loginActivity = (LoginActivity) getActivity();

        final TextView textView = binding.textForgot;

        inputEmail = binding.etEmail;
        btSubmit = binding.btSubmit;

        btSubmit.setOnClickListener(this);
        dialog = new ProgressDialog(root.getContext());


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btSubmit.setEnabled(false);
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (UserValidation.isValidEmail(inputEmail.getText().toString()))
                    btSubmit.setEnabled(true);
                else
                    btSubmit.setEnabled(false);

                System.out.println("------------------------------------------------------------");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void forgotPassword(String email) {
        dialog.setMessage("Please wait while the link is sent to your email");
        dialog.setTitle("Password Reset...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "sendPasswordResetEmail:success");
                Toast.makeText(getContext(), "A password reset email has been sent to you", Toast.LENGTH_LONG).show();
                if (getActivity() instanceof LoginActivity) {
                    ((LoginActivity) getActivity()).swapFragmentByPosition(0);
                }
                dialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "sendPasswordResetEmail:failed");
            Toast.makeText(getContext(), "send link failed " + e.getMessage(), Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        System.out.println("onDestroyView Function");
    }

    @Override
    public void onClick(View view) {
        String email = inputEmail.getText().toString().trim();
        forgotPassword(email);

    }

    //----------------------------------------------Getter&&Setter---------------------------------------


}