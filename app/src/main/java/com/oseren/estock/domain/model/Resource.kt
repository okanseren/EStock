package com.oseren.estock.domain.model

sealed class Resource<out T> {
    data class Success<out R>(val data : R) : Resource<R>()
    data class Failure(val msg:Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
