package com.example.foodreciepes.util;

import android.util.Log;

import com.example.foodreciepes.models.Recipe;

import java.util.List;

public class Testing {

    public static void printRecipes(List<Recipe>list, String tag){
        for (Recipe recipe: list){
            Log.d(tag, "onChanged: "+ recipe.getTitle());
        }
    }
}
