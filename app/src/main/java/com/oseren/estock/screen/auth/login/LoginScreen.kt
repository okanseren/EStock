package com.oseren.estock.screen.auth.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.oseren.estock.R
import com.oseren.estock.navigation.route.AUTHENTICATION_ROUTE
import com.oseren.estock.navigation.route.HOME_ROUTE
import com.oseren.estock.screen.auth.AuthButton
import com.oseren.estock.screen.auth.AuthUIEvents
import com.oseren.estock.screen.auth.InfoTextField
import com.oseren.estock.screen.auth.Login_to_register
import com.oseren.estock.screen.auth.PasswordTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = hiltViewModel()
                , navController: NavController) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        loginViewModel.eventFlow.collectLatest { event ->
            when (event) {

                is AuthUIEvents.NavigateEvent -> {
                    navController.navigate(HOME_ROUTE) {
                        popUpTo(AUTHENTICATION_ROUTE) {
                            inclusive = true
                        }
                    }
                }
                is AuthUIEvents.SnackbarEvent -> {
                    Toast.makeText(context,event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(end = 12.dp
            , start = 12.dp
            , top = 24.dp
            , bottom = 24.dp)) {

        InfoTextField(placeholder = stringResource(id = R.string.login_email)
            , painter = painterResource(id = R.drawable.mail24px)
            , onTextChange = loginViewModel::onEmailChange
            , modifier = Modifier)

        Spacer(modifier = Modifier.height(25.dp))

        PasswordTextField(placeholder = stringResource(id = R.string.login_password)
            , painter = painterResource(id = R.drawable.lock24px)
            , onTextChange = loginViewModel::onPasswordChange
            , modifier = Modifier)

        Spacer(modifier = Modifier.height(25.dp))

        AuthButton(text = stringResource(id = R.string.login_button)
            , isEnabled = loginViewModel.loginAllPassed.value
            , onClick = { loginViewModel.loginUser() }
            , modifier = Modifier)

        Spacer(modifier = Modifier.height(15.dp))

        Login_to_register(navController = navController)

    }


}