package com.example.tocare.UIL.ui.phone;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PhoneViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PhoneViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Login with Phone number");
    }

    public LiveData<String> getText() {
        return mText;
    }
}