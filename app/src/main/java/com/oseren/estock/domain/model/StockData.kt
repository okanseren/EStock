package com.oseren.estock.domain.model

import com.google.firebase.firestore.DocumentReference


data class UserData(val firstName: String = ""
                    , val lastName: String = "")

data class StockData(val name: String = ""
    , val category: String = ""
    , val price: Double = 0.0
    , val units: String = ""
    , val unit_value: Int = 0
    , val photo: String = ""
    , var ref: DocumentReference? = null)

data class Category(val category : String = ""
                    , val photo: String = "")

data class ShoppingCart(val count : Int = 0
                        , val stock : StockData = StockData()
                        , val docID : String = ""
                        , val tp : TotalPrice
)

data class TotalPrice(var totalPrice : Double = 0.0)