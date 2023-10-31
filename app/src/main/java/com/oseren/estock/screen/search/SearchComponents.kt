package com.oseren.estock.screen.search

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.oseren.estock.R
import com.oseren.estock.domain.model.StockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBars(searchViewModel: SearchViewModel
                     , navigationClick: () -> Unit) {

    val searchText by searchViewModel.searchText.collectAsState()

    TopAppBar(title = {

        BasicTextField(value = searchText
            , onValueChange = searchViewModel::search
            , modifier = Modifier
                .height(48.dp)
                .clip(RoundedCornerShape(25))
                .fillMaxWidth()
            , textStyle = TextStyle(fontSize = 18.sp)
            , singleLine = true) {

            TextFieldDefaults.TextFieldDecorationBox(value = searchText
                , innerTextField = it
                , singleLine = true
                , enabled = true
                , visualTransformation = VisualTransformation.None
                , trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchViewModel.search("") }) {
                            Icon(
                                Icons.Default.Clear
                                , contentDescription = "")
                        }
                    }
                }
                , placeholder = {
                    Text(text = "Ürün arayın",
                        fontSize = 18.sp,)
                }
                , interactionSource = MutableInteractionSource()
                , contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(top = 0.dp, bottom = 0.dp)
                , colors = TextFieldDefaults.textFieldColors(focusedIndicatorColor = Color.Transparent
                    , unfocusedIndicatorColor = Color.Transparent
                    , disabledIndicatorColor = Color.Transparent))
        }
    }
        , navigationIcon = {
            IconButton(onClick = navigationClick) {
                Icon(painter = painterResource(id = R.drawable.arrow_back24px)
                    , contentDescription = "")
            }
        })
}

@Composable
fun SearchContent(data: Search) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 12.dp, top = 76.dp, end = 12.dp, bottom = 12.dp))  {


        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            items(data.data) {
                SearchCardDesign(data = it)
            }
        }

        if (data.isLoading) {
            Box(modifier = Modifier.fillMaxSize()
                , contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun SearchCardDesign(data: StockData) {
    Row(verticalAlignment = Alignment.CenterVertically
        , horizontalArrangement = Arrangement.SpaceBetween
        , modifier = Modifier
            .fillMaxWidth()) {
        Card {
            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(data.photo)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = data.name)
            Text(text = data.price.toString())
            Text(text = "${data.unit_value} ${data.units}")
        }
    }
}
