package com.example.tocare.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tocare.databinding.FragmentFeedBinding;
import com.example.tocare.ui.login.LoginFragment;


public class FeedsFragment extends Fragment {
    private static FeedsFragment single_instance = null;
    private FragmentFeedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FeedsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(FeedsViewModel.class);

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textFeeds;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public FeedsFragment() {
    }

    public static FeedsFragment getInstance() {
        if (single_instance == null)
            single_instance = new FeedsFragment();

        return single_instance;
    }
}