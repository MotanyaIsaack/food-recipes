package com.example.foodreciepes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodreciepes.models.Recipe;
import com.example.foodreciepes.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    //Retrieving, holding and displaying Recipes Displayed in the application

    private RecipeRepository mRecipeRepository;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }

    //Build a method that will be called from the view
    public void searchRecipesApi(String query, int pageNumber){
        mRecipeRepository.searchRecipesApi(query,pageNumber);
    }

}
