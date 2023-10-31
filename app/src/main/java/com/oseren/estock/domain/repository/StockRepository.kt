package com.oseren.estock.domain.repository

import com.google.firebase.firestore.DocumentReference
import com.oseren.estock.domain.model.Category
import com.oseren.estock.domain.model.Resource
import com.oseren.estock.domain.model.ShoppingCart
import com.oseren.estock.domain.model.StockData
import com.oseren.estock.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun readStockData() : Flow<Resource<List<StockData>>>
    //fun realReadStockData() : Flow<Resource<List<Stock>>>
    fun readCategoryData() : Flow<Resource<List<Category>>>
    //fun realReadCategoryData() : Flow<Resource<List<Category>>>
    fun queryProduct(category : String) : Flow<Resource<List<StockData>>>

    fun querySearch(str : String) : Flow<Resource<List<StockData>>>

    suspend fun setShoppingCartData(count: Int, ref: DocumentReference): Flow<Boolean>

    suspend fun getShoppingCart() : Flow<Resource<List<ShoppingCart>>>

    suspend fun updateShoppingCart(docID: String, count: Int)

    suspend fun deleteShoppingCart(docID: String)

    fun getUserData(): Flow<Resource<UserData>>
}