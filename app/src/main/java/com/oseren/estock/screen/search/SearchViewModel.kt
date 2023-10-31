package com.oseren.estock.screen.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oseren.estock.domain.model.Resource
import com.oseren.estock.domain.model.StockData
import com.oseren.estock.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val stockRepository: StockRepository) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var _searchData: MutableState<Search> = mutableStateOf(Search())
    val searchData : State<Search> = _searchData

    private var searchJob: Job? = null

    fun search(str : String) {
        searchJob?.cancel()

        _searchText.value = str

        searchJob =  viewModelScope.launch {
            delay(600)
            stockRepository.querySearch(str = _searchText.value).collect {
                when (it) {
                    is Resource.Failure -> {
                        _searchData.value = Search(error = it.msg.toString())
                    }

                    Resource.Loading -> {
                        _searchData.value = Search(isLoading = true)
                    }

                    is Resource.Success -> {
                        _searchData.value = Search(data = it.data)
                    }
                }
            }
        }
    }
}

data class Search(val data : List<StockData> = emptyList()
                  , val error : String = ""
                  , val isLoading : Boolean = false)