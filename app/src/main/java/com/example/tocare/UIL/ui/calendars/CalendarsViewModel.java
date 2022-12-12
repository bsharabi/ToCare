package com.example.tocare.UIL.ui.calendars;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalendarsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CalendarsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is calenders fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}