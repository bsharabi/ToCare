package com.example.tocare.UIL.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.tocare.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;

public class CameraFragment extends Fragment {
    private FrameLayout preview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ImageView first = view.findViewById(R.id.firstImage);

        view.findViewById(R.id.close_camera).setOnClickListener(v -> goTo());
        view.findViewById(R.id.take_photo).setOnClickListener(null);
        preview = view.findViewById(R.id.camera_frame);

        if (getArguments() != null) {
            String firstImage = getArguments().getString("gallery");
            Picasso.get().load(firstImage).fit().into(first);
        } else
            Picasso.get().load(R.drawable.demo_profile).fit().into(first);

        first.setOnClickListener(v -> goTo());
        startCameraPreviewAndTakePhoto();
        return view;
    }

    private void startCameraPreviewAndTakePhoto() {
        // Step 1: Create a CameraManager
        CameraManager manager = (CameraManager) requireContext().getSystemService(Context.CAMERA_SERVICE);

        // Step 2: Get the camera ID
        String cameraId = null;
        try {
            cameraId = manager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // Step 3: Create a SurfaceView
        SurfaceView surfaceView = new SurfaceView(getContext());
        SurfaceHolder surfaceHolder = surfaceView.getHolder();

        // Step 4: Create a CameraDevice.StateCallback
        CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice camera) {
                // Step 5: Start the camera preview
                try {
                    camera.createCaptureSession(Collections.singletonList(surfaceHolder.getSurface()),
                            new CameraCaptureSession.StateCallback() {
                                @Override
                                public void onConfigured(CameraCaptureSession session) {
                                    try {
                                        CaptureRequest.Builder previewRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                        previewRequestBuilder.addTarget(surfaceHolder.getSurface());
                                        session.setRepeatingRequest(previewRequestBuilder.build(), null, null);
                                    } catch (CameraAccessException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onConfigureFailed(CameraCaptureSession session) {
                                }
                            }, null);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDisconnected(CameraDevice camera) {
            }

            @Override
            public void onError(CameraDevice camera, int error) {
            }
        };

        // Step 6: Open the camera
        try {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // Step 7: Add the SurfaceView to the layout
        preview.addView(surfaceView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void goTo() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_post, GalleryFragment.class, null).commit();
    }
}

