package com.oseren.estock.screen.auth.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oseren.estock.domain.model.Resource
import com.oseren.estock.domain.repository.AuthRepository
import com.oseren.estock.navigation.route.Routes
import com.oseren.estock.screen.auth.AuthUIEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private var loginData = mutableStateOf(LoginData())

    private val  _eventFlow = MutableSharedFlow<AuthUIEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    var loginAllPassed = mutableStateOf(false)

    fun onEmailChange(email: String) {
        loginData.value = loginData.value.copy(email = email)
        Log.d("Deneme","email: ${loginData.value.email}")
        buttonValidate()
    }

    fun onPasswordChange(password: String) {
        loginData.value = loginData.value.copy(password = password)
        Log.d("Deneme","password: ${loginData.value.password}")
        buttonValidate()
    }

    private fun buttonValidate() {
        loginAllPassed.value = (loginData.value.email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(loginData.value.email).matches())
                && (loginData.value.password.isNotEmpty() && loginData.value.password.length >= 6)
    }

    fun loginUser() {

        val email = loginData.value.email
        val password = loginData.value.password

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.loginUser(email, password).collect {
                when(it) {
                    is Resource.Failure -> {
                        _eventFlow.emit(AuthUIEvents.SnackbarEvent(it.msg.toString()))
                    }
                    Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        _eventFlow.emit(AuthUIEvents.NavigateEvent(Routes.HomeScreen.route))
                    }
                }
            }
        }
    }
}

data class LoginData(val email: String = ""
                      , val password: String = "")

