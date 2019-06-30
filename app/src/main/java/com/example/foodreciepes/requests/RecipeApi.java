package com.example.foodreciepes.requests;

import com.example.foodreciepes.requests.responses.RecipeResponse;
import com.example.foodreciepes.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {
    //Hold all the request methods for the API

    //SEARCH GETTER METHOD
    @GET("api/search")
    //Call Object => response
    Call<RecipeSearchResponse> searchRecipe(
            @Query("key") String key, //Append ?
            @Query("q") String query, //Append &
            @Query("page") String page //Append &

    );

    //GET RECIPE REQUEST
    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );

}
