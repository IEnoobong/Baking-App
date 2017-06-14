/*
 * Copyright (c) 2017. Ibanga Enoobong Ime (World class developer and entrepreneur in transit)
 */

package co.enoobong.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.enoobong.bakingapp.R;
import co.enoobong.bakingapp.data.Recipe;
import co.enoobong.bakingapp.network.ApiClient;
import co.enoobong.bakingapp.network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static co.enoobong.bakingapp.ui.RecipeAdapter.RECIPE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.rv_recipe)
    RecyclerView mRecipeRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar mLoadingIndicator;
    private ArrayList<Recipe> mRecipesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mRecipesList = savedInstanceState.getParcelableArrayList(RECIPE);
            loadData();
        } else {
            getRecipes();
        }
    }

    private void getRecipes() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final Type TYPE = new TypeToken<ArrayList<Recipe>>() {
        }.getType();
        Call<JsonArray> call = apiInterface.getRecipe();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                mRecipesList = new Gson().fromJson(response.body(), TYPE);
                loadData();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, R.string.error_no_internet, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadData() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        RecipeAdapter recipeAdapter = new RecipeAdapter(MainActivity.this, mRecipesList);
        mRecipeRecyclerView.setAdapter(recipeAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE, mRecipesList);
    }
}
