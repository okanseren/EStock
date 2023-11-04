package com.oseren.estock.screen.auth.register

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
import com.oseren.estock.screen.auth.EmailTextField
import com.oseren.estock.screen.auth.InfoTextField
import com.oseren.estock.screen.auth.PasswordTextField
import com.oseren.estock.screen.auth.Register_to_login
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel = hiltViewModel()
                   , navController: NavController) {

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = BringIntoViewRequester()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        registerViewModel.eventFlow.collectLatest { event ->
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

        InfoTextField(placeholder = stringResource(id = R.string.register_firstName)
            , painter = painterResource(id = R.drawable.person24px)
            , onTextChange = registerViewModel::onFirstNameChange
            , modifier = Modifier
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch(Dispatchers.IO) {
                            delay(420)
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                })

        Spacer(modifier = Modifier.height(25.dp))

        InfoTextField(placeholder = stringResource(id = R.string.register_lastName)
            , painter = painterResource(id = R.drawable.person24px)
            , onTextChange = registerViewModel::onLastNameChange
            , modifier = Modifier
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch(Dispatchers.IO) {
                            delay(420)
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                })

        Spacer(modifier = Modifier.height(25.dp))

        EmailTextField(placeholder = stringResource(id = R.string.register_email)
            , painter = painterResource(id = R.drawable.mail24px)
            , onTextChange = registerViewModel::onEmailChange
            , modifier = Modifier
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch(Dispatchers.IO) {
                            delay(420)
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                })

        Spacer(modifier = Modifier.height(25.dp))

        PasswordTextField(placeholder = stringResource(id = R.string.register_password)
            , painter = painterResource(id = R.drawable.lock24px)
            , onTextChange = registerViewModel::onPasswordChange
            , modifier = Modifier
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch(Dispatchers.IO) {
                            delay(420)
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                })

        Spacer(modifier = Modifier.height(25.dp))

        AuthButton(text = stringResource(id = R.string.register_button)
            , isEnabled = registerViewModel.registerAllPassed.value
            , onClick = { registerViewModel.registerUser() }
            , modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequester))

        Spacer(modifier = Modifier.height(15.dp))

        Register_to_login(navController = navController)
    }

}