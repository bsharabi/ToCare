package com.example.tocare.UIL.ui.phone;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PhoneCodeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PhoneCodeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Code verification");
    }

    public LiveData<String> getText() {
        return mText;
    }
}