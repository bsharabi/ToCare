package com.example.tocare.UIL.Fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.tocare.BLL.Listener.UploadCallback;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.BLL.Model.User;
import com.example.tocare.Controller.MainActivity;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;


public class PostDetailsFragment extends Fragment implements View.OnClickListener, ViewSwitcher.ViewFactory, UploadCallback {

    private int position = 0;
    private EditText type, description, priority, bid, startDate, endDate;
    private ImageSwitcher imageSwitcher;
    private ArrayList<String> mArrayUrl;
    private Data localData;
    private ProgressDialog dialog;
    private ImageButton btPrevious, btNext;
    private List<String> url;
    private SwitchCompat switcher;
    private Spinner select;
    private List<User> child;
    private List<String> selectItem;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        localData = Data.getInstance();

        mArrayUrl = new ArrayList<>();
        child = new ArrayList<>();
        url = new ArrayList<>();

        ImageView back = view.findViewById(R.id.go_back);
        TextView post = view.findViewById(R.id.tv_share);

        dialog = new ProgressDialog(getContext());

        btPrevious = view.findViewById(R.id.bt_previous);
        btNext = view.findViewById(R.id.bt_next);

        select = view.findViewById(R.id.spinner_select);
        switcher = view.findViewById(R.id.visibility);

        btPrevious.setVisibility(View.GONE);
        btNext.setVisibility(View.GONE);

        imageSwitcher = view.findViewById(R.id.image_select);
        description = view.findViewById(R.id.description);
        startDate = view.findViewById(R.id.dateStart);
        priority = view.findViewById(R.id.priority);
        endDate = view.findViewById(R.id.dateEnd);
        type = view.findViewById(R.id.type);
        bid = view.findViewById(R.id.bid);

