package com.oseren.estock.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.oseren.estock.domain.model.Category
import com.oseren.estock.domain.model.Resource
import com.oseren.estock.domain.model.ShoppingCart
import com.oseren.estock.domain.model.StockData
import com.oseren.estock.domain.model.TotalPrice
import com.oseren.estock.domain.model.UserData
import com.oseren.estock.domain.repository.StockRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore
    , firebaseAuth: FirebaseAuth): StockRepository {

    private val currentUser = firebaseAuth.currentUser

    override fun readStockData(): Flow<Resource<List<StockData>>> = callbackFlow {
        val docRef = firestore.collection("stock")

        trySend(Resource.Loading)
        //
        docRef.get()
            .addOnCompleteListener {
                val data = it.result.toObjects<StockData>()

                trySend(Resource.Success(data = data)).isSuccess

            }
            .addOnFailureListener {
                trySend(Resource.Failure(it.cause!!))
            }
        awaitClose {
            close()
        }

    }

    override fun readCategoryData(): Flow<Resource<List<Category>>> = callbackFlow {
        val docRef = firestore.collection("category")

        trySend(Resource.Loading)


        docRef.get()
            .addOnCompleteListener {
                val data = it.result.toObjects<Category>()

                trySend(Resource.Success(data = data)).isSuccess

            }
            .addOnFailureListener {
                trySend(Resource.Failure(it.cause!!))
            }
        awaitClose {
            close()
        }

    }

    override fun queryProduct(category: String): Flow<Resource<List<StockData>>> = callbackFlow {

        val docRef = firestore.collection("stock")
            .whereEqualTo("category", category)

        trySend(Resource.Loading)
        delay(200)

        docRef.get()
            .addOnCompleteListener {
                val stock = mutableListOf<StockData>()
                it.result.documents.forEach { doc ->
                    val data = doc.toObject<StockData>()
                    if (data != null) {
                        data.ref = doc.reference
                        stock.add(data)
                    }
                }

                trySend(Resource.Success(data = stock)).isSuccess
            }
            .addOnFailureListener {
                trySend(Resource.Failure(it.cause!!))
            }
        awaitClose {
            close()
        }
    }


    override fun querySearch(str: String): Flow<Resource<List<StockData>>> = callbackFlow {
        val docRef = firestore.collection("stock")
            .orderBy("name")
            .startAt(str)
            .endAt(str + "\uf8ff")

        trySend(Resource.Loading)
        docRef.get()
            .addOnCompleteListener {
                if (str.isBlank()) {
                    trySend(Resource.Success(data = emptyList())).isSuccess
                } else {
                    val data = it.result.toObjects<StockData>()

                    trySend(Resource.Success(data = data)).isSuccess
                }

            }
            .addOnFailureListener {
                trySend(Resource.Failure(it.cause!!))
            }
        awaitClose {
            close()
        }
    }

    override suspend fun setShoppingCartData(count: Int, ref: DocumentReference): Flow<Boolean> = callbackFlow {

        trySend(true)
        val aCount = if (count <= 0) 1 else 1

        val data = hashMapOf("count" to aCount
            , "ref" to ref)

        if (currentUser != null) {
            // users/userID/shoppingCart
            val shoppingCartRef = firestore
                .collection("users")
                .document(currentUser.uid)
                .collection("shoppingCart")

            // users/userID/shoppingCart/documentID/ ref datasını kontrol ediyor
            val querySnapshot = shoppingCartRef
                .whereEqualTo("ref", ref)
                .get()
                .await()

            // Boş ise yeni bir belge ekliyor
            if (querySnapshot.isEmpty) {

                shoppingCartRef
                    .add(data)
                    .await()
                trySend(false)

            } else {

                val firstDoc = querySnapshot.documents[0]
                val newCount = firstDoc.getLong("count")!! + count

                // Count değeri 0 veya daha küçükse belgeyi sil
                if (newCount <= 0) {
                    shoppingCartRef
                        .document(firstDoc.id)
                        .delete()
                        .await()
                    trySend(false)
                } else {
                    shoppingCartRef
                        .document(firstDoc.id)
                        .update("count", newCount)
                        .await()
                    trySend(false)
                }

            }
        }
        awaitClose {
            close()
        }
    }

    override suspend fun getShoppingCart(): Flow<Resource<List<ShoppingCart>>> = callbackFlow {

        if (currentUser != null) {
            val docRef = firestore
                .collection("users")
                .document(currentUser.uid)
                .collection("shoppingCart")

            trySend(Resource.Loading)

            val listener = docRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(Resource.Failure(e))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val newShoppingCart = mutableListOf<ShoppingCart>()
                    val totalPrice = TotalPrice()

                    snapshot.documents.forEach { documentSnapshot ->
                        documentSnapshot.getDocumentReference("ref")?.get()?.addOnSuccessListener {

                            val shoppingCart = ShoppingCart(count = documentSnapshot["count"].toString().toInt()
                                , stock = StockData(name = it["name"].toString()
                                    , price = it["price"].toString().toDouble()
                                    , photo = it["photo"].toString()
                                    , units = it["units"].toString()
                                    , unit_value = it["unit_value"].toString().toInt())
                                , docID = documentSnapshot.reference.id
                                , tp = totalPrice)

                            totalPrice.totalPrice += (shoppingCart.count * shoppingCart.stock.price)

                            newShoppingCart.add(shoppingCart)

                            if (newShoppingCart.size == snapshot.documents.size) {
                                trySend(Resource.Success(newShoppingCart))
                            }

                        }?.addOnFailureListener { exception ->
                            trySend(Resource.Failure(exception))
                        }
                    }
                }
            }
            awaitClose {
                listener.remove()
                close()
            }
        }

    }


    override suspend fun updateShoppingCart(docID: String, count: Int) {

        if (currentUser != null) {
            firestore
                .collection("users")
                .document(currentUser.uid)
                .collection("shoppingCart")
                .document(docID)
                .update("count",FieldValue.increment(count.toLong()))
                .await()
        }
    }

    override suspend fun deleteShoppingCart(docID: String) {

        if (currentUser != null) {
            firestore
                .collection("users")
                .document(currentUser.uid)
                .collection("shoppingCart")
                .document(docID)
                .delete()
                .await()
        }
    }

    override fun getUserData(): Flow<Resource<UserData>> = callbackFlow {

        if (currentUser != null) {

            trySend(Resource.Loading)

            val docRef = firestore
                .collection("users")
                .document(currentUser.uid)
                .get()

            docRef
                .addOnCompleteListener {
                val data = it.result.toObject<UserData>()
                trySend(Resource.Success(data!!)).isSuccess
            }
                .addOnFailureListener {
                    trySend(Resource.Failure(it))
                }
        }
        awaitClose {
            close()
        }
    }

}
