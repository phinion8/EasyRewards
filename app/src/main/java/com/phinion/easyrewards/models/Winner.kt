package com.phinion.easyrewards.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Winner(
    val name: String = "",
    val email: String = "",
    val id: String = "",
    @ServerTimestamp
    val date: Date = Date()
)
