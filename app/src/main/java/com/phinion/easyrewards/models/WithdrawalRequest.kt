package com.phinion.easyrewards.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class WithdrawalRequest(
    val userId: String = "",
    val userEmail: String = "",
    val coinAmount: Int = -1,
    val platform: String = "",
    val gameId: String = "",
    val country: String = "",
    @ServerTimestamp
    val date: Date = Date()
)
