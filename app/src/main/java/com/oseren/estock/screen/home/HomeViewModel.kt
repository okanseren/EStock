package com.oseren.estock.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestoreException
import com.oseren.estock.domain.model.Category
import com.oseren.estock.domain.model.Resource
import com.oseren.estock.domain.model.UserData
import com.oseren.estock.domain.repository.AuthRepository
import com.oseren.estock.domain.repository.StockRepository
import com.oseren.estock.navigation.route.AUTHENTICATION_ROUTE
import com.oseren.estock.screen.auth.AuthUIEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val stockRepository: StockRepository
    , private val authRepository: AuthRepository) : ViewModel() {

    private val _userState = MutableStateFlow(authRepository.currentUser)
    val userState: StateFlow<FirebaseUser?> = _userState.asStateFlow()

    init {
        viewModelScope.launch {
            _userState.emit(authRepository.currentUser)
        }
    }


    private var _userData : MutableState<UserData> = mutableStateOf(UserData())
    val userData : State<UserData> = _userData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.getUserData().collect {
                when (it) {
                    is Resource.Failure -> { }
                    Resource.Loading -> { }
                    is Resource.Success -> {
                        _userData.value = it.data
                    }
                }
            }
        }
    }

    private var _category : MutableState<ItemState> = mutableStateOf(ItemState())
    val category : State<ItemState> = _category

    init {
        try {
            if (_userState.value != null) {
                viewModelScope.launch {
                    stockRepository.readCategoryData().collect {
                        when (it) {
                            is Resource.Failure -> {
                                _category.value = ItemState(error = it.msg.toString())
                            }
                            Resource.Loading -> { }
                            is Resource.Success -> {
                                _category.value = _category.value.copy(data = it.data)
                                delay(1000)
                                _category.value = _category.value.copy(isLoading = false)
                            }
                        }
                    }
                }
            }
        } catch (e: FirebaseFirestoreException) {
                e.cause

        }
    }

    private val  _eventFlow = MutableSharedFlow<AuthUIEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun logoutUser() {
        viewModelScope.launch {
            authRepository.logoutUser().collect {
                when(it) {
                    is Resource.Failure -> { }
                    Resource.Loading -> { }
                    is Resource.Success -> {
                        _eventFlow.emit(AuthUIEvents.NavigateEvent(AUTHENTICATION_ROUTE))
                    }
                }
            }
        }
    }
}

data class ItemState(val data : List<Category> = emptyList()
                     , val error : String = ""
                     , val isLoading : Boolean = true)