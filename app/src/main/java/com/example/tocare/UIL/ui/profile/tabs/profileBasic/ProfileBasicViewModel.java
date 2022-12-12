package com.example.tocare.UIL.ui.profile.tabs.profileBasic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tocare.BLL.Departments.UserModel;
import com.example.tocare.UIL.ui.Activities.LoginActivity;
import com.example.tocare.UIL.ui.Activities.MainActivity;

public class ProfileBasicViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> bio;

    public ProfileBasicViewModel() {
        mText = new MutableLiveData<>();
        bio = new MutableLiveData<>();
        mText.setValue("This is basic fragment");
        bio.setValue(MainActivity.currentUser.getBio());
    }

    public MutableLiveData<String> getBio() {
        return bio;
    }

    public LiveData<String> getText() {
        return mText;
    }
}