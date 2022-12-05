package com.example.tocare.ui.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FeedsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is feed fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}