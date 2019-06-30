package com.example.foodreciepes.adapters;

public interface OnRecipeListener {

    //Detects clicks to a recipe
    void onRecipeClick(int position);

    //Detects clicks to a category
    void onCategoryClick(String category);


}
