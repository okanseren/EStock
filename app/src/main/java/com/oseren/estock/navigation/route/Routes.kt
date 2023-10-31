package com.oseren.estock.navigation.route

const val HOME_ROUTE = "HOME_ROUTE"
const val SHOPPING_CART_ROUTE = "SHOPPING_CART_ROUTE"
const val AUTHENTICATION_ROUTE = "AUTHENTICATION_ROUTE"

sealed class Routes(val route: String) {

    object HomeScreen: Routes(route = "homeScreen")

    object InCategoryScreen: Routes(route = "inCategoryScreen/{category}")

    object SearchScreen: Routes(route = "searchScreen")

    object ShoppingCartScreen: Routes(route = "shoppingCartScreen")

    object RegisterScreen: Routes(route = "registerScreen")

    object LoginScreen: Routes(route = "loginScreen")

}
