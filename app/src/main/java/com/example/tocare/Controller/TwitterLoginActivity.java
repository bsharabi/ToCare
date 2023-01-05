package com.example.tocare.Controller;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.tocare.BLL.Model.Admin;
import com.example.tocare.DAL.Auth;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Listener.TwitterCallback;
import com.example.tocare.databinding.ActivityTwitterLoginBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;



public class TwitterLoginActivity extends AppCompatActivity implements TwitterCallback {

    private static final String TAG = "TwitterLoginActivity";
    private ProgressDialog dialog;
    private ActivityTwitterLoginBinding binding;
    private ImageView imTwitter;
    private Auth login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTwitterLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        login = Auth.getInstance();

        final float alpha = 0;
        imTwitter = binding.imTwitter;
        imTwitter.setTranslationY(300);
        imTwitter.setAlpha(alpha);
        imTwitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setTitle("Login with Twitter");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        UserModel userModel = new Admin(
                "",
                "",
                "",
                "",
                "",
                "Hello my name is ",
                "https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a",
                true);
        SignInWithTwitter(userModel);
    }

    private void SignInWithTwitter(UserModel userModel) {
        Log.w(TAG, "signInWithTwitter:create OAuth Provider");
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        provider.addCustomParameter("lang", "en");
        login.handleSignInResult(provider,this,this,userModel);
    }

    @Override
    public void onCallback(boolean success, Exception e, FirebaseUser user) {
        dialog.dismiss();
        if (success) {
            Log.w(TAG, "signInWithTwitter:success");
            Toast.makeText(this, "Successfully", Toast.LENGTH_LONG);
            reload(MainActivity.class);
        } else {
            Log.w(TAG, "signInWithTwitter:failed", e);
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG);
            reload(LoginActivity.class);
        }
    }

    @Override
    public void onSuccess(boolean success, Exception e) {
        if (success) {
            dialog.dismiss();
            Log.d(TAG, "DocumentReference::User::success");
            reload(MainActivity.class);
        } else {
            dialog.dismiss();
            Log.d(TAG, "DocumentReference::User::failed");
            Toast.makeText(this, "The details were not successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();
            reload(LoginActivity.class);
        }
    }


    public void reload(Class<?> name) {
        Intent intent = new Intent(TwitterLoginActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}