/*
 * Copyright (c) 2017. Ibanga Enoobong Ime (World class developer and entrepreneur in transit)
 */

package co.enoobong.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.enoobong.bakingapp.R;
import co.enoobong.bakingapp.data.Recipe;
import co.enoobong.bakingapp.ui.IngredientStepAdapter.OnIngredientStepListener;

import static co.enoobong.bakingapp.ui.RecipeAdapter.RECIPE;


public class MasterListFragment extends Fragment {


    @BindView(R.id.rv_ingredients_steps)
    RecyclerView mIngredientStepRecyclerView;
    private Recipe mRecipe;
    private IngredientStepAdapter mIngredientStepAdapter;
    private OnIngredientStepListener mClickListener;

    public MasterListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mClickListener = (OnIngredientStepListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE);
        }
        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, rootView);
        mRecipe = getActivity().getIntent().getParcelableExtra(RECIPE);
        mIngredientStepAdapter = new IngredientStepAdapter(getContext(), mRecipe, mClickListener);
        mIngredientStepRecyclerView.setAdapter(mIngredientStepAdapter);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE, mRecipe);
    }
}
