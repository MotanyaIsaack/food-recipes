package com.example.foodreciepes.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodreciepes.models.Recipe;
import com.example.foodreciepes.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient mRecipeApiClient;

    public static RecipeRepository getInstance(){
        if (instance==null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    public RecipeRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeApiClient.getRecipes();
    }

    //Build a method that will be called from the view model
    public void searchRecipesApi(String query, int pageNumber){
        if (pageNumber == 0){
            pageNumber =1;
        }
        mRecipeApiClient.searchRecipesApi(query,pageNumber);
    }


}
