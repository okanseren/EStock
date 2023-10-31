package com.oseren.estock.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.oseren.estock.navigation.route.AUTHENTICATION_ROUTE
import com.oseren.estock.navigation.route.HOME_ROUTE
import com.oseren.estock.navigation.route.Routes
import com.oseren.estock.screen.auth.AuthUIEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController
               , homeViewModel: HomeViewModel = hiltViewModel()) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        homeViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AuthUIEvents.NavigateEvent -> {
                    navController.navigate(AUTHENTICATION_ROUTE) {
                        popUpTo(HOME_ROUTE) {
                            inclusive = true
                        }
                    }
                }
                is AuthUIEvents.SnackbarEvent -> { }
            }
        }
    }

    ModalNavigationDrawer(drawerState = drawerState
        , drawerContent = {
            HomeModalDrawer(firstName = homeViewModel.userData.value.firstName
                , lastName = homeViewModel.userData.value.lastName
                , logoutButton = { homeViewModel.logoutUser() }
                , shoppingCartClick = {
                    coroutineScope.launch {
                        navController.navigate(Routes.ShoppingCartScreen.route)
                        delay(200)
                        drawerState.close()
                    }
                })
        }
        , content = {
            Scaffold(topBar = { HomeCenteredTopBars(title = "eStock"
                , navigationClick = {
                coroutineScope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            })
        }
                , content = { HomeContent(navController = navController) })
        })

}

@Composable
fun HomeContent(navController: NavController
                , homeViewModel: HomeViewModel = hiltViewModel()) {

    val category = homeViewModel.category.value

    val image = listOf("https://wallpapers.com/images/hd/purple-anime-city-hld4uxffs8qsb359.webp"
        , "https://wallpapers.com/images/hd/sunset-hill-road-iwsvff46ia8qhgg5.webp"
        , "https://wallpapers.com/images/hd/red-forest-on-fall-rl23gqazljr1ee47.webp")

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)
        , modifier = Modifier
            .padding(top = 76.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
            .fillMaxSize()) {

        HomeSearchBar(onClick = { navController.navigate(Routes.SearchScreen.route) })

        HomePager(images = image)

        CategoryContent(categoryData = category
            , navController = navController)
    }
}
