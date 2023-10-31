package com.oseren.estock.navigation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.oseren.estock.navigation.route.AUTHENTICATION_ROUTE
import com.oseren.estock.navigation.route.Routes
import com.oseren.estock.screen.auth.login.LoginScreen
import com.oseren.estock.screen.auth.register.RegisterScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(route = AUTHENTICATION_ROUTE
        , startDestination = Routes.RegisterScreen.route) {
        composable(route = Routes.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Routes.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
    }
}