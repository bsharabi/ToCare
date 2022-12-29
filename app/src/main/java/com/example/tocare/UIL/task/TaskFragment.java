package com.example.tocare.UIL.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.R;
import com.example.tocare.databinding.FragmentTaskBinding;


public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;
    private Bundle bundle;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        System.out.println(savedInstanceState);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view_task, TasksFragment.class, null)
                .commit();


        return root;
    }

    public void swapFragmentByFragmentClass(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view_task, fragmentClass, bundle)
                .setReorderingAllowed(true)
                .addToBackStack(fragmentClass.getName())
                .commit();
    }


}