package com.oseren.estock.screen.shoppingcart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.oseren.estock.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCenteredTopBars(title: String
                            , navigationClick: () -> Unit) {

    CenterAlignedTopAppBar(title = { Text(text = title) }
        , navigationIcon = {
            IconButton(onClick = navigationClick) {
                Icon(painter = painterResource(id = R.drawable.arrow_back24px)
                    , contentDescription = "")
            }
        })
}

@Composable
fun ShoppingBottomBars(totalPrice: Double) {

    if (totalPrice != 0.0) {
        BottomAppBar(modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))) {

            Button(onClick = { /*TODO*/ }
                , modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 8.dp)
                , shape = RoundedCornerShape(12.dp)
                , contentPadding = PaddingValues()) {

                Row(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer)) {

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .weight(5f)
                        , contentAlignment = Alignment.Center) {

                        Text(text = "Siparişi Onayla"
                            , fontSize = 18.sp
                            , color = MaterialTheme.colorScheme.onPrimaryContainer)

                    }

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .weight(3f)
                        .padding(horizontal = 12.dp)
                        , contentAlignment = Alignment.Center) {

                        Text(text = "₺ ${totalPrice.toFloat()}"
                            , fontSize = 20.sp
                            , color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartCardDesign(count: Int
                     , name: String
                     , photo: String
                     , price: String
                     , units: String
                     , unitsValue: String
                     , increase: () -> Unit
                     , decrease: () -> Unit) {

    Row(verticalAlignment = Alignment.CenterVertically
        , horizontalArrangement = Arrangement.SpaceBetween
        , modifier = Modifier
            .fillMaxWidth()) {
        Card {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp))
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = name)
            Text(text = price)
            Text(text = "$unitsValue $units")
        }

        Box(modifier = Modifier
            .height(32.dp)
            .width(96.dp)
            .clip(RoundedCornerShape(6.dp))) {

            Row {
                Surface(color = MaterialTheme.colorScheme.secondaryContainer
                    , modifier = Modifier
                        .fillMaxHeight()
                        .width(32.dp)
                    , onClick = decrease) {
                    Icon(painter = painterResource(id = R.drawable.minus24px)
                        , contentDescription = ""
                        , tint = MaterialTheme.colorScheme.onSecondaryContainer)
                }

                Surface(color = MaterialTheme.colorScheme.primaryContainer
                    , modifier = Modifier
                        .fillMaxHeight()
                        .width(32.dp)) {
                    Text(text = count.toString()
                        , fontSize = 22.sp
                        , textAlign = TextAlign.Center
                        , color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Surface(color = MaterialTheme.colorScheme.secondaryContainer
                    , modifier = Modifier
                        .fillMaxHeight()
                        .width(32.dp)
                    , onClick = increase) {
                    Icon(painter = painterResource(id = R.drawable.plus24px)
                        , contentDescription = ""
                        , tint = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
        }
    }
}