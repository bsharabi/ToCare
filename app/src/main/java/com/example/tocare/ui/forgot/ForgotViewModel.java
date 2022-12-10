package com.example.tocare.ui.forgot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForgotViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> mReset;

    public ForgotViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Rest your password...");

        mReset = new MutableLiveData<>();
        mReset.setValue("your code sent...");
    }

    public LiveData<String> getText() {
        return mText;
    }
}