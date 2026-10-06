package com.clickretina.brewkery.data.remote

import com.clickretina.brewkery.data.model.MenuItem
import com.clickretina.brewkery.data.model.MenuResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BrewkeryApiService {

    @GET("data.json")
    suspend fun getFullMenu(): MenuResponse

    @GET("api/items/{id}.json")
    suspend fun getItemDetail(
        @Path("id") id: Int
    ): MenuItem
}
