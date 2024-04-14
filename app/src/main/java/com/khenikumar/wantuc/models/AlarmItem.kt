package com.khenikumar.wantuc.models

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)