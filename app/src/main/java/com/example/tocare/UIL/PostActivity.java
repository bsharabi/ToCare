package com.example.tocare.UIL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.tocare.BLL.Listener.UploadCallback;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.Controller.MainActivity;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;


public class PostActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, ViewSwitcher.ViewFactory, UploadCallback {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int PICK_IMAGE_MULTIPLE = 1;

    private int position = 0;

    private EditText type, description, startDate, endDate;
    private ImageSwitcher imageSwitcher;
    private ArrayList<Uri> mArrayUri;
    private ArrayList<String> mArrayUrl;
    private Data localData;
    private ProgressDialog dialog;
    private ImageButton btPrevious, btNext;


    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        localData = Data.getInstance();

        ImageView close = findViewById(R.id.close);
        TextView post = findViewById(R.id.tv_post);

        dialog = new ProgressDialog(this);

        btPrevious = findViewById(R.id.bt_previous);
        btNext = findViewById(R.id.bt_next);

        btPrevious.setVisibility(View.GONE);
        btNext.setVisibility(View.GONE);

        ImageButton btGallery = findViewById(R.id.bt_gallery);
        ImageButton btCamera = findViewById(R.id.bt_camera);

        imageSwitcher = findViewById(R.id.image_select);
        imageSwitcher.setVisibility(View.GONE);

        type = findViewById(R.id.type);
        description = findViewById(R.id.description);
        startDate = findViewById(R.id.dateStart);
        endDate = findViewById(R.id.dateEnd);
        SeekBar priority = findViewById(R.id.seek_bar);
        priority.setOnClickListener(this);

        startDate.setInputType(InputType.TYPE_NULL);
        endDate.setInputType(InputType.TYPE_NULL);

        mArrayUri = new ArrayList<>();
        mArrayUrl = new ArrayList<>();

        imageSwitcher.setFactory(this);
        startDate.setOnTouchListener(this);
        endDate.setOnTouchListener(this);
        imageSwitcher.setOnClickListener(this);
        post.setOnClickListener(this);
        close.setOnClickListener(this);
        btPrevious.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btGallery.setOnClickListener(this);
        btCamera.setOnClickListener(this);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadMultiImage() {
        if (!mArrayUri.isEmpty()) {
            dialog.setMessage("Posting... ");
            dialog.show();
            String postId = localData.getRandomIdByCollectionName("Posts");
            for (Uri uri : mArrayUri) {
                localData.uploadImage(uri, getFileExtension(uri), postId, this);
            }
        } else {
            Toast.makeText(this, "Must select a picture", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The CAMERA permission has been granted, you can start the camera activity
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } else {
                // The CAMERA permission has been denied, you can show a message to the user
                Toast.makeText(this, "Camera permission is required to capture an image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            Uri imageUrl;
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                int count = mClipData.getItemCount();
                for (int i = 0; i < count; i++) {
                    imageUrl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageUrl);
                }
            } else {
                imageUrl = data.getData();
                mArrayUri.add(imageUrl);
            }
            imageSwitcher.setImageURI(mArrayUri.get(0));
            position = 0;
        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
        if(!mArrayUri.isEmpty()){
            imageSwitcher.setVisibility(View.VISIBLE);
            btPrevious.setVisibility(View.VISIBLE);
            btNext.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public boolean onTouch(@NonNull View v, MotionEvent event) {

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        switch (v.getId()) {
            case R.id.dateStart:
                DatePickerDialog pickerStart = new DatePickerDialog(PostActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) -> startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
                pickerStart.show();

                break;
            case R.id.dateEnd:
                DatePickerDialog pikerEnd = new DatePickerDialog(PostActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) -> endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
                pikerEnd.show();
                break;
            default:
                break;
        }

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.close:
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.tv_post:
                uploadMultiImage();
                break;
            case R.id.bt_previous:
                imageSwitcher.setInAnimation(PostActivity.this, R.anim.from_right);
                imageSwitcher.setOutAnimation(PostActivity.this, R.anim.to_left);
                --position;
                if (position < 0)
                    position = mArrayUri.size() - 1;
                imageSwitcher.setImageURI(mArrayUri.get(position));
                break;
            case R.id.bt_next:
                imageSwitcher.setInAnimation(PostActivity.this, R.anim.from_left);
                imageSwitcher.setOutAnimation(PostActivity.this, R.anim.to_right);
                position++;
                if (position == mArrayUri.size())
                    position = 0;
                imageSwitcher.setImageURI(mArrayUri.get(position));
                break;

            case R.id.bt_gallery:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
                break;

            case R.id.bt_camera:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    // The app has the CAMERA permission, you can start the camera activity
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        return imageView;
    }

    @Override
    public void onSuccessUpload(boolean success, Exception e, String myUri, String postId) {
        if (success) {
            mArrayUrl.add(myUri);
            dialog.setMessage("Posting...\n" + "upload: " + mArrayUrl.size() + "/" + mArrayUri.size());
            if (mArrayUrl.size() == mArrayUri.size()) {

                Task task = new Task(
                        postId,
                        type.getText().toString(),
                        description.getText().toString(),
                        FirebaseAuth.getInstance().getUid(),
                        "Active",
                        12,
                        "public",
                        "all",
                        startDate.getText().toString(),
                        endDate.getText().toString(),
                        localData.getCurrentUser().getId(),
                        mArrayUrl);
                localData.addPost(postId, task, this);
            }
        } else {
            Toast.makeText(this, "Filed", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onSuccess(boolean success, Exception e) {
        if (success) {
            Toast.makeText(this, "Upload success", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Filed", Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
        startActivity(new Intent(PostActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}