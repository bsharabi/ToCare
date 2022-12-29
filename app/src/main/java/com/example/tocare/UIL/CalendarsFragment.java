package com.example.tocare.UIL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tocare.databinding.FragmentCalendarsBinding;

public class CalendarsFragment extends Fragment {
    private static CalendarsFragment single_instance = null;
    private FragmentCalendarsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCalendarsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCalendars;
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