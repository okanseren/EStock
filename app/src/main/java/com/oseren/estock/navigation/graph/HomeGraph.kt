package com.oseren.estock.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.oseren.estock.navigation.route.HOME_ROUTE
import com.oseren.estock.navigation.route.Routes
import com.oseren.estock.screen.category.CategoryScreen
import com.oseren.estock.screen.home.HomeScreen
import com.oseren.estock.screen.search.SearchScreen

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation(route = HOME_ROUTE
        , startDestination = Routes.HomeScreen.route) {
        composable(route = Routes.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = "${Routes.InCategoryScreen.route}/{category}"
            , arguments = listOf(navArgument(name = "category") {
                type = NavType.StringType
                defaultValue = ""
            })) {
            CategoryScreen(navController = navController
                , category = it.arguments?.getString("category"))
        }
        composable(route = Routes.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
    }
}