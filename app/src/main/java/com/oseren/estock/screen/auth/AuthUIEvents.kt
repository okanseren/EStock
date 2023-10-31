package com.oseren.estock.screen.auth

sealed class AuthUIEvents {
    data class SnackbarEvent(val message : String): AuthUIEvents()
    data class NavigateEvent(val route: String): AuthUIEvents()
}
