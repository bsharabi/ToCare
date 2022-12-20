package com.example.tocare.UIL.ui.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tocare.BLL.Departments.Task;
import com.example.tocare.MainActivity;
import com.example.tocare.databinding.FragmentTaskBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;
    private FloatingActionButton btAddTask;
    private static TaskFragment single_instance = null;
    private MainActivity mainActivity;
    private TextView textView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TaskViewModel taskViewModel =
                new ViewModelProvider(this).get(TaskViewModel.class);
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mainActivity = (MainActivity) getActivity();
        btAddTask = binding.fabAddTask;
        textView = binding.textHome;

        taskViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task("5445", "t5ask", "clean the kitfchen", "active", 3, null);
                DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.set(mainActivity.getCurrentUser()).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        System.out.println("Ok");
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public TaskFragment() {
    }

    public static TaskFragment getInstance() {
        if (single_instance == null)
            single_instance = new TaskFragment();

        return single_instance;
    }
}