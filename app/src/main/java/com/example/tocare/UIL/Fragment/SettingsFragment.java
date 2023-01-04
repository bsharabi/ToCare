package com.example.tocare.UIL.Fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.tocare.R;
import com.example.tocare.databinding.FragmentSettingBinding;

public class SettingsFragment extends PreferenceFragment {
    private static SettingsFragment single_instance = null;
    private FragmentSettingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        addPreferencesFromResource(R.xml.preferences);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public SettingsFragment() {
    }

    public static SettingsFragment getInstance() {
        if (single_instance == null)
            single_instance = new SettingsFragment();

        return single_instance;
    }
}