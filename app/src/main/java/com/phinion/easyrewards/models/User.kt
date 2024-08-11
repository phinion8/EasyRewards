package com.phinion.easyrewards.models

data class User(
    val name : String = "",
    val email : String = "",
    val password : String = "",
    val coins : Int = -1,
    val isRedeemed: Boolean = false
)
