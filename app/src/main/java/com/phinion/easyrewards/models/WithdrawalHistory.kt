package com.phinion.easyrewards.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class WithdrawalHistory(

    val status:String = "Pending",
    val title: String = "",
    val amountOfCoin: Int = -1,
    @ServerTimestamp
    val time: Date = Date()

)