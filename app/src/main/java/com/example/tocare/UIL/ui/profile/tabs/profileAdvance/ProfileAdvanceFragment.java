package com.example.tocare.UIL.ui.profile.tabs.profileAdvance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tocare.databinding.FragmentNotificationsBinding;
import com.example.tocare.databinding.FragmentProfileAdvanceBinding;

public class ProfileAdvanceFragment extends Fragment {

    private FragmentProfileAdvanceBinding binding;
    private static ProfileAdvanceFragment single_instance = null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileAdvanceViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ProfileAdvanceViewModel.class);

        binding = FragmentProfileAdvanceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfileAdvance;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public ProfileAdvanceFragment() {
    }
    public static ProfileAdvanceFragment getInstance() {
        if (single_instance == null)
            single_instance = new ProfileAdvanceFragment();

        return single_instance;
    }
}