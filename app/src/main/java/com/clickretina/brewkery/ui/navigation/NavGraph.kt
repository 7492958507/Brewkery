package com.clickretina.brewkery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.clickretina.brewkery.ui.screens.cart.CartScreen
import com.clickretina.brewkery.ui.screens.detail.ItemDetailScreen
import com.clickretina.brewkery.ui.screens.menu.MenuScreen
import com.clickretina.brewkery.ui.screens.order.OrderStatusScreen
import com.clickretina.brewkery.ui.viewmodel.BrewkeryViewModel

@Composable
fun BrewkeryNavGraph(
    navController: NavHostController,
    viewModel: BrewkeryViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Menu.route
    ) {
        composable(Screen.Menu.route) {
            MenuScreen(
                viewModel = viewModel,
                onItemClick = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                },
                onCartClick = {
                    navController.navigate(Screen.Cart.route)
                },
                onTrackOrderClick = {
                    navController.navigate(Screen.OrderStatus.route)
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 1
            ItemDetailScreen(
                itemId = itemId,
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onAddedToCart = {
                    navController.navigate(Screen.Cart.route) {
                        popUpTo(Screen.Menu.route)
                    }
                }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onOrderPlaced = {
                    navController.navigate(Screen.OrderStatus.route) {
                        popUpTo(Screen.Menu.route)
                    }
                }
            )
        }

        composable(Screen.OrderStatus.route) {
            OrderStatusScreen(
                viewModel = viewModel,
                onBackToMenu = {
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Menu.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
