package com.example.tocare.UIL.ui.users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.tocare.BLL.Departments.User;
import com.example.tocare.ManageUsersActivity;
import com.example.tocare.databinding.FragmentCardUserBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class UserCardFragment extends Fragment {

//להוסיף פרגמנט של משימה עם viewpager2
    private static final String TAG = "UsersFragment";
    private FragmentCardUserBinding binding;
    private MaterialToolbar materialToolbar;
    private ManageUsersActivity manageUsersActivity;
    private EditText bio, name, lastName, userName;
    private Button btSubmit;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCardUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        manageUsersActivity = (ManageUsersActivity) getActivity();
        materialToolbar = binding.topAppBar;
        bio = binding.ttvBio;
        name = binding.etName;
        lastName = binding.etLastName;
        userName = binding.etUserName;
        btSubmit = binding.btSubmit;
        User user;


        user = (User) getArguments().getSerializable("User");
        binding.tvUserName.setText("Welcome " + user.getUserName());
        ImageView imageView = binding.imgViewProfile;
        String imageURL = ("https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a");
        Picasso.get().load(imageURL).into(imageView);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                updateUser(user);
            }
        });
        bio.setText(user.getBio());
        name.setText(user.getName());
        lastName.setText(user.getLastName());
        userName.setText(user.getUserName());

        lastName.setEnabled(false);
        name.setEnabled(false);
        userName.setEnabled(false);
        bio.setEnabled(false);

        materialToolbar.setNavigationOnClickListener(v -> {

            manageUsersActivity.swapFragmentByFragmentClass(UsersFragment.class, null);

        });

        return root;
    }

    private void updateUser(User user) {

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


}