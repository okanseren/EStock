package com.oseren.estock.screen.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.oseren.estock.R
import com.oseren.estock.navigation.route.Routes
import kotlin.math.absoluteValue

@Composable
fun HomeModalDrawer(firstName: String
                    , lastName: String
                    , logoutButton: () -> Unit
                    , shoppingCartClick: () -> Unit) {

    ModalDrawerSheet(modifier = Modifier.width(240.dp)) {

        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier
                .fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically
                    , horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    
                    Card(shape = RoundedCornerShape(50)
                        , modifier = Modifier.size(90.dp)) {

                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = firstName
                            , fontSize = 18.sp)
                        Text(text = lastName
                            , fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider(modifier = Modifier.fillMaxWidth())

            Column {
                NavigationDrawerItem(label = { Text(text = stringResource(id = R.string.home_drawer_shoppingcart)) }
                    , selected = false
                    , onClick = shoppingCartClick
                    , icon = {
                        Icon(painter = painterResource(id = R.drawable.shopping_bag24px)
                            , contentDescription = "")
                    })
            }
            Divider(modifier = Modifier.fillMaxWidth())

            Column {
                TextButton(onClick = logoutButton) {
                    Text(text = stringResource(id = R.string.home_drawer_logout))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCenteredTopBars(title: String
                    , navigationClick: () -> Unit) {
    CenterAlignedTopAppBar(title = { Text(text = title) }
        , colors = centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
            , titleContentColor = MaterialTheme.colorScheme.onSurface)
        , navigationIcon = {
            IconButton(onClick = navigationClick) {
                Icon(painter = painterResource(id = R.drawable.menu24px)
                    , contentDescription = "")
            }
        })
}

@Composable
fun HomeSearchBar(onClick: () -> Unit) {

    Box(modifier = Modifier
        .height(48.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(25))
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .clickable(onClick = onClick)
        , contentAlignment = Alignment.CenterStart) {
        
        Row {

            Spacer(modifier = Modifier.width(12.dp))
            
            Icon(Icons.Default.Search, contentDescription = "")

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = stringResource(id = R.string.search_bar_placeholder),
                fontSize = 18.sp)
            
        }

    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePager(images: List<String>) {

    val pagerState = rememberPagerState(pageCount = { images.size }
        , initialPage = 1)

//    LaunchedEffect(
//        key1 = Unit,
//        block = {
//            repeat(times = Int.MAX_VALUE,
//                action = {
//                    delay(
//                        timeMillis = 4000
//                    )
//                    pagerState.animateScrollToPage(
//                        page = pagerState.currentPage + 1
//                    )
//                }
//            )
//        })

    HorizontalPager(state = pagerState
        , contentPadding = PaddingValues(horizontal = 24.dp)
        , pageSpacing = 8.dp
        , pageContent = { page ->

            val showShimmer = remember { mutableStateOf(true) }

            Card(modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .graphicsLayer {
                    val pageOffset = ((pagerState.currentPage - page)
                            + pagerState.currentPageOffsetFraction).absoluteValue

                    alpha = lerp(
                        start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                    scaleY = lerp(
                        start = 1f, stop = 0.85f, fraction = pageOffset.coerceIn(0f, 1f)
                    )
                }) {

                AsyncImage(model = images[page]
                    , contentDescription = ""
                    , modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = showShimmer.value
                            )
                        )
                    , contentScale = ContentScale.Crop
                    , onSuccess = { showShimmer.value = false })
            }
        })
}


@Composable
fun shimmerBrush(showShimmer: Boolean = true,targetValue:Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition(label = "")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent,Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}


@Composable
fun CategoryContent(categoryData: ItemState
                 , navController: NavController) {

    Column {
        Text(text = stringResource(id = R.string.home_category_title)
            , modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
            , textAlign = TextAlign.Left)

        LazyRow(content = {
            items(items = categoryData.data
                , itemContent = {

                    if(categoryData.isLoading) {
                        Box(modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .shimmerEffect()) {
                        }
                    } else {
                        CategoryCardDesign(categoryName = it.category
                            , categoryPhoto = it.photo
                            , onClick = {
                                navController.navigate("${Routes.InCategoryScreen.route}/${it.category}")
                            })
                    }

                })
        }
            , horizontalArrangement = Arrangement.spacedBy(12.dp))
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCardDesign(categoryName: String
                       , categoryPhoto: String
                       , onClick: () -> Unit) {

    Card(colors = cardColors(MaterialTheme.colorScheme.secondaryContainer)
        , onClick = onClick
        , modifier = Modifier
            .size(height = 100.dp
                , width = 100.dp)) {

        AsyncImage(model = categoryPhoto
            , contentDescription = ""
            , modifier = Modifier
                .graphicsLayer { alpha = 0.90f }
                .drawWithContent {
                    val colors =
                        listOf(Color.Black, Color.Black, Color.Transparent)
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(colors), blendMode = BlendMode.DstIn
                    )
                }
                .size(100.dp, height = 70.dp)
            , contentScale = ContentScale.Crop)

        Text(text = categoryName
            , modifier = Modifier
                .fillMaxWidth()
            , textAlign = TextAlign.Center
            , color = MaterialTheme.colorScheme.onSecondaryContainer)
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000))
        , label = "")

    background(brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())))
        .onGloballyPositioned {
            size = it.size
        }
}