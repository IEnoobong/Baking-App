/*
 * Copyright (c) 2017. Ibanga Enoobong Ime (World class developer and entrepreneur in transit)
 */

package co.enoobong.bakingapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.enoobong.bakingapp.R;
import co.enoobong.bakingapp.data.Ingredient;


public class IngredientListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<Ingredient> mIngredients;

    public IngredientListAdapter(ArrayList<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_header, parent, false);
            return new IngredientHeaderViewHolder(rootView);
        } else if (viewType == TYPE_ITEM) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
            return new IngredientViewHolder(rootView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof IngredientViewHolder) {
            ((IngredientViewHolder) holder).mIngredient.setText(mIngredients.get(position - 1).getIngredient());
            ((IngredientViewHolder) holder).mMeasure.setText(mIngredients.get(position - 1).getMeasure());
            ((IngredientViewHolder) holder).mQuantity.setText(String.valueOf(mIngredients.get(position - 1).getQuantity()));
        }

    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) {
            return 0;
        }
        return mIngredients.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    static class IngredientHeaderViewHolder extends RecyclerView.ViewHolder {

        public IngredientHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient)
        TextView mIngredient;
        @BindView(R.id.tv_measure)
        TextView mMeasure;
        @BindView(R.id.tv_quantity)
        TextView mQuantity;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
