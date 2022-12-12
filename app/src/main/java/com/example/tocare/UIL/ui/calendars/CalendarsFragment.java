package com.example.tocare.UIL.ui.calendars;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tocare.databinding.FragmentCalendarsBinding;
import com.example.tocare.databinding.FragmentNotificationsBinding;

public class CalendarsFragment extends Fragment {
    private static CalendarsFragment single_instance = null;
    private FragmentCalendarsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalendarsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(CalendarsViewModel.class);

        binding = FragmentCalendarsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCalendars;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public CalendarsFragment() {
    }

    public static CalendarsFragment getInstance() {
        if (single_instance == null)
            single_instance = new CalendarsFragment();

        return single_instance;
    }
}