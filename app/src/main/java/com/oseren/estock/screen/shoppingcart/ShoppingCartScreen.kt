package com.oseren.estock.screen.shoppingcart

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.oseren.estock.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShoppingCartScreen(navController: NavController
                       , shoppingCartViewModel: ShoppingCartViewModel = hiltViewModel()) {

    val totalPrice by shoppingCartViewModel.price.collectAsState()

    Scaffold(topBar = {

        ShoppingCenteredTopBars(title = stringResource(id = R.string.shopping_cart_title)
        , navigationClick = { navController.popBackStack() })

    }
        , content = { CustomContent(navController = navController) }
        , bottomBar = { ShoppingBottomBars(totalPrice = totalPrice) })
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CustomContent(shoppingCartViewModel: ShoppingCartViewModel = hiltViewModel()
                  , navController: NavController) {

    val shoppingCartData by shoppingCartViewModel.shoppingCartData.collectAsState()

    val newData = shoppingCartData.data.sortedBy { it.stock.name.length }.reversed()

    Column(modifier = Modifier.padding(top = 76.dp
        , end = 12.dp
        , start = 12.dp
        , bottom = 108.dp)) {

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)
            , modifier = Modifier
            .fillMaxWidth()) {
            items(items = newData
                , key = { it.docID }) {

                val isLoadingState = shoppingCartViewModel.isLoadingForItem(it.docID)

                ShoppingCartCardDesign(count = it.count,
                    data = it,
                    isLoading = isLoadingState.value,
                    increase = {
                        CoroutineScope(Dispatchers.Default).launch {
                            shoppingCartViewModel.increaseShoppingCartData(it.docID)
                        }
                    },
                    decrease = {
                        CoroutineScope(Dispatchers.Main).launch {
                            if (shoppingCartData.data.size != 1) {
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
                        }
                    })

            }
        }
    }
}

