package com.example.tocare.UIL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tocare.BLL.Listener.OnChangeCallback;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private Data localData;
    private EditText name, lastName, userName, bio;
    private ImageView image_profile;
    private TextView edit_picture;
    private TextView done, cancel;
    private ProgressDialog dialog;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        localData = Data.getInstance();
        name = findViewById(R.id.et_name);
        lastName = findViewById(R.id.et_lastName);
        userName = findViewById(R.id.et_userName);
        bio = findViewById(R.id.et_bio);
        done = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel);
        edit_picture = findViewById(R.id.edit_picture);
        image_profile = findViewById(R.id.image_edit_profile);

        dialog = new ProgressDialog(this);

        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String profileId = sharedPreferences.getString("profileId", null);
        boolean isChild = sharedPreferences.getBoolean("isChild", false);

        done.setOnClickListener(v -> {
            setDialog("Update");
            Map<String, Object> data = new HashMap<>();
            data.put("name",name.getText().toString()) ;
            data.put("lastName", lastName.getText().toString());
            data.put("userName", userName.getText().toString());
            data.put("bio",bio.getText().toString() );
            localData.updateUserById(profileId, data, () -> {
                dialog.dismiss();
                finish();
            });
        });
        cancel.setOnClickListener(v -> finish());


        localData.getUserById(profileId, (success, userModel) -> {
            if (success) {
                user = userModel;
                userName.setText(userModel.getUserName());
                lastName.setText(userModel.getLastName());
                name.setText(userModel.getName());
                bio.setText(userModel.getBio());
                Picasso.get().load(userModel.getImageUrl()).into(image_profile);
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setDialog(String msg) {
        dialog.setMessage("Please wait while Registration");
        dialog.setTitle(msg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}