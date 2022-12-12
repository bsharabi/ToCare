package com.example.tocare.UIL.ui.profile.tabs.profileBasic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tocare.databinding.FragmentNotificationsBinding;
import com.example.tocare.databinding.FragmentProfileBasicBinding;

public class ProfileBasicFragment extends Fragment {

    private FragmentProfileBasicBinding binding;
    private static ProfileBasicFragment single_instance = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileBasicViewModel profileBasicViewModel =
                new ViewModelProvider(this).get(ProfileBasicViewModel.class);

        binding = FragmentProfileBasicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfileBasic;
        final TextView textBio = binding.tvBio;
        profileBasicViewModel.getBio().observe(getViewLifecycleOwner(), textView::setText);
        profileBasicViewModel.getText().observe(getViewLifecycleOwner(), textBio::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public ProfileBasicFragment() {
    }

    public static ProfileBasicFragment getInstance() {
        if (single_instance == null)
            single_instance = new ProfileBasicFragment();

        return single_instance;
    }
}