package com.ensak.AlloOustad.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface EnseignantDao {

    @Query("SELECT * FROM doctors")
    LiveData<List<EnseignantEntry>> loadEnseignants();

    @Query("SELECT * FROM doctors WHERE uid > :uid")
    LiveData<EnseignantEntry> loadEnseignantByUid(String uid);

    @Insert
    void insertEnseignant(EnseignantEntry doctorEntry);

    @Delete
    void delete(EnseignantEntry doctorEntry);

    @Query("SELECT uid FROM doctors")
    List<String> getUids();

    @Query("SELECT * FROM doctors")
    List<EnseignantEntry> loadEnseignantsFoeWidget();
}