        imageSwitcher.setOnClickListener(this);
        btPrevious.setOnClickListener(this);
        startDate.setOnClickListener(this);
        priority.setOnClickListener(this);
        endDate.setOnClickListener(this);
        btNext.setOnClickListener(this);
        imageSwitcher.setFactory(this);
        post.setOnClickListener(this);
        back.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            mArrayUrl = getArguments().getStringArrayList("selected");
            Uri uri = Uri.parse(mArrayUrl.get(position));
            imageSwitcher.setImageURI(uri);
        } else
            goTo();

        if (mArrayUrl.size() > 1) {
            btPrevious.setVisibility(View.VISIBLE);
            btNext.setVisibility(View.VISIBLE);
        }

        switcher.setOnClickListener(v -> {
            if (switcher.getText().toString().equals("Private")) {
                switcher.setText(R.string.Public);
                switcher.setTag(R.string.Public);
                select.setVisibility(View.GONE);
            } else {
                switcher.setText(R.string.privatePermission);
                switcher.setTag(R.string.privatePermission);
                select.setVisibility(View.VISIBLE);
            }
        });
        priority.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int num = Integer.parseInt(s.toString());
                    if (num > 12) {
                        priority.setError("Number should be between 0 and 12");
                        priority.setText("12");
                    } else {
                        priority.setError(null);
                    }
                } catch (NumberFormatException e) {
                    // Do nothing
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        List<String> childName = new ArrayList<>();
        selectItem = new ArrayList<>();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, childName);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select.setAdapter(spinnerArrayAdapter);
        localData.getAllChildren(child, () -> {
            childName.clear();
            selectItem.clear();
            childName.add("mySelf");
            selectItem.add(localData.getCurrentUserId());
            if (!child.isEmpty()) {
                childName.add("Random");
                selectItem.add(child.get(new Random().nextInt(child.size())).getId());
                childName.add("Free");
                selectItem.add("");
            }
            for (User user : child) {
                childName.add(user.getUserName());
                selectItem.add(user.getId());
            }
            spinnerArrayAdapter.notifyDataSetChanged();
        });


    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadMultiImage() {
        if (!mArrayUrl.isEmpty()) {
            dialog.setMessage("Posting... ");
            dialog.show();
            String postId = localData.getRandomIdByCollectionName("Posts");
            for (String path : mArrayUrl) {
                Uri uri = Uri.parse(path);
                localData.uploadImage(uri, getFileExtension(uri), postId, this);
            }
        } else {
            Toast.makeText(getContext(), "Must select a picture", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.go_back:
                goTo();
                break;
            case R.id.tv_share:
                uploadMultiImage();
                break;
            case R.id.bt_previous:
                nextOrPrevImage(-1);
                break;
            case R.id.bt_next:
                nextOrPrevImage(1);
                break;
            case R.id.dateStart:
                setDate(startDate);
                break;
            case R.id.dateEnd:
                setDate(endDate);
                break;
            default:
                break;
        }
    }

    private void nextOrPrevImage(int pos) {
        imageSwitcher.setInAnimation(getContext(), (pos == 1) ? R.anim.from_left : R.anim.from_right);
        imageSwitcher.setOutAnimation(getContext(), (pos == 1) ? R.anim.to_right : R.anim.to_left);
        position = (position + pos) % mArrayUrl.size();
        Uri uri = Uri.parse(mArrayUrl.get(position));
        imageSwitcher.setImageURI(uri);
    }

    private void setDate(@NonNull final EditText date) {

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        @SuppressLint("SetTextI18n") DatePickerDialog picker = new DatePickerDialog(requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String dateSelect = dayOfMonth + "/" + monthOfYear + 1 + "/" + String.valueOf(year1).replace(",", "");
                    date.setText(dateSelect);
                    if (date.getTag().equals("start")) {
                        endDate.setEnabled(true);
                    }
                },
                year,
                month,
                day);

        if (date.getTag().equals("end")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            try {
                picker.getDatePicker().setMinDate(Objects.requireNonNull(sdf.parse(startDate.getText().toString())).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else
            picker.getDatePicker().setMinDate(System.currentTimeMillis());

        picker.show();
        picker.setOnCancelListener(dialog -> {
            if (date.getTag().equals("start")) {
                endDate.setText("");
                endDate.setEnabled(false);
            }
            date.setText("");
        });
    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(requireContext());
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
            url.add(myUri);
            dialog.setMessage("Posting...\n" + "upload: " + mArrayUrl.size() + "/" + url.size());
            if (mArrayUrl.size() == url.size()) {

                Task task = new Task(postId,
                        type.getText().toString().trim(),
                        description.getText().toString().trim(),
                        switcher.getText().toString().equals("Private") ? selectItem.get(select.getSelectedItemPosition()) : "",
                        Integer.parseInt(priority.getText().toString().isEmpty() ? 0 + "" : priority.getText().toString().trim()),
                        switcher.getText().toString().trim(),
                        bid.getText().toString().isEmpty() ? "0" : bid.getText().toString().trim(),
                        startDate.getText().toString().isEmpty() ?
                                ""
                                : new Date(startDate.getText().toString()).toString(),
                        endDate.getText().toString().isEmpty() ?
                                "" :
                                new Date(endDate.getText().toString()).toString(),
                        localData.getCurrentUserId(),
                        url,
                        new Date().toString());
                localData.addPost(postId, task, this);
            }
        } else {
            Toast.makeText(requireContext(), "Filed", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            reload();
        }
    }

    @Override
    public void onSuccess(boolean success, Exception e) {
        if (success) {
            Toast.makeText(requireContext(), "Upload success", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(requireContext(), "Filed", Toast.LENGTH_LONG).show();
        }
        reload();
    }

    private void reload() {
        dialog.dismiss();
        startActivity(new Intent(requireContext(), MainActivity.class));
        requireActivity().finish();
    }

    private void goTo() {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("selected", mArrayUrl);
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_post, GalleryFragment.class, bundle).commit();
    }
}


