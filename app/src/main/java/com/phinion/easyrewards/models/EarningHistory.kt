package com.phinion.easyrewards.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class EarningHistory(
    val earningMethod: String = "",
    val amountEarned: Int = -1,
    @ServerTimestamp
    val date: Date = Date()
)
