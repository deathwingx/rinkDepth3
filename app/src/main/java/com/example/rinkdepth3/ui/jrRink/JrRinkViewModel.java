package com.example.rinkdepth3.ui.jrRink;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JrRinkViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public JrRinkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is jrRink fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}