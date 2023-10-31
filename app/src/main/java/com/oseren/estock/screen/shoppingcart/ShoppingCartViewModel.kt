package com.oseren.estock.screen.shoppingcart

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.oseren.estock.domain.model.Resource
import com.oseren.estock.domain.model.ShoppingCart
import com.oseren.estock.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(private val stockRepository: StockRepository) : ViewModel() {

    private var _shoppingCartData : MutableStateFlow<Shopping> = MutableStateFlow(Shopping())
    val shoppingCartData : StateFlow<Shopping> = _shoppingCartData.asStateFlow()

    private var _price : MutableStateFlow<Double> = MutableStateFlow(0.0)
    val price : StateFlow<Double> = _price.asStateFlow()

    private val isLoadingMap = mutableMapOf<DocumentReference, MutableState<Boolean>>()

    fun isLoadingForItem(ref: DocumentReference): MutableState<Boolean> {
        return isLoadingMap.getOrPut(ref) { mutableStateOf(false) }
    }



    init {
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.getShoppingCart().collect {
                when (it) {
                    is Resource.Failure -> {
                        _shoppingCartData.value = Shopping(error = it.msg.toString())
                    }
                    Resource.Loading -> {
                        _shoppingCartData.value = Shopping(isLoading = true)
                    }
                    is Resource.Success -> {
                        _shoppingCartData.value = Shopping(data = it.data)
                        _shoppingCartData.value.data.forEach { each ->
                            _price.emit(each.tp.totalPrice)
                        }
                    }
                }
            }
        }
    }

    private fun setShoppingCartData(count : Int, ref : DocumentReference) {
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.setShoppingCartData(count, ref)
        }
    }

    suspend fun addToCart(ref: DocumentReference) {
        isLoadingForItem(ref).value = true
        setShoppingCartData(1, ref)
        delay(1000)
        isLoadingForItem(ref).value = false
    }

    suspend fun removeFromCart(ref: DocumentReference) {
        isLoadingForItem(ref).value = true
        setShoppingCartData(-1, ref)
        delay(1000)
        isLoadingForItem(ref).value = false
    }

    private fun updateShoppingCart(docID: String,count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.updateShoppingCart(docID,count)
        }
    }

    fun increaseShoppingCartData(docID: String) {
        updateShoppingCart(docID,1)
    }

    fun decreaseShoppingCartData(docID: String) {
        updateShoppingCart(docID,-1)
    }

    fun deleteShoppingCartData(docID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(100)
            stockRepository.deleteShoppingCart(docID)
        }
    }
}
data class Shopping(var data : List<ShoppingCart> = emptyList()
                    , val error : String = ""
                    , val isLoading : Boolean = false)


