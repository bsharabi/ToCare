package com.example.tocare.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tocare.Adapters.ProfileAdapter;
import com.example.tocare.R;
import com.example.tocare.databinding.FragmentProfileBinding;
import com.google.android.material.tabs.TabLayout;

public class ProfileFragment extends Fragment {
    private static ProfileFragment single_instance = null;
    private FragmentProfileBinding binding;
    TabLayout tabLayout;
    ViewPager2 viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;



        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabLayout = view.findViewById(R.id.tab_layout_profile);
        viewPager = view.findViewById(R.id.view_pager_profile);


        tabLayout.addTab(tabLayout.newTab().setText("Basic"));
        tabLayout.addTab(tabLayout.newTab().setText("Advance"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ProfileAdapter adapter = new ProfileAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }


        });


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public ProfileFragment() {
    }

    public static ProfileFragment getInstance() {
        if (single_instance == null)
            single_instance = new ProfileFragment();

        return single_instance;
    }
}