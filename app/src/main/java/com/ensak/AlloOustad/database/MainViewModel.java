package com.ensak.AlloOustad.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private final LiveData<List<EnseignantEntry>> doctors;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the doctors from the DataBase");
        doctors = database.doctorDao().loadEnseignants();
    }

    public LiveData<List<EnseignantEntry>> getEnseignants() {
        return doctors;
    }
}