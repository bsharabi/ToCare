package com.example.tocare.UIL.profile.tabs.profileBasic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileBasicViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> bio;

    public ProfileBasicViewModel() {
        mText = new MutableLiveData<>();
        bio = new MutableLiveData<>();
        mText.setValue("This is basic Profile");
    }

    public MutableLiveData<String> getBio() {
        return bio;
    }

    public LiveData<String> getText() {
        return mText;
    }
}