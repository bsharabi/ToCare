package com.example.tocare.UIL.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tocare.BLL.Adapters.UserEditAdapter;
import com.example.tocare.DAL.Data;
import com.example.tocare.BLL.Listener.Refresh;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.Controller.ManageUsersActivity;
import com.example.tocare.databinding.FragmentCardUserBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;


public class UserCardFragment extends Fragment implements TabLayout.OnTabSelectedListener, Refresh {

    private static final String TAG = "UsersFragment";
    private FragmentCardUserBinding binding;
    private MaterialToolbar materialToolbar;
    private ManageUsersActivity manageUsersActivity;
    private Data localData;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private UserModel user;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCardUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        manageUsersActivity = (ManageUsersActivity) getActivity();

        materialToolbar = binding.topAppBar;
        tabLayout = binding.tabLayoutProfile;
        viewPager = binding.viewPagerProfile;

        tabLayout.addTab(tabLayout.newTab().setText("Basic"));
        tabLayout.addTab(tabLayout.newTab().setText("Tasks"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        localData = Data.getInstance();

        String UserID;
        assert getArguments() != null;
        UserID = getArguments().getString("userID");

        user = localData.getUserById(UserID);
        final UserEditAdapter adapter = new UserEditAdapter(getActivity().getSupportFragmentManager(), getLifecycle(), user);
        viewPager.setAdapter(adapter);

        localData.setRefresh(this);

        tabLayout.addOnTabSelectedListener(this);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


        binding.tvUserName.setText("Welcome " + user.getUserName());
        ImageView imageView = binding.imgViewProfile;
        String imageURL = ("https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a");
        Picasso.get().load(imageURL).into(imageView);


        materialToolbar.setNavigationOnClickListener(v -> {
            manageUsersActivity.getSupportFragmentManager().restoreBackStack("replacement");
            manageUsersActivity.swapFragmentByFragmentClass(UsersFragment.class, null);

        });

        return root;
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void refresh() {
        Bundle bundle = new Bundle();
        bundle.putString("userID", user.getId());
        manageUsersActivity.swapFragmentByFragmentClass(UserCardFragment.class, bundle);
    }
}