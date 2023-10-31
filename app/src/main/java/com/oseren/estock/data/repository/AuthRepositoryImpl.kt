package com.oseren.estock.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.oseren.estock.domain.model.Resource
import com.oseren.estock.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth
    , private val firestore: FirebaseFirestore) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override fun registerUser(email: String, password: String,firstName: String ,lastName: String): Flow<Resource<String>> = callbackFlow {

        trySend(Resource.Loading)

        firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Resource.Success("registration completed"))
                    Log.d("AuthRepositoryImpl","${task.isSuccessful}")

                    val userID = task.result.user!!.uid

                    val data = hashMapOf("firstName" to firstName
                        , "lastName" to lastName
                        , "email" to email
                        , "userID" to userID)

                    firestore
                        .collection("users")
                        .document(userID)
                        .set(data)
                        .addOnCompleteListener {
                            Log.d("AuthRepositoryImpl","${it.isSuccessful}")
                        }
                        .addOnFailureListener {
                            Log.d("AuthRepositoryImpl","${it.message}")
                        }
                }
            }
            .addOnFailureListener { exception ->
                trySend(Resource.Failure(exception))
            }
        awaitClose {
            close()
        }
    }

    override fun loginUser(email: String, password: String): Flow<Resource<String>> = callbackFlow {

        trySend(Resource.Loading)

        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Resource.Success("login completed"))
                }
            }
            .addOnFailureListener { exception ->
                trySend(Resource.Failure(exception))
            }
        awaitClose {
            close()
        }
    }

    override fun logoutUser(): Flow<Resource<String>> = callbackFlow {

        trySend(Resource.Loading)

        try {
            firebaseAuth.signOut()
            trySend(Resource.Success("exit completed"))

        } catch (e : Exception) {
            trySend(Resource.Failure(e))
        }
        awaitClose {
            close()
        }

    }
}