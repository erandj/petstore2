package com.example.petstore;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PetStoreAPI {
    @GET("pet/{petId}")
    Call<PetStore> getData(@Path("petId") int id);

    @POST("pet")
    Call<PetStore> postPet(@Body PetStore pet);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://petstore.swagger.io/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
