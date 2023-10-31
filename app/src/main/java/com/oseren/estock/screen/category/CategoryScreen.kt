package com.oseren.estock.screen.category

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.oseren.estock.screen.home.shimmerEffect
import com.oseren.estock.screen.shoppingcart.ShoppingCartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoryScreen(navController: NavController, category: String?) {

    Scaffold(topBar = {
        CategoryCenteredTopBars(title = " $category Kategorisi"
        , navigationClick = { navController.popBackStack() })
    }
        , content = { CustomContent(category) })

}

@Composable
private fun CustomContent(category : String?
                          , categoryViewModel: CategoryViewModel = hiltViewModel()) {

    val categoryData = categoryViewModel.categoryData.value

    val shoppingCartVM: ShoppingCartViewModel = hiltViewModel()

    LaunchedEffect(category) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryViewModel.getCategoryInside(category = category.toString())
        }

    }


    Column(horizontalAlignment = CenterHorizontally
        , modifier = Modifier
            .padding(top = 76.dp
                , bottom = 12.dp
                , start = 12.dp
                , end = 12.dp)) {
        LazyVerticalGrid(columns = GridCells.Fixed(count = 3)
            , content = {
                items(categoryData.data) {

                    val state = remember { mutableStateOf(false) }
                    var count by remember { mutableIntStateOf(1) }
                    val isLoadingState = shoppingCartVM.isLoadingForItem(it.ref!!)

                    if(categoryData.isLoading) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Box(modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .shimmerEffect())

                            Box(modifier = Modifier
                                .width(100.dp)
                                .height(14.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .shimmerEffect())

                            Box(modifier = Modifier
                                .width(100.dp)
                                .height(14.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .shimmerEffect())

                            Box(modifier = Modifier
                                .width(100.dp)
                                .height(14.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .shimmerEffect())
                        }
                    } else {
                        CategoryCard(state = state.value,
                            count = count,
                            data = it,
                            isLoading = isLoadingState.value,
                            addToCart = {
                                CoroutineScope(Dispatchers.Default).launch {
                                    if (!state.value) {
                                        state.value = true
                                        shoppingCartVM.addToCart(it.ref!!)
                                    } else {
                                        count++
                                        shoppingCartVM.addToCart(it.ref!!)
                                    }
                                }
                            },
                            removeFromCart = {
                                CoroutineScope(Dispatchers.Default).launch {
                                    if(count != 1) {
                                        shoppingCartVM.removeFromCart(it.ref!!)
                                        count--
                                    } else {
                                        shoppingCartVM.removeFromCart(it.ref!!)
                                        state.value = false
                                    }
                                }
                            })
                    }
                }

            }, verticalArrangement = Arrangement.spacedBy(12.dp)
            , horizontalArrangement = Arrangement.spacedBy(4.dp))

    }

}


