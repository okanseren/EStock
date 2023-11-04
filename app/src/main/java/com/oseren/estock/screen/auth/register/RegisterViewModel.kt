package com.oseren.estock.screen.auth.register

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
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private var registerData = mutableStateOf(RegisterData())

    private val  _eventFlow = MutableSharedFlow<AuthUIEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    var registerAllPassed = mutableStateOf(false)

    fun onFirstNameChange(firstName: String) {
        registerData.value = registerData.value.copy(firstName = firstName)
        buttonValidate()
    }

    fun onLastNameChange(lastName: String) {
        registerData.value = registerData.value.copy(lastName = lastName)
        buttonValidate()
    }

    fun onEmailChange(email: String) {

        registerData.value = registerData.value.copy(email = email)
        buttonValidate()
    }

    fun onPasswordChange(password: String) {
        registerData.value = registerData.value.copy(password = password)
        buttonValidate()
    }

    private fun buttonValidate() {
        registerAllPassed.value = (registerData.value.firstName.isNotEmpty() && registerData.value.firstName.length >= 3)
                && (registerData.value.lastName.isNotEmpty() && registerData.value.lastName.length >= 3)
                && (registerData.value.email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(registerData.value.email).matches())
                && (registerData.value.password.isNotEmpty() && registerData.value.password.length >= 6)
    }

    fun registerUser() {

        val firstName = registerData.value.firstName
        val lastName = registerData.value.lastName
        val email = registerData.value.email
        val password = registerData.value.password


        viewModelScope.launch(Dispatchers.IO) {
            authRepository.registerUser(email, password, firstName, lastName).collect {
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

//    fun events(signUpUIEvent: SignUpUIEvent) {
//        when(signUpUIEvent) {
//            is SignUpUIEvent.EmailChanged -> {
//                signUpData.value = signUpData.value.copy(email = signUpUIEvent.email)
//                debugState()
//            }
//            is SignUpUIEvent.FirstNameChanged -> {
//                signUpData.value = signUpData.value.copy(firstName = signUpUIEvent.firstName)
//                debugState()
//            }
//            is SignUpUIEvent.LastNameChanged -> {
//                signUpData.value = signUpData.value.copy(lastName = signUpUIEvent.lastName)
//                debugState()
//            }
//            is SignUpUIEvent.PasswordChanged -> {
//                signUpData.value = signUpData.value.copy(password = signUpUIEvent.password)
//                debugState()
//            }
//            is SignUpUIEvent.PrivacyPolicyCheckBoxClicked -> {
//                signUpData.value = signUpData.value.copy(privacyPolicy = signUpUIEvent.status)
//                debugState()
//            }
//            SignUpUIEvent.RegisterButtonClicked -> {
//                createUserInFirebase(email = signUpData.value.email
//                    , password = signUpData.value.password)
//            }
//        }
//        validateWithRules()
//    }
//    private fun validateWithRules() {
//        val firstNameResult = Validators.firstNameValidator(signUpData2.value.firstName)
//        val lastNameResult = Validators.lastNameValidator(signUpData2.value.lastName)
//        val emailResult = Validators.emailValidator(signUpData2.value.email)
//        val passwordResult = Validators.passwordValidator(signUpData2.value.password)
//        val checkboxResult = Validators.checkboxValidate(signUpData.value.privacyPolicy)
//
//        signUpData.value = signUpData.value.copy(firstNameError = firstNameResult.status
//            , lastNameError = lastNameResult.status
//            , emailError = emailResult.status
//            , passwordError = passwordResult.status
//            , privacyPolicyError = checkboxResult.status)
//
//        signUpAllPassed.value = firstNameResult.status
//                && lastNameResult.status
//                && emailResult.status
//                && passwordResult.status
//    }
//
//    private fun createUserInFirebase(email : String, password: String) {
//
//        //signUpInProgress.value = true
//
//        FirebaseAuth
//            .getInstance()
//            .createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener {
//                Log.d("SignUpViewModel", "Inside_OnCompleteListener")
//                Log.d("SignUpViewModel", " isSuccessful = ${it.isSuccessful}")
//
//                //signUpInProgress.value = false
//                if (it.isSuccessful) {
//                    ScreenRoute.changeRoute(route = Screen.HomeScreen)
//                }
//            }
//            .addOnFailureListener {
//                Log.d("SignUpViewModel", "Inside_OnFailureListener")
//                Log.d("SignUpViewModel", "Exception= ${it.message}")
//                Log.d("SignUpViewModel", "Exception= ${it.localizedMessage}")
//            }
//
//        /*viewModelScope.launch(Dispatchers.IO) {
//            repository.createUser(firstName = signUpData.value.firstName
//                , lastName = signUpData.value.lastName
//                , email = email
//                , password = password).collect {
//                when(it) {
//                    is Resource.Failure -> {
//
//                    }
//                    Resource.Loading -> {
//                        signUpProgress.value = true
//                    }
//                    is Resource.Success -> {
//                        ScreenRoute.changeRoute(route = Screen.HomeScreen)
//                    }
//                }
//            }
//        }
//
//         */
//    }
//
//    private fun debugState() {
//        Log.d("SignUpViewModel", "-------------------------------------------------")
//        Log.d("SignUpViewModel", "FirstName: ${signUpData.value.firstName}")
//        Log.d("SignUpViewModel", "LastName: ${signUpData.value.lastName}")
//        Log.d("SignUpViewModel", "Email: ${signUpData.value.email}")
//        Log.d("SignUpViewModel", "Password: ${signUpData.value.password}")
//        Log.d("SignUpViewModel", "Checkbox: ${signUpData.value.privacyPolicy}")
//    }
}

data class RegisterData(val firstName: String = ""
                       , val lastName: String = ""
                       , val email: String = ""
                       , val password: String = "")