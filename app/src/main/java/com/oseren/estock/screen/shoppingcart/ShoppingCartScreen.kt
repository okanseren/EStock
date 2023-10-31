package com.oseren.estock.screen.shoppingcart

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShoppingCartScreen(navController: NavController
                       , shoppingCartViewModel: ShoppingCartViewModel = hiltViewModel()) {

    val totalPrice by shoppingCartViewModel.price.collectAsState()

    Scaffold(topBar = {

        ShoppingCenteredTopBars(title = "Sepetim"
        , navigationClick = { navController.popBackStack() })

    }
        , content = { CustomContent(navController = navController) }
        , bottomBar = { ShoppingBottomBars(totalPrice = totalPrice) })
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CustomContent(shoppingCartViewModel: ShoppingCartViewModel = hiltViewModel()
                  , navController: NavController) {

    val data by shoppingCartViewModel.shoppingCartData.collectAsState()

    val newData = data.data.sortedBy { it.stock.name.length }.reversed()

    Column(modifier = Modifier.padding(top = 76.dp
        , end = 12.dp
        , start = 12.dp
        , bottom = 108.dp)) {

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)
            , modifier = Modifier
            .fillMaxWidth()) {
            items(items = newData
                , key = { it.docID }) {

                ShoppingCartCardDesign(count = it.count,
                    name = it.stock.name,
                    photo = it.stock.photo,
                    price = it.stock.price.toString(),
                    units = it.stock.units,
                    unitsValue = it.stock.unit_value.toString(),
                    increase = {
                        shoppingCartViewModel.increaseShoppingCartData(it.docID)
                    },
                    decrease = {
                        if (data.data.size != 1) {
                            if (it.count != 1) {
                                shoppingCartViewModel.decreaseShoppingCartData(it.docID)

                            } else {
                                shoppingCartViewModel.deleteShoppingCartData(it.docID)
                            }
                        } else {
                            if (it.count != 1) {
                                shoppingCartViewModel.decreaseShoppingCartData(it.docID)

                            } else {
                                shoppingCartViewModel.deleteShoppingCartData(it.docID)
                                navController.popBackStack()
                            }
                        }
                    })
            }
        }
        
        if (data.data.isEmpty()) {
            Box(contentAlignment = Alignment.Center
                , modifier = Modifier
                .fillMaxSize()) {
                Text(text = "Sepetin boş gözüküyor")
            }
        }
    }
}

