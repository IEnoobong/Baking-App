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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.enoobong.bakingapp.R;
import co.enoobong.bakingapp.data.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public static final String RECIPE = "recipe";
    private Context mContext;
    private ArrayList<Recipe> mRecipes;

    public RecipeAdapter(final Context context, ArrayList<Recipe> mRecipes) {
        this.mContext = context;
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
        holder.setRecipeImage(mContext, mRecipes.get(position).getImageUrl());
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
        @BindView(R.id.iv_recipe_image)
        ImageView mRecipeImage;

        public RecipeViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void setRecipeName(final String recipeName) {
            mRecipeName.setText(recipeName);
        }

        void setRecipeImage(final Context context, final String recipeImage) {
            if (!recipeImage.isEmpty()) {
                mRecipeImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(recipeImage)
                        .into(mRecipeImage);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, IngredientStepActivity.class);
            intent.putExtra(RECIPE, mRecipes.get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}
