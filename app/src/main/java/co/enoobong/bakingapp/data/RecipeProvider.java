/*
 * Copyright (c) 2017. Ibanga Enoobong Ime (World class developer and entrepreneur in transit)
 */

package co.enoobong.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static co.enoobong.bakingapp.data.RecipeContract.CONTENT_AUTHORITY;
import static co.enoobong.bakingapp.data.RecipeContract.PATH_RECIPE;
import static co.enoobong.bakingapp.data.RecipeContract.RECIPE_CONTENT_URI;
import static co.enoobong.bakingapp.data.RecipeContract.RecipeEntry;

public class RecipeProvider extends ContentProvider {

    public static final int CODE_RECIPE = 100;
    public static final int CODE_RECIPE_WITH_ID = 101;

    public static final String ACTION_DATA_UPDATED =
            "co.enoobong.bakingapp.ACTION_DATA_UPDATED";

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private RecipeDbHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_RECIPE, CODE_RECIPE);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_RECIPE + "/#", CODE_RECIPE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPE_WITH_ID:
                cursor = db.query(RecipeEntry.RECIPE_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_RECIPE:
                cursor = db.query(RecipeEntry.RECIPE_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        long id;

        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPE_WITH_ID:
                id = db.insert(RecipeEntry.RECIPE_TABLE, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RECIPE_CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case CODE_RECIPE:
                id = db.insertWithOnConflict(RecipeEntry.RECIPE_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RECIPE_CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        updateWidgets();

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numberOfDeletedIngredients;
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPE_WITH_ID:
                numberOfDeletedIngredients = db.delete(RecipeEntry.RECIPE_TABLE,
                        selection,
                        selectionArgs);
                break;
            case CODE_RECIPE:
                numberOfDeletedIngredients = db.delete(RecipeEntry.RECIPE_TABLE, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);

        }

        if (numberOfDeletedIngredients != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            updateWidgets();
        }
        return numberOfDeletedIngredients;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void updateWidgets() {
        Context context = getContext();
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }
}
