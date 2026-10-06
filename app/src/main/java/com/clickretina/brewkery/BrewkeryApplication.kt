package com.clickretina.brewkery

import android.app.Application
import com.clickretina.brewkery.data.remote.RetrofitClient
import com.clickretina.brewkery.data.repository.BrewkeryRepository
import com.clickretina.brewkery.data.repository.BrewkeryRepositoryImpl

class BrewkeryApplication : Application() {

    lateinit var repository: BrewkeryRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = BrewkeryRepositoryImpl(
            apiService = RetrofitClient.apiService,
            appContext = this
        )
    }
}
