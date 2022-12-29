package com.example.tocare.UIL.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tocare.BLL.Adapters.TaskAdapter;
import com.example.tocare.DAL.Data;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.R;
import com.example.tocare.databinding.FragmentTasksBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;



public class TasksFragment extends Fragment {

    private FragmentTasksBinding binding;
    private FloatingActionButton btAddTask;
    private TextView textView;
    private List<Task> mTask;
    private Bundle bundle;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btAddTask = binding.fabAddTask;
        textView = binding.textHome;

        RecyclerView tasksRV = binding.rvTaskView;
        mTask = Data.getInstance().getAllTask();
        TaskAdapter taskAdapter = new TaskAdapter(getContext(), mTask);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        tasksRV.setLayoutManager(linearLayoutManager);
        tasksRV.setAdapter(taskAdapter);

        return root;
    }

    public void swapFragmentByFragmentClass(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view_task, fragmentClass, null)
                .addToBackStack(fragmentClass.getName())
                .commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btAddTask.setOnClickListener(view1 -> {
            swapFragmentByFragmentClass(NewTaskFragment.class, null);
        });

    }


}