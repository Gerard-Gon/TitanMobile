package com.example.titancake.navigation

object Routes {

    const val HOME = "home"
    const val PROFILE = "profile"
    const val DETAIL = "detail/{itemId}"

    const val REGISTER = "register"

    const val SHOPPINGCART = "shoppingCart"

    const val ADMIN = "Admin"

    const val SPLASH = "splashScreen"
    fun detailRoute(itemId: Int) = "detail/$itemId"

}

