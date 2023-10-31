package com.oseren.estock.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.oseren.estock.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser: FirebaseUser?

    fun registerUser(email: String ,password: String ,firstName: String ,lastName: String) : Flow<Resource<String>>

    fun loginUser(email: String , password: String) : Flow<Resource<String>>

    fun logoutUser() : Flow<Resource<String>>
}