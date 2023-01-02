package com.example.tocare.UIL.users;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tocare.BLL.Adapters.UserAdapter;
import com.example.tocare.DAL.Data;
import com.example.tocare.BLL.Listener.Refresh;
import com.example.tocare.Controller.ManageUsersActivity;
import com.example.tocare.UIL.signup.SignupUserFragment;
import com.example.tocare.databinding.FragmentUsersBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class UsersFragment extends Fragment  implements Refresh {

    private static final String TAG = "UsersFragment";
    private FragmentUsersBinding binding;
    private ManageUsersActivity manageUsersActivity;
    private FloatingActionButton addUser;
    private MaterialToolbar materialToolbar;
    private RecyclerView rvUsers;
    private UserAdapter usersAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        manageUsersActivity = (ManageUsersActivity) getActivity();
        rvUsers = binding.idRVUser;
        addUser = binding.fabAddUser;
        materialToolbar = binding.topAppBar;

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        usersAdapter = new UserAdapter(getContext(),manageUsersActivity);

        Data.getInstance().setRefresh(this);
        rvUsers.setLayoutManager(linearLayoutManager);
        rvUsers.setAdapter(usersAdapter);

        materialToolbar.setNavigationOnClickListener(v -> {
            manageUsersActivity.finish();
        });
        addUser.setOnClickListener(v -> {
            manageUsersActivity.swapFragmentByFragmentClass(SignupUserFragment.class, null);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void refresh() {
        usersAdapter.notifyDataSetChanged();
    }
}