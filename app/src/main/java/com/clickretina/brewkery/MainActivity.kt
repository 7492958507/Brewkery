package com.clickretina.brewkery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.clickretina.brewkery.ui.navigation.BrewkeryNavGraph
import com.clickretina.brewkery.ui.theme.BrewkeryTheme
import com.clickretina.brewkery.ui.viewmodel.BrewkeryViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: BrewkeryViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as BrewkeryApplication
                return BrewkeryViewModel(app.repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrewkeryTheme {
                val navController = rememberNavController()
                BrewkeryNavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
