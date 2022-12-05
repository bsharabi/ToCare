package com.example.tocare.ui.profile.tabs.profileBasic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileBasicViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProfileBasicViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is basic fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}