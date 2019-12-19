package com.ensak.AlloOustad.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ensak.AlloOustad.Constants;
import com.ensak.AlloOustad.R;
import com.ensak.AlloOustad.database.AppDatabase;
import com.ensak.AlloOustad.database.AppExecutors;
import com.ensak.AlloOustad.database.EnseignantEntry;
import com.ensak.AlloOustad.model.Enseignant;
import com.ensak.AlloOustad.model.EnseignantResponse;
import com.ensak.AlloOustad.model.Phone;
import com.ensak.AlloOustad.rest.ApiClient;
import com.ensak.AlloOustad.rest.ApiInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.like.IconType;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ensak.AlloOustad.Constants.ENSEIGNANT_ENTRY_INTENT_EXTRA;
import static com.ensak.AlloOustad.Constants.LAT;
import static com.ensak.AlloOustad.Constants.LONG;

public class ProfileActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.iv_profile_image)
    CircleImageView profileImage;
    @BindView(R.id.tv_name)
    TextView nameTextView;
    @BindView(R.id.tv_specialty)
    TextView specialtyTextView;
    @BindView(R.id.tv_description)
    TextView descriptionTextView;
    @BindView(R.id.tv_address)
    TextView addressTextView;
    @BindView(R.id.tv_bio)
    TextView bioTextView;
    @BindView(R.id.star_button)
    LikeButton likeButton;
    @BindView(R.id.tv_specialty_second)
    TextView tvSpecialtySecond;

    private AppDatabase mDb;

    private String uid = "";
    private Enseignant mEnseignant;
    private EnseignantEntry mEnseignantEntry;
    private GoogleMap mMap;
    private boolean isMapReady = false;
    private boolean isLocationReady = false;

    private static final String DOCTOR_ENTRY_KEY = "doctorEntryKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(DOCTOR_ENTRY_KEY)) {
            mEnseignantEntry = savedInstanceState.getParcelable(DOCTOR_ENTRY_KEY);
            assert mEnseignantEntry != null;
            uid = mEnseignantEntry.getUid();
            initProfile();

        } else {
            if (getIntent().hasExtra(Constants.DOCTOR_UID)) {
                uid = getIntent().getStringExtra(Constants.DOCTOR_UID);
                getEnseignantData();
            } else {
                mEnseignantEntry = getIntent().getParcelableExtra(ENSEIGNANT_ENTRY_INTENT_EXTRA);
                uid = mEnseignantEntry.getUid();
                initProfile();
            }
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);

        initLikeButton();


    }

    private void initLikeButton() {

        likeButton.setEnabled(true);
        likeButton.setIcon(IconType.Heart);
        likeButton.setIconSizeDp(30);

        checkEnseignantState();

        starButtonListener();

    }

    private void checkEnseignantState() {

        final List<String> uidList = new ArrayList<>();

        AppExecutors.getInstance().diskIO().execute(() -> {
            uidList.addAll(mDb.doctorDao().getUids());
            setLike(uidList);

        });
    }

    private void setLike(List<String> uidList) {

        boolean state = false;

        for (String id : uidList) {
            if (id.equals(uid)) {
                state = true;
                break;
            }
        }

        if (state) {
            likeButton.setLiked(true);
        } else {
            likeButton.setLiked(false);
        }
    }

    private void starButtonListener() {

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                saveEnseignantProfile();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteEnseignantProfile();
            }
        });
    }

    private void getEnseignantData() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<EnseignantResponse> call;
        call = apiService.getEnseignantResponse(uid);


        call.enqueue(new Callback<EnseignantResponse>() {
            @Override
            public void onResponse(Call<EnseignantResponse> call, Response<EnseignantResponse> response) {


                if (response.body() != null && response.body() != null) {
                    mEnseignant = response.body().getData();
                    initEnseignantEntry();
                    initProfile();

                } else {
                }
            }

            @Override
            public void onFailure(Call<EnseignantResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, getString(R.string.connection_failed) + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initProfile() {
        setViews();
        isLocationReady = true;
        initMap();
    }

    private void setViews() {

        //first card
        Picasso.with(this)
                .load(mEnseignantEntry.getProfileImage())
                .noFade().
                into(profileImage);

        String name = "Mr. " + mEnseignantEntry.getFirstName() + " " + mEnseignantEntry.getLastName();
        nameTextView.setText(name);

        String specialty = mEnseignantEntry.getTitle() + ", " + mEnseignantEntry.getSpecialtyName();
        specialtyTextView.setText(specialty);
        tvSpecialtySecond.setText(specialty);

        String description = mEnseignantEntry.getSpecialtyDescription();
        descriptionTextView.setText(description);



        String address =
                mEnseignantEntry.getState() + ", " +
                        mEnseignantEntry.getCity() + ", " +
                        mEnseignantEntry.getStreet();
        if (mEnseignantEntry.getCity() == null || mEnseignantEntry.getCity().equals("")) {
            addressTextView.setText(getString(R.string.no_location_message));
        } else {
            addressTextView.setText(address);
        }

        //third card
        String bio = mEnseignantEntry.getBio();
        bioTextView.setText(bio);

    }

    public void showZipCode(View view) {

        String zipCode = mEnseignantEntry.getZip();

        if (zipCode == null || zipCode.equals("")) zipCode = getString(R.string.no_zip_code);

        shoeDialogMessage(Constants.ZIP_CODE_TITLE, zipCode);

    }

    public void showFaxNumber(View view) {

        String faxNumber = mEnseignantEntry.getFaxNumber();

        if (faxNumber == null || faxNumber.equals("")) {
            faxNumber = getString(R.string.no_fax_number);
        }

        shoeDialogMessage(Constants.FAX_NUMBER_TITLE, faxNumber);

    }

    private void shoeDialogMessage(String title, String number) {
        new SweetAlertDialog(this)
                .setTitleText(title)
                .setContentText(number)
                .show();
    }

    public void openWebsite(View view) {

        String url = mEnseignantEntry.getWebsite();
        String msg = getString(R.string.no_website);

        if (url != null && !url.equals("")) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void saveEnseignantProfile() {

        AppExecutors.getInstance().diskIO().execute(() -> mDb.doctorDao().insertEnseignant(mEnseignantEntry));


    }

    public void deleteEnseignantProfile() {

        AppExecutors.getInstance().diskIO().execute(() -> mDb.doctorDao().delete(mEnseignantEntry));

    }

    @NonNull
    private void initEnseignantEntry() {
        String uid = "";
        String website = "";
        String city = "";
        double lat = 0;
        double lon = 0;
        String state = "";
        String street = "";
        String zip = "";
        String landLineNumber = "";
        String faxNumber = "";
        String firstName = "";
        String lastName = "";
        String title = "";
        String profileImage = "";
        String bio = "";
        String specialtyName = "";
        String specialtyDescription = "";


        if (mEnseignant.getCoordonnees().size() > 0) {
            Log.i("hahuwa",mEnseignant.getCoordonnees().get(0).getWebsite());
            website = mEnseignant.getCoordonnees().get(0).getWebsite();
            city = mEnseignant.getCoordonnees().get(0).getVisitAddress().getCity();
            lat = mEnseignant.getCoordonnees().get(0).getVisitAddress().getLat();
            lon = mEnseignant.getCoordonnees().get(0).getVisitAddress().getLon();
            state = mEnseignant.getCoordonnees().get(0).getVisitAddress().getState();
            street = mEnseignant.getCoordonnees().get(0).getVisitAddress().getStreet();
            zip = mEnseignant.getCoordonnees().get(0).getVisitAddress().getZip();
            landLineNumber = getPhoneNumber(Constants.LAND_LINE);
            faxNumber = getPhoneNumber(Constants.FAX);

        }

        firstName = mEnseignant.getProfile().getFirstName();
        lastName = mEnseignant.getProfile().getLastName();
        title = mEnseignant.getProfile().getTitle();
        profileImage = mEnseignant.getProfile().getImageUrl();
        bio = mEnseignant.getProfile().getBio();

        if (mEnseignant.getMatiers().size() > 0) {
            specialtyName = mEnseignant.getMatiers().get(0).getName();
            specialtyDescription = mEnseignant.getMatiers().get(0).getDescription();
        }

        uid = mEnseignant.getUid();

        mEnseignantEntry = new EnseignantEntry(
                uid,
                website,
                city,
                lat,
                lon,
                state,
                street,
                zip,
                landLineNumber,
                faxNumber,
                firstName,
                lastName,
                title,
                profileImage,
                bio,
                specialtyName,
                specialtyDescription
        );
    }

    private String getPhoneNumber(String type) {

        for (Phone phone : mEnseignant.getCoordonnees().get(0).getPhones()) {
            if (phone.getType().equals(type)) {
                return phone.getNumber();
            }
        }
        return null;


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        isMapReady = true;
        mMap = googleMap;
        initMap();

    }

    private void initMap() {

        if (isMapReady && isLocationReady) {

            mMap.getUiSettings().setZoomGesturesEnabled(false);
            mMap.getUiSettings().setScrollGesturesEnabled(false);

            double lat = mEnseignantEntry.getLat();
            double lng = mEnseignantEntry.getLon();

            if (lat != 0) {
                LatLng location = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(location)
                        .title(getString(R.string.doctor_location_title)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            }
        }
    }

    public void call(View view) {

        String phone = mEnseignantEntry.getLandLineNumber();

        if (!phone.equals("")) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.no_phone_number), Toast.LENGTH_SHORT).show();
        }
    }

    public void startMapActivity(View view) {

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(LAT, mEnseignantEntry.getLat());
        intent.putExtra(LONG, mEnseignantEntry.getLon());
        startActivity(intent);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(DOCTOR_ENTRY_KEY, mEnseignantEntry);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
