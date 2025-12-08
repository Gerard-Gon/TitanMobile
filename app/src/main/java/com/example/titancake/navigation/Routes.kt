package com.example.titancake.navigation

object Routes {
    const val HOME = "home"
    const val HOMEADMIN = "homeAdmin"
    const val PROFILE = "profile"
    const val PROFILEADMIN = "profileAdmin"
    const val SHOPPINGCART = "shoppingcart"
    const val ADMIN = "admin"
    const val DETAIL = "detail/{itemId}"

    const val DETAILADMIN = "detailAdmin/{itemId}"

    const val SALES_ADMIN = "salesAdmin"

    const val HISTORY = "history"

    const val HISTORY_DETAIL = "history_detail/{carritoId}"

    fun detailRoute(id: Int) = "detail/$id"

    fun detailAdminRoute(id: Int) = "detailAdmin/$id"
    fun historyDetailRoute(id: Int) = "history_detail/$id"


}

