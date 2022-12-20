package com.example.tocare.BLL;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tocare.BLL.Departments.UserModel;
import com.example.tocare.BLL.Validation.UserValidation;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Authenticate {
    private static ProgressDialog dialog;
    private static final String TAG_SIGN_UP = "SignUp";
    private static final String TAG_SIGN_IN = "SignIn";
    private static final String TAG_UPDATE = "UpdateProfile";
    private static final String TAG_FORGOT_PASSWORD = "ForgotPassword";
    private static final String TAG_MAIN = "MainActivity";
    private UserModel userModel;
    private FirebaseUser firebaseUser;

    public Authenticate(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public void signInController(String emailOrPhone, String password, Context context) {

        dialog = new ProgressDialog(context);
        String ans = UserValidation.checkIsEmailOrPhoneNumber(emailOrPhone);
        switch (ans) {
            case "Email":
                signInWithEmail(emailOrPhone, password, context);
                break;
            case "Phone":
                SignInWithPhone(emailOrPhone, context);
                break;
            default:
//                LoginFragment.getInstance().getInputPassword().setError("Invalid argument");
                break;
        }

    }

    private void signInWithEmail(String email, String password, @NonNull Context context) {
        dialog.setMessage("Please wait while Login with email");
        dialog.setTitle("Login...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG_SIGN_IN, "signInWithEmail:success");
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                updateUI(context);

            }
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    public void createAccountWithEmail(String email, String password, String userName, String phone, @NonNull Context context) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG_SIGN_UP, "createUserWithEmail:success");
                        firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();
                        firebaseUser.updateEmail(email);
                        firebaseUser.updatePassword(password);
                        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                        builder.setDisplayName(userName);
                        firebaseUser.updateProfile(builder.build());
                        updateUI(context);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG_SIGN_UP, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(context);
                    }
                });
        // [END create_user_with_email]
    }

    public void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    // Email sent
                });
        // [END send_email_verification]
    }


    public void isEmailVerified() {
        // Send verification email
        // [START send_email_verification]
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    // Email sent
                });
        // [END send_email_verification]
    }

    private void updateUI(@NonNull Context context) {
        if (firebaseUser != null) {
            System.out.println(firebaseUser.getDisplayName());
            DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                    .document(firebaseUser.getUid());
            reference.addSnapshotListener((value, error) -> {
                dialog.dismiss();
                userModel = (UserModel) value.toObject(UserModel.class);
                Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show();

            });
        } else {
            Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show();
        }

    }

    public static boolean SignInWithPhone(String phoneNumber, @NonNull Context context) {
        return true;
    }

    public void createAccountWithPhone(String email, String password, String userName, String phone, @NonNull Context context) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG_SIGN_UP, "createUserWithEmail:success");
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        firebaseUser.updateEmail(email);
                        firebaseUser.updatePassword(password);
                        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                        builder.setDisplayName(userName);
                        firebaseUser.updateProfile(builder.build());
                        updateUI(context);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG_SIGN_UP, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(context);
                    }
                });
        // [END create_user_with_email]
    }

    private void updateUserProfile(String name, String url) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(url))
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG_UPDATE, "User profile updated.");
                    }
                });
        firebaseUser.updateEmail("user@example.com")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG_UPDATE, "User email address updated.");
                    }
                });
    }

    public void updatePassword(String newPassword) {
        if (firebaseUser != null)
            firebaseUser.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG_MAIN, "User password updated.");
                        }
                    });
        else {
            Log.d(TAG_MAIN, "User password updated is failed.");
        }

    }

    public void ResetPassword(String EmailAddress) {
       FirebaseAuth.getInstance()
               .sendPasswordResetEmail(EmailAddress)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG_SIGN_IN, "Email sent.");
                    }
                });
    }

    public void DeleteUser() {
        if (firebaseUser != null)
            firebaseUser.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG_MAIN, "User account deleted.");
                        }
                    });
    }

    public void reAuth(String email, String password) {

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);

        // Prompt the user to re-provide their sign-in credentials
        firebaseUser.reauthenticate(credential)
                .addOnCompleteListener(task -> Log.d(TAG_MAIN, "User re-authenticated."));


    }
}
