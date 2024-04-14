package com.khenikumar.wantuc.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
import java.util.Locale

data class User(
    val name : String = "",
    val email : String = "",
    val password : String = "",
    val coins : Int = -1,
    val isRedeemed: Boolean = false
)
