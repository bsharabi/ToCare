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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tocare.BLL.Adapters.TaskAdapter;
import com.example.tocare.BLL.Departments.Task;
import com.example.tocare.BLL.Departments.Tasks;
import com.example.tocare.MainActivity;
import com.example.tocare.databinding.FragmentTaskBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


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
        RecyclerView courseRV = binding.rvTaskView;
//        List<Task> mTask = mainActivity.getTasks().get(FirebaseAuth.getInstance().getUid());
        List<Task> mTask = new ArrayList<>();
        for (int i = 0; i <20  ; i++) {

            mTask.add(new Task());
        }

        TaskAdapter courseAdapter = new TaskAdapter(getContext(), mTask);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);

        taskViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,List<Task>> taskList =new HashMap();
                List<Task> list = new ArrayList<>();
                Tasks tasks = new Tasks();
                taskList.put(FirebaseAuth.getInstance().getUid(), new ArrayList<Task>());
                for (int i = 0; i < 30; i++) {
                    Task task = new Task("5445"+i, "t5ask"+i, "clean the kitfchen"+i, "active", 3, null);
                    list.add(task);
                    tasks.add("task");
                    taskList.get(FirebaseAuth.getInstance().getUid()).add(task);
                }
                DocumentReference reference = FirebaseFirestore.getInstance().collection("Task")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.set(taskList).addOnCompleteListener(task1 -> {
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