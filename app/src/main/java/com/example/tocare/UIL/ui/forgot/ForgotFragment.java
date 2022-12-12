package com.example.tocare.UIL.ui.forgot;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tocare.UIL.ui.Activities.LoginActivity;
import com.example.tocare.UIL.ui.login.LoginFragment;
import com.example.tocare.databinding.FragmentAdminBinding;
import com.example.tocare.databinding.FragmentForgotBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotFragment extends Fragment implements View.OnClickListener {

    private FragmentForgotBinding binding;
    private static ForgotFragment single_instance = null;
    private static final String TAG = "ForgotFragment";
    private TextView inputEmail;
    private Button btSubmit;
    private ProgressDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ForgotViewModel forgotViewModel = new ViewModelProvider(this).get(ForgotViewModel.class);

        binding = FragmentForgotBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textForgot;
        forgotViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        inputEmail = binding.etEmail;
        btSubmit = binding.btSubmit;
        btSubmit.setOnClickListener(this);
        dialog = new ProgressDialog(root.getContext());
        return root;
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
                LoginActivity.getInstance().getViewPager().setCurrentItem(0);
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

    private ForgotFragment() {

    }

    public static ForgotFragment getInstance() {
        if (single_instance == null)
            single_instance = new ForgotFragment();
        return single_instance;
    }

    @Override
    public void onClick(View view) {
        String email = inputEmail.getText().toString().trim();
        forgotPassword(email);
    }
}