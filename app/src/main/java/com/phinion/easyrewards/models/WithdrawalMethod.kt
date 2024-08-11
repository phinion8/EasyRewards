package com.phinion.easyrewards.models

data class WithdrawalMethod(
    var id: String = "",
    val platform: String = "",
    val amountOfCoins: Int = -1,
    val coinValue: String = ""
)
