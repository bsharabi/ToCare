package com.example.tocare.UIL.profile.tabs.profileBasic;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tocare.DAL.Data;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.databinding.FragmentProfileBasicBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileBasicFragment extends Fragment {

    private FragmentProfileBasicBinding binding;
    private EditText bio, name, lastName, userName;
    private Button btSubmit;
    private static final String TAG = "ProfileBasicFragment";
    private UserModel user;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileBasicViewModel profileBasicViewModel =
                new ViewModelProvider(this).get(ProfileBasicViewModel.class);

        binding = FragmentProfileBasicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfileBasic;


        profileBasicViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        bio = binding.ttvBio;
        name = binding.etName;

        lastName = binding.etLastName;
        userName = binding.etUserName;
        btSubmit = binding.btSubmit;

        btSubmit.setOnClickListener(v -> updateUser(user));
        bio.setText(user.getBio());
        name.setText(user.getName());
        lastName.setText(user.getLastName());
        userName.setText(user.getUserName());

        lastName.setEnabled(false);
        name.setEnabled(false);
        userName.setEnabled(false);
        bio.setEnabled(false);
        return root;
    }

    private void updateUser(UserModel user) {

        user.setUserName(userName.getText().toString());
        user.setBio(bio.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setName(name.getText().toString());

        FirebaseFirestore.getInstance().collection("User")
                .document(user.getId())
                .set(user)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {

                        Log.d(TAG, "DocumentReference:success");
                        Toast.makeText(getContext(), "The details have been successfully registered", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "DocumentReference:failed");
                    Toast.makeText(getContext(), "The details were not successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public ProfileBasicFragment() {
        user = Data.getInstance().getCurrentUser();
    }

    public ProfileBasicFragment(UserModel user) {
        this.user = user;
    }

}