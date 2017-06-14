/*
 * Copyright (c) 2017. Ibanga Enoobong Ime (World class developer and entrepreneur in transit)
 */

package co.enoobong.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.enoobong.bakingapp.R;
import co.enoobong.bakingapp.data.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public static final String RECIPE = "recipe";
    private Context mContext;
    private ArrayList<Recipe> mRecipes;

    public RecipeAdapter(final Context mContext, ArrayList<Recipe> mRecipes) {
        this.mContext = mContext;
        this.mRecipes = mRecipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, final int position) {
        holder.setRecipeName(mRecipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        }
        return mRecipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_name)
        TextView mRecipeName;

        public RecipeViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void setRecipeName(final String recipeName) {
            mRecipeName.setText(recipeName);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, IngredientStepActivity.class);
            intent.putExtra(RECIPE, mRecipes.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}
