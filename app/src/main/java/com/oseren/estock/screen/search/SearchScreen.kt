package com.oseren.estock.screen.search

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController
                 , searchViewModel: SearchViewModel = hiltViewModel()) {

    val data = searchViewModel.searchData.value

    Scaffold(content = { SearchContent(data = data) }
        , topBar = {

            SearchTopAppBars(searchViewModel = searchViewModel
            , navigationClick = { navController.popBackStack() })

        })

}