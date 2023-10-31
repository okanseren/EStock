package com.oseren.estock.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.oseren.estock.navigation.route.Routes
import com.oseren.estock.navigation.route.SHOPPING_CART_ROUTE
import com.oseren.estock.screen.shoppingcart.ShoppingCartScreen

fun NavGraphBuilder.shoppingCartGraph(navController: NavController) {
    navigation(route = SHOPPING_CART_ROUTE
        , startDestination = Routes.ShoppingCartScreen.route) {
        composable(route = Routes.ShoppingCartScreen.route) {
            ShoppingCartScreen(navController = navController)
        }
    }
}