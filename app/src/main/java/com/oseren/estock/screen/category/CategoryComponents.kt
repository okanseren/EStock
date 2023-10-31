package com.oseren.estock.screen.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.oseren.estock.R
import com.oseren.estock.domain.model.StockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCenteredTopBars(title: String
                    , navigationClick: () -> Unit) {
    CenterAlignedTopAppBar(title = { Text(text = title) }
        , colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface
            , titleContentColor = MaterialTheme.colorScheme.onSurface)
        , navigationIcon = {
            IconButton(onClick = navigationClick) {
                Icon(painter = painterResource(id = R.drawable.arrow_back24px)
                    , contentDescription = "")
            }
        })
}

@Composable
fun CategoryCard(state: Boolean
                 , count: Int
                 , data: StockData
                 , isLoading: Boolean
                 , addToCart: () -> Unit
                 , removeFromCart: () -> Unit) {

    Box(modifier = Modifier.width(122.dp)) {
        Box(modifier = Modifier
            .zIndex(1f)
            .padding(start = 84.dp, top = 8.dp)
            // start = 68.dp, top = 24.dp
            .width(32.dp)
            .clip(RoundedCornerShape(6.dp))) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Surface(color = MaterialTheme.colorScheme.secondaryContainer
                    , modifier = Modifier
                        .size(32.dp)
                    , onClick = { if (!isLoading) addToCart.invoke() }
                    , content = {
                        Icon(painter = painterResource(id = R.drawable.plus24px)
                            , contentDescription = ""
                            , tint = MaterialTheme.colorScheme.onSecondaryContainer)
                    })
                AnimatedVisibility(visible = state) {
                    Column {
                        Surface(color = MaterialTheme.colorScheme.primaryContainer
                            , modifier = Modifier
                                .size(32.dp)
                            , content = {
                                if (isLoading) {
                                    CircularProgressIndicator()
                                } else {

                                    Text(text = count.toString()
                                        , fontSize = 22.sp
                                        , textAlign = TextAlign.Center
                                        , color = MaterialTheme.colorScheme.onPrimaryContainer)
                                }
                            })

                        Surface(color = MaterialTheme.colorScheme.secondaryContainer
                            , modifier = Modifier
                                .size(32.dp)
                            , onClick = { if (!isLoading) removeFromCart.invoke() }
                            , content = {
                                Icon(painter = painterResource(id = R.drawable.minus24px)
                                    , contentDescription = ""
                                    , tint = MaterialTheme.colorScheme.onSecondaryContainer)
                            })
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(top = 24.dp)) {
            Card {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data.photo)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp))
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = data.name)
            Text(text = data.price.toString())
            Text(text = "${data.unit_value} ${data.units}")
        }
    }
}
