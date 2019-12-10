package com.ensak.AlloOustad.rest;


import com.ensak.AlloOustad.model.EnseignantResponse;
import com.ensak.AlloOustad.model.models.PreEnseignantSearchResponse;
import com.ensak.AlloOustad.model.SpecialitiesSearchResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {



    @GET("enseignant/search")
    Call<PreEnseignantSearchResponse> getEnseignantList(@Query("lat") String lat,
                                                        @Query("lon") String lon,
                                                        @Query("name") String name,
                                                        @Query("matieres") String matieres,
                                                        @Query("gender") String gender);



    @GET("specialties")
    Call<SpecialitiesSearchResponse> getSpecialities(@Query("user_key") String userKey);

    @GET("enseignant/{uid}")
    Call<EnseignantResponse> getEnseignantResponse(@Path(value = "uid", encoded = true) String uid);

    @GET("enseignant/search")
    Call<PreEnseignantSearchResponse> getEnseignantList(@Query("lat") String lat,
                                                        @Query("lon") String lon,
                                                        @Query("gender") String gender);


//    @GET("enseignant/search")
//    Call<PreEnseignantSearchResponse> getEnseignantList(@Query("lat") String lat,
//                                                        @Query("lon") String lon);


}
