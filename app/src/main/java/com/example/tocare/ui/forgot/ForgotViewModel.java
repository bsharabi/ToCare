package com.example.tocare.ui.forgot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForgotViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ForgotViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is forgot fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}