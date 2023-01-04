package com.example.tocare.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import com.example.tocare.R;
import com.example.tocare.BLL.Listener.Refresh;
import com.example.tocare.UIL.Fragment.UsersManageFragment;



public class ManageUsersActivity extends AppCompatActivity implements Refresh {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, UsersManageFragment.class, null)
                .commit();
    }


    public void swapFragmentByFragmentClass(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, fragmentClass, bundle)
                .setReorderingAllowed(true)
                .addToBackStack(fragmentClass.getName())
                .commit();
    }

    @Override
    public void refresh() {
        swapFragmentByFragmentClass(UsersManageFragment.class,null);
    }
}