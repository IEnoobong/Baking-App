/*
 * Copyright (c) 2017. Ibanga Enoobong Ime (World class developer and entrepreneur in transit)
 */

package co.enoobong.bakingapp.network;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<JsonArray> getRecipe();
}
