package com.clickretina.brewkery.data.repository

import com.clickretina.brewkery.data.model.MenuItem
import com.clickretina.brewkery.data.model.MenuResponse
import com.clickretina.brewkery.data.remote.NetworkResult

interface BrewkeryRepository {
    suspend fun getFullMenu(): NetworkResult<MenuResponse>
    suspend fun getItemDetail(id: Int): NetworkResult<MenuItem>
}
