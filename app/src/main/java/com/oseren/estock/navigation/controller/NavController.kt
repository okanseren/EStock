package com.oseren.estock.navigation.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.oseren.estock.navigation.graph.authGraph
import com.oseren.estock.navigation.graph.homeGraph
import com.oseren.estock.navigation.graph.shoppingCartGraph
import com.oseren.estock.navigation.route.AUTHENTICATION_ROUTE
import com.oseren.estock.navigation.route.HOME_ROUTE
import com.oseren.estock.screen.home.HomeViewModel

@Composable
fun NavController(homeViewModel: HomeViewModel = hiltViewModel()) {

    val navController = rememberNavController()

    val userState by homeViewModel.userState.collectAsState()

    val startDestination = if (userState == null) {
        AUTHENTICATION_ROUTE
    } else {
        HOME_ROUTE
    }

    NavHost(navController = navController
        , startDestination = startDestination
    ) {
        homeGraph(navController = navController)
        shoppingCartGraph(navController = navController)
        authGraph(navController = navController)
    }
    
}