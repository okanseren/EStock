@file:OptIn(ExperimentalMaterial3Api::class)

package com.oseren.estock.screen.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oseren.estock.R
import com.oseren.estock.navigation.route.Routes
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var job: Job? = null
@Composable
fun InfoTextField(placeholder: String
                  , painter: Painter
                  , onTextChange: (String) -> Unit
                  , modifier: Modifier) {

    var text by remember {
        mutableStateOf("")
    }

    var isError2 by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    TextField(value = text
        , onValueChange = {
            job?.cancel()
            text = it
            onTextChange(it)
            job = coroutineScope.launch {
                delay(600)
                isError2 = AuthValidator.usernameValidator(it)
            }
        }
        , singleLine = true
        , placeholder = { Text(text = placeholder) }
        , shape = RoundedCornerShape(50)
        , colors = TextFieldDefaults.textFieldColors(unfocusedIndicatorColor = Color.Transparent
            , focusedIndicatorColor = Color.Transparent
            , disabledIndicatorColor = Color.Transparent
            , errorIndicatorColor = Color.Transparent)
        , leadingIcon = {
            Icon(painter = painter
                , contentDescription = "")
        }
        , isError = isError2
        , supportingText = {
            if (isError2) {
                Text(text = "3 karakterden uzun olmalıdır"
                    , fontSize = 16.sp)
            }
        }
        , keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        , modifier = modifier
            .fillMaxWidth())
}

@Composable
fun EmailTextField(placeholder: String
                   , painter: Painter
                   , onTextChange: (String) -> Unit
                   , modifier: Modifier) {

    var text by remember {
        mutableStateOf("")
    }

    var isError2 by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    TextField(value = text
        , onValueChange = {
            job?.cancel()
            text = it
            onTextChange(it)
            job = coroutineScope.launch {
                delay(600)
                isError2 = AuthValidator.emailValidator(it)
            }
        }
        , singleLine = true
        , placeholder = { Text(text = placeholder) }
        , shape = RoundedCornerShape(50)
        , colors = TextFieldDefaults.textFieldColors(unfocusedIndicatorColor = Color.Transparent
            , focusedIndicatorColor = Color.Transparent
            , disabledIndicatorColor = Color.Transparent
            , errorIndicatorColor = Color.Transparent)
        , leadingIcon = {
            Icon(painter = painter
                , contentDescription = "")
        }
        , isError = isError2
        , supportingText = {
            if (isError2) {
                Text(text = "Geçersiz email syntaxı"
                    , fontSize = 16.sp)
            }
        }
        , keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next
            , keyboardType = KeyboardType.Email)
        , modifier = modifier
            .fillMaxWidth())
}


@Composable
fun PasswordTextField(placeholder: String
                      , painter: Painter
                      , onTextChange: (String) -> Unit
                      , modifier: Modifier) {

    var text by remember {
        mutableStateOf("")
    }

    var visibleState by remember {
        mutableStateOf(false)
    }

    val iconState = if (visibleState) {
        painterResource(id = R.drawable.visibility_off24px)
    } else {
        painterResource(id = R.drawable.visibility_on24px)
    }

    var isError by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    TextField(value = text
        , onValueChange = {
            job?.cancel()
            text = it
            onTextChange(it)
            job = coroutineScope.launch {
                delay(600)
                isError = AuthValidator.passwordValidator(it)
            }
        }
        , singleLine = true
        , placeholder = { Text(text = placeholder) }
        , shape = RoundedCornerShape(50)
        , colors = TextFieldDefaults.textFieldColors(unfocusedIndicatorColor = Color.Transparent
            , focusedIndicatorColor = Color.Transparent
            , disabledIndicatorColor = Color.Transparent
            , errorIndicatorColor = Color.Transparent)
        , leadingIcon = {
            Icon(painter = painter
                , contentDescription = "")
        }
        , trailingIcon = {
            IconButton(onClick = { visibleState = !visibleState }) {
                Icon(painter = iconState
                    , contentDescription = "")
            }
        }
        , isError = isError
        , supportingText = {
            if (isError) {
                Text(text = "6 karakterden uzun olmalıdır"
                    , fontSize = 16.sp)
            }
        }
        , visualTransformation = if (visibleState) VisualTransformation.None else PasswordVisualTransformation()
        , modifier = modifier
            .fillMaxWidth())
}

@Composable
fun AuthButton(text : String
               , isEnabled : (Boolean)
               , onClick : () -> Unit
               , modifier: Modifier) {

    Button(onClick = onClick
        , enabled = isEnabled
        , modifier = modifier
            .fillMaxWidth()) {
        Text(text = text
            , fontSize = 24.sp
            , fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Register_to_login(navController: NavController) {

    val text1 = stringResource(id = R.string.register_to_login_1)
    val text2 = stringResource(id = R.string.register_to_login_2)

    val annotatedString = buildAnnotatedString {
        append("$text1 ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
            pushStringAnnotation(tag = text2
                , annotation = text2)
            append(text2)
        }
    }

    ClickableText(text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    if (span.item == text2) {
                        navController.navigate(Routes.LoginScreen.route)
                    }
                }
        }
        , style = TextStyle(fontSize = 18.sp
            , textAlign = TextAlign.Center)
        , modifier = Modifier
            .fillMaxWidth())
}

@Composable
fun Login_to_register(navController: NavController) {

    val text1 = stringResource(id = R.string.login_to_register_1)
    val text2 = stringResource(id = R.string.login_to_register_2)

    val annotatedString = buildAnnotatedString {
        append("$text1 ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
            pushStringAnnotation(tag = text2
                , annotation = text2)
            append(text2)
        }
    }

    ClickableText(text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    if (span.item == text2) {
                        navController.navigate(Routes.RegisterScreen.route)
                    }
                }
        }
        , style = TextStyle(fontSize = 18.sp
            , textAlign = TextAlign.Center)
        , modifier = Modifier
            .fillMaxWidth())
}