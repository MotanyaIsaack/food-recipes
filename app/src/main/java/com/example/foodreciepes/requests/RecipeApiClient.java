package com.example.foodreciepes.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodreciepes.AppExecutors;
import com.example.foodreciepes.models.Recipe;
import com.example.foodreciepes.requests.responses.RecipeResponse;
import com.example.foodreciepes.requests.responses.RecipeSearchResponse;
import com.example.foodreciepes.util.Constants;
import com.google.android.material.appbar.AppBarLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.example.foodreciepes.util.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    //Singleton pattern
    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    //Global object for the runnable
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;
    private RetrieveRecipeRunnable mRetrieveRecipeRunnable;
    private MutableLiveData<Recipe> mRecipe;

    public static RecipeApiClient getInstance(){
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipe;
    }

    public void searchRecipesApi(String query,int pageNumber){
        if (mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable = null;
        }

        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query,pageNumber);
        //We use the future to set the timeout
        final Future handler = AppExecutors.get().networkIO().submit(mRetrieveRecipesRunnable);

        AppExecutors.get().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Let the user know its timed out
                handler.cancel(true);
            }
        },NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void searchRecipeById(String recipeId){
        if (mRetrieveRecipeRunnable!=null){
            mRetrieveRecipeRunnable = null;
        }
        mRetrieveRecipeRunnable = new RetrieveRecipeRunnable(recipeId);

        final Future handler = AppExecutors.get().networkIO().submit(mRetrieveRecipeRunnable);
        AppExecutors.get().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Let the user know its timed out
                handler.cancel(true);
            }
        },NETWORK_TIMEOUT,TimeUnit.MILLISECONDS);
    }

    //Build the runnable class we are going to use to retrieve data from the runnable API
    private class RetrieveRecipesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try {
                Response response = getRecipes(query,pageNumber).execute();
                if (cancelRequest){
                    return;
                }
                if (response.code()==200){
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse) response.body()).getRecipes());
                    if (pageNumber ==1){
                        //postvalue() is used since its on a background thread
                        mRecipes.postValue(list);
                    }else{
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(currentRecipes);
                    }
                }else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    //In case of an error i post null
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                //In case of an error i post null
                mRecipes.postValue(null);
            }


        }
        //Method that will return the call object
        private Call<RecipeSearchResponse> getRecipes (String query, int pageNumber){
            return ServiceGenerator.getReciepeApi().searchRecipe(
                    Constants.API_KEY,
                    query,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: cancelling the search request ");
            cancelRequest = true;
        }
    }


    private class RetrieveRecipeRunnable implements Runnable{

        private String recipeId;
        private boolean cancelRequest;

        public RetrieveRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try {
                Response response = getRecipe(recipeId).execute();
                if (cancelRequest){
                    return;
                }
                if (response.code()==200){
                    Recipe recipe = ((RecipeResponse)response.body()).getRecipe();
                    mRecipe.postValue(recipe);
                }else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    //In case of an error i post null
                    mRecipe.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                //In case of an error i post null
                mRecipe.postValue(null);
            }


        }
        //Method that will return the call object
        private Call<RecipeResponse> getRecipe (String recipeId){
            return ServiceGenerator.getReciepeApi().getRecipe(
                    Constants.API_KEY,
                    recipeId
            );
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest: cancelling the search request ");
            cancelRequest = true;
        }
    }
    public void cancelRequest(){
        if (mRetrieveRecipesRunnable!=null){
            mRetrieveRecipesRunnable.cancelRequest();
        }
        if (mRetrieveRecipeRunnable!=null){
            mRetrieveRecipeRunnable.cancelRequest();
        }
    }
}
