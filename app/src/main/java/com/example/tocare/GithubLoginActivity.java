package com.example.tocare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tocare.BLL.Validation.UserValidation;
import com.example.tocare.databinding.ActivityGithubLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GithubLoginActivity extends AppCompatActivity {
    private ActivityGithubLoginBinding binding;
    private EditText inputEmail;
    private Button btLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGithubLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inputEmail = binding.etEmail;
        btLogin = binding.btLogin;
        mAuth = FirebaseAuth.getInstance();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignInWithGitHub();
            }
        });

    }

    private void SignInWithGitHub() {

        String email = inputEmail.getText().toString().trim();
        if (UserValidation.isValidEmail(email)) {
            handleSignInResult(email);
        } else {
            Toast.makeText(GithubLoginActivity.this, "Email error", Toast.LENGTH_LONG).show();
        }


    }

    private void handleSignInResult(String email) {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
        provider.addCustomParameter("login", email);

        List<String> scopes =
                new ArrayList<String>() {
                    {
                        add("user:email");
                    }
                };
        provider.setScopes(scopes);

        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    Toast.makeText(GithubLoginActivity.this, " error", Toast.LENGTH_LONG).show();
                                }
                            });
        } else {
            mAuth
                    .startActivityForSignInWithProvider(/* activity= */ GithubLoginActivity.this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    System.out.println("-------------------------------------"+authResult.getUser().getDisplayName());
                                    reload(MainActivity.class);
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    Toast.makeText(GithubLoginActivity.this, " error", Toast.LENGTH_LONG).show();
                                }
                            });
        }
    }

    private void updateGithubData(FirebaseUser firebaseUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
//        if (account != null) {
//            UserModel userModel = new Admin(
//                    firebaseUser.getUid(),
//                    account.getDisplayName(),
//                    account.getGivenName(),
//                    account.getFamilyName(),
//                    "",
//                    "Hello my name is " + account.getDisplayName(),
//                    "https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a",
//                    true);
//            DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
//                    .document(firebaseUser.getUid());
//            reference.set(userModel)
//                    .addOnCompleteListener(task1 -> {
//                        if (task1.isSuccessful()) {
//                            dialog.dismiss();
//                            Log.d(TAG, "DocumentReference:success");
//                            Toast.makeText(this, "The details have been successfully registered", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(e -> {
//                        dialog.dismiss();
//                        Log.d(TAG, "DocumentReference:failed");
//                        Toast.makeText(this, "The details were not successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    });
//        }

    }

    public void reload(Class<?> name) {
        Intent intent = new Intent(GithubLoginActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}