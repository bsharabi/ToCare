package com.example.tocare.UIL.task;


import static android.app.Activity.RESULT_OK;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.tocare.DAL.Data;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.R;
import com.example.tocare.databinding.FragmentNewTaskBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class NewTaskFragment extends Fragment {

    public static final int PICK_IMAGE_REQUEST = 22;

    private FragmentNewTaskBinding binding;
    private ImageView imageView;
    private Button btSubmit, btnSelect;
    private Uri filePath;
    private Data localData;
    private String UserSelected;
    private Bundle bundle;
    private List<String> items = new ArrayList<>();
    private List<String> itemsID = new ArrayList<>();
    private Spinner dropdown;
    private String userID;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        localData = Data.getInstance();
        btSubmit = binding.btSubmit;
        btnSelect = binding.btnChoose;
        imageView = binding.imag;

        btnSelect.setOnClickListener(v -> SelectImage());

        btSubmit.setOnClickListener(v -> {
//            localData.addNewTask(UserSelected, new Task(binding.etBio.getText().toString(), UserSelected));
            localData.updateListTaskByUserId(UserSelected)
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            System.out.println("Ok");
                            swapFragmentByFragmentClass(TasksFragment.class, null);
                        }
                    });
        });
        dropdown = binding.tilChooseUsers;
        for (UserModel userModel : localData.getAllUser()) {
            items.add(userModel.getUserName());
            itemsID.add(userModel.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                UserSelected = itemsID.get(position);
                System.out.println(UserSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return root;
    }

    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getActivity().getContentResolver(), filePath);


                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }




    public void swapFragmentByFragmentClass(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view_task, fragmentClass, null)
                .addToBackStack(fragmentClass.getName())
                .commit();
    }
}