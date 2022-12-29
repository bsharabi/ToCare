package com.example.tocare.UIL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tocare.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {
    private static NotificationsFragment single_instance = null;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public NotificationsFragment() {
    }

    public static NotificationsFragment getInstance() {
        if (single_instance == null)
            single_instance = new NotificationsFragment();

        return single_instance;
    }
}