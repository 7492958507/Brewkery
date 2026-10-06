package com.clickretina.brewkery.data.repository

import android.content.Context
import com.clickretina.brewkery.data.model.MenuItem
import com.clickretina.brewkery.data.model.MenuResponse
import com.clickretina.brewkery.data.remote.BrewkeryApiService
import com.clickretina.brewkery.data.remote.NetworkResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BrewkeryRepositoryImpl(
    private val apiService: BrewkeryApiService,
    private val appContext: Context? = null
) : BrewkeryRepository {

    private val gson = Gson()

    override suspend fun getFullMenu(): NetworkResult<MenuResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getFullMenu()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            // Attempt fallback to local asset if network is completely unreachable
            val assetFallback = loadMenuFromAssets()
            if (assetFallback != null) {
                NetworkResult.Success(assetFallback)
            } else {
                NetworkResult.Error(
                    message = e.localizedMessage ?: "Failed to connect to Brewkery servers. Please check your internet connection.",
                    throwable = e
                )
            }
        }
    }

    override suspend fun getItemDetail(id: Int): NetworkResult<MenuItem> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getItemDetail(id)
            NetworkResult.Success(response)
        } catch (e: Exception) {
            // Fallback from cached asset menu
            val assetFallback = loadMenuFromAssets()
            val item = assetFallback?.items?.find { it.id == id }
            if (item != null) {
                NetworkResult.Success(item)
            } else {
                NetworkResult.Error(
                    message = e.localizedMessage ?: "Unable to fetch item #$id. Please retry.",
                    throwable = e
                )
            }
        }
    }

    private fun loadMenuFromAssets(): MenuResponse? {
        return try {
            appContext?.assets?.open("data.json")?.use { inputStream ->
                val json = inputStream.bufferedReader().use { it.readText() }
                gson.fromJson(json, MenuResponse::class.java)
            }
        } catch (_: Exception) {
            null
        }
    }
}
