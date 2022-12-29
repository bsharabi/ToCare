package com.example.tocare.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;

import com.example.tocare.R;
import com.example.tocare.DAL.Data;
import com.example.tocare.BLL.Listener.Refresh;
import com.example.tocare.UIL.users.UsersFragment;



public class ManageUsersActivity extends AppCompatActivity implements Refresh {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        Data.getInstance().setRefresh(this);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, UsersFragment.class, null)
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
        swapFragmentByFragmentClass(UsersFragment.class,null);
    }
}