/*
 * Copyright (c) 2017. Ibanga Enoobong Ime (World class developer and entrepreneur in transit)
 */

package co.enoobong.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.enoobong.bakingapp.data.RecipeContract.RecipeEntry;


public final class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipe.db";

    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeEntry.RECIPE_TABLE + " (" +
            RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
            RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
            RecipeEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
            RecipeEntry.COLUMN_INGREDIENT_MEASURE + " TEXT NOT NULL, " +
            RecipeEntry.COLUMN_INGREDIENT_QUANTITY + " INTEGER NOT NULL " +
            ");";

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.RECIPE_TABLE);
        onCreate(db);
    }
}
