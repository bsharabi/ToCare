package com.example.tocare.UIL.ui.profile.tabs.profileAdvance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileAdvanceViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProfileAdvanceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is advance fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}