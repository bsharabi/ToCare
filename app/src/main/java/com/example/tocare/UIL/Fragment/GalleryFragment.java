package com.example.tocare.UIL.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tocare.BLL.Adapters.GalleryAdapter;
import com.example.tocare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment {

    private static final int STORAGE_REQUEST_CODE = 28;
    private static final String TAG = "GalleryFragment";

    private RecyclerView recyclerView_gallery;
    private TextView next;
    private ImageView close, imageView, fit;
    private FloatingActionButton takePhoto;
    private List<Uri> mImage;
    private List<String> mSelect;
    private GalleryAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView_gallery = view.findViewById(R.id.recycler_view_gallery);
        next = view.findViewById(R.id.next_gallery);
        close = view.findViewById(R.id.close_gallery);
        imageView = view.findViewById(R.id.image_item_gallery);
        takePhoto = view.findViewById(R.id.take_photo_gallery);
        fit = view.findViewById(R.id.im_fit);

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null)
            mSelect = getArguments().getStringArrayList("selected");
        else
            mSelect = new ArrayList<>();

        close.setOnClickListener(v -> requireActivity().finish());
        next.setOnClickListener(v -> {
            if (mSelect.size() != 0)
                goTo(PostDetailsFragment.class);
            else
                Toast.makeText(requireContext(), "At least one image must be selected", Toast.LENGTH_SHORT).show();
        });


        takePhoto.setOnClickListener(v -> goTo(CameraFragment.class));
        mImage = new ArrayList<>();

        checkPermission();
        adapter = new GalleryAdapter(getContext(), mImage, mSelect, imageView, fit);
        fit.setOnClickListener(v -> {
            fit.setTag((fit.getTag().equals("fit")) ? "uFit" : "fit");
            adapter.notifyItemChanged((mSelect.isEmpty()) ? 0 : mImage.indexOf(Uri.parse(mSelect.get(mSelect.size() - 1))));
        });
        recyclerView_gallery.setHasFixedSize(true);
        recyclerView_gallery.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView_gallery.setAdapter(adapter);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        } else {
            getAllImagesUri();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAllImagesUri();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @SuppressLint("Recycle")
    private void getAllImagesUri() {
        Uri uri;
        Cursor cursor;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.MediaColumns.DATA};
        cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if ((cursor != null)) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                Uri contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
                mImage.add(contentUri);
                Log.d(TAG, contentUri.toString());
            }
            cursor.close();
        }
    }

    private void goTo(Class<? extends Fragment> name) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("selected", (ArrayList<String>) mSelect);
        bundle.putString("gallery", (!mImage.isEmpty()) ? mImage.get(0).toString() : "");
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_post, name, bundle).commit();
    }
}
