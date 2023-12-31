package com.oseren.estock.screen.category

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.oseren.estock.domain.model.Resource
import com.oseren.estock.domain.model.StockData
import com.oseren.estock.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val stockRepository: StockRepository): ViewModel() {

    private var _categoryData : MutableState<CategoryData> = mutableStateOf(CategoryData())
    val categoryData : State<CategoryData> = _categoryData

    private val isLoadingMap = mutableMapOf<DocumentReference, MutableState<Boolean>>()

    fun getCategoryInside(category : String) {
        viewModelScope.launch {
            stockRepository.queryProduct(category).collect {
                when(it) {
                    is Resource.Failure -> {
                        _categoryData.value = CategoryData(error = it.msg.toString())
                    }
                    Resource.Loading -> {
                        _categoryData.value = CategoryData(isLoading = true)
                    }
                    is Resource.Success -> {
                        _categoryData.value = CategoryData(data = it.data)
                        delay(1000)
                        _categoryData.value = _categoryData.value.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun isLoadingForItem(ref: DocumentReference): MutableState<Boolean> {
        return isLoadingMap.getOrPut(ref) { mutableStateOf(false) }
    }

    private fun setShoppingCartData(count : Int, ref : DocumentReference) {
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.setShoppingCartData(count, ref).collect {
                isLoadingForItem(ref).value = it
            }
        }
    }

    fun addToCart(ref: DocumentReference) {
        setShoppingCartData(1, ref)
    }

    fun removeFromCart(ref: DocumentReference) {
        setShoppingCartData(-1, ref)
    }
}
data class CategoryData(val data : List<StockData> = emptyList()
                        , val error : String = ""
                        , val isLoading : Boolean = true)