package com.ensak.AlloOustad.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ensak.AlloOustad.R;
import com.ensak.AlloOustad.adapter.SavedEnseignantListAdapter;
import com.ensak.AlloOustad.database.EnseignantEntry;
import com.ensak.AlloOustad.database.MainViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ensak.AlloOustad.Constants.ENSEIGNANT_ENTRY_INTENT_EXTRA;

public class SavedEnseignantActivity extends AppCompatActivity implements SavedEnseignantListAdapter.SavedEnseignantAdapterOnClickHandler {

    @BindView(R.id.saves_doctor_list_recycler_view)
    RecyclerView savesEnseignantListRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    private SavedEnseignantListAdapter mAdapter;
    private List<EnseignantEntry> mEnseignantEntryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_doctor);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mAdapter = new SavedEnseignantListAdapter(this, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        savesEnseignantListRecyclerView.setLayoutManager(layoutManager);
        savesEnseignantListRecyclerView.setAdapter(mAdapter);

        setupViewModel();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(SavedEnseignantActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }


    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getEnseignants().observe(this, enseignantEntries -> {
            mEnseignantEntryList = enseignantEntries;
            mAdapter.setEnseignants(enseignantEntries);
        });
    }

    @Override
    public void onClick(int position) {

        EnseignantEntry doctorEntry = mEnseignantEntryList.get(position);
        Intent intent = new Intent(this, ProfileActivity.class);

        intent.putExtra(ENSEIGNANT_ENTRY_INTENT_EXTRA, doctorEntry);
        startActivity(intent);
    }

}
