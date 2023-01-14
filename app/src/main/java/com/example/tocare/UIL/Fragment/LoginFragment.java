package com.example.tocare.UIL.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tocare.BLL.Adapters.LoginAdapter;
import com.example.tocare.Controller.FacebookLoginActivity;
import com.example.tocare.Controller.GithubLoginActivity;
import com.example.tocare.Controller.GoogleLoginActivity;
import com.example.tocare.Controller.TwitterLoginActivity;
import com.example.tocare.R;
import com.example.tocare.UIL.phone.PhoneLoginFragment;
import com.example.tocare.databinding.FragmentLoginBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


public class LoginFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    private static final String TAG = "LoginFragment";
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton facebook, google, github, twitter, phone;
    private ImageView heart, handLeft, handRight, hands;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentLoginBinding binding = FragmentLoginBinding.inflate(inflater, container, false);

        tabLayout = binding.tabLayout;
        viewPager = binding.viewPager;
        facebook = binding.fabFacebook;
        google = binding.fabGoogle;
        github = binding.fabGithub;
        twitter = binding.fabTwitter;
        heart = binding.imgViewHeart;
        handLeft = binding.imgViewLeftHand;
        handRight = binding.imgViewRightHand;
        hands = binding.imgViewHands;
        phone = binding.fabPhone;

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(requireActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout.setTranslationY(300);
        handLeft.setTranslationY(-300);
        handRight.setTranslationX(300);
        heart.setRotation(360);
        hands.setRotation(-360);

        final float alpha = 0;

        hands.setAlpha(alpha);
        heart.setAlpha(alpha);

        tabLayout.setAlpha(alpha);
        handLeft.setAlpha(alpha);
        handRight.setAlpha(alpha);

        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
        hands.animate().rotation(0).alpha(1).setDuration(1000).setStartDelay(350).start();
        heart.animate().rotation(0).alpha(1).setDuration(1000).setStartDelay(350).start();
        handRight.animate().translationX(80).alpha(1).setDuration(1000).setStartDelay(300).start();
        handLeft.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(300).start();

        startAnimationFabUp();

        facebook.setOnClickListener(this);
        google.setOnClickListener(this);
        github.setOnClickListener(this);
        twitter.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(this);
        phone.setOnClickListener(this);

    }

    public void swapFragmentByPosition(int position) {
        viewPager.setCurrentItem(position);
        startAnimationFabDown();
    }

    private void startAnimationFabUp() {

        phone.setTranslationY(300);
        facebook.setTranslationY(300);
        google.setTranslationY(300);
        twitter.setTranslationY(300);

        github.setTranslationY(300);

        final float alpha = 0;


        facebook.setAlpha(alpha);
        google.setAlpha(alpha);
        phone.setAlpha(alpha);
        twitter.setAlpha(alpha);
        github.setAlpha(alpha);

        facebook.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(550).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(650).start();
        phone.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(750).start();
        twitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(850).start();
        github.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(950).start();
    }

    private void startAnimationFabDown() {

        facebook.animate().translationY(500).alpha(1).setDuration(0).setStartDelay(300).start();
        google.animate().translationY(500).alpha(1).setDuration(0).setStartDelay(250).start();
        phone.animate().translationY(500).alpha(1).setDuration(0).setStartDelay(200).start();
        twitter.animate().translationY(500).alpha(1).setDuration(0).setStartDelay(150).start();
        github.animate().translationY(500).alpha(1).setDuration(0).setStartDelay(50).start();
    }

    @Override
    public void onTabSelected(@NonNull TabLayout.Tab tab) {
        int pos = tab.getPosition();
        swapFragmentByPosition(pos);
        if (pos == 0) {
            startAnimationFabUp();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void reload(Class<?> name) {
        Log.d(TAG, "Reload:nextScreen");
        Intent intent = new Intent(getActivity(), name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.fab_google:
                reload(GoogleLoginActivity.class);
                break;
            case R.id.fab_github:
                reload(GithubLoginActivity.class);
                break;
            case R.id.fab_facebook:
                reload(FacebookLoginActivity.class);
                break;
            case R.id.fab_twitter:
                reload(TwitterLoginActivity.class);
                break;
            case R.id.fab_phone:
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Login via phone")
                        .setMessage("Attempting to log in using this method is only possible for the user and not for the admin.\n" +
                                "Would you still like to continue?")
                        .setPositiveButton("Continue", (dialogInterface, i) -> requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container_login, new PhoneLoginFragment()).commit())
                        .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        })
                        .show();

                break;
            default:
                break;
        }

    }


}
