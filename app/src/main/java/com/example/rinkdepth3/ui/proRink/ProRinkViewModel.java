package com.example.rinkdepth3.ui.proRink;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProRinkViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProRinkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is proRink fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}