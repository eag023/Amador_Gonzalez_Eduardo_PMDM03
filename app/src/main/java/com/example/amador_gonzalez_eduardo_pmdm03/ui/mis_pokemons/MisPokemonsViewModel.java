package com.example.amador_gonzalez_eduardo_pmdm03.ui.mis_pokemons;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MisPokemonsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MisPokemonsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}