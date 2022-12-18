package com.example.tocare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import com.example.tocare.UIL.ui.phone.PhoneFragment;

public class PhoneLoginActivity extends AppCompatActivity {

    public PhoneLoginActivity() {
        super(R.layout.activity_phone_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt("some_int", 0);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, PhoneFragment.class, null)
                    .commit();

        }
    }

    public void swapFragmentByFragmentClass(Class<? extends Fragment> fragmentClass,Bundle bundle) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, fragmentClass, bundle)
                .setReorderingAllowed(true)
                .addToBackStack(fragmentClass.getName())
                .commit();
    }

    public void reload(Class<?> name) {
        Intent intent = new Intent(PhoneLoginActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}