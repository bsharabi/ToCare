package com.example.tocare.UIL.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.tocare.BLL.Adapters.UserSearchAdapter;

import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.DAL.Data;
import com.example.tocare.R;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements TextWatcher {


    private RecyclerView recyclerView;
    private EditText search;
    private List<UserModel> userModelList;
    private Data localData;
    private UserSearchAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        localData = Data.getInstance();

        search = view.findViewById(R.id.search_bar);
        recyclerView = view.findViewById(R.id.recycler_view);
        userModelList = new ArrayList<>();


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new UserSearchAdapter(getContext(), userModelList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        search.addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().equals("")) {
            this.userModelList.clear();
            adapter.notifyDataSetChanged();
        } else
            localData.searchUsers(s.toString().trim(), userModelList, () -> adapter.notifyDataSetChanged());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}