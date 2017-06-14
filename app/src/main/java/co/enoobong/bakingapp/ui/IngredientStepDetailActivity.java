/*
 * Copyright (c) 2017. Ibanga Enoobong Ime (World class developer and entrepreneur in transit)
 */

package co.enoobong.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import co.enoobong.bakingapp.R;
import co.enoobong.bakingapp.data.Ingredient;
import co.enoobong.bakingapp.data.Step;

import static co.enoobong.bakingapp.ui.IngredientStepActivity.PANES;
import static co.enoobong.bakingapp.ui.IngredientStepActivity.POSITION;
import static co.enoobong.bakingapp.ui.IngredientStepAdapter.INGREDIENTS;
import static co.enoobong.bakingapp.ui.IngredientStepAdapter.STEPS;


public class IngredientStepDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_step_detail);
        if (savedInstanceState == null) {
            ArrayList<Ingredient> ingredients = getIntent().getParcelableArrayListExtra(INGREDIENTS);
            ArrayList<Step> steps = getIntent().getParcelableArrayListExtra(STEPS);
            int position = getIntent().getIntExtra(POSITION, 0);
            boolean mTwoPane = getIntent().getBooleanExtra(PANES, false);
            IngredientStepDetailFragment fragment = new IngredientStepDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(POSITION, position);
            bundle.putBoolean(PANES, mTwoPane);
            bundle.putParcelableArrayList(INGREDIENTS, ingredients);
            bundle.putParcelableArrayList(STEPS, steps);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, fragment)
                    .commit();

        }
    }

}
