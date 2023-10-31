package com.oseren.estock.screen.auth

import android.util.Patterns

object AuthValidator {


//    fun String.isValidEmail() =
//        !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun usernameValidator(username : String): Boolean {
        return username.isNotEmpty() && username.length < 3
    }

    fun emailValidator(email: String): Boolean {
        return email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun passwordValidator(password: String): Boolean {
        return password.isNotEmpty() && password.length < 6
    }

}
