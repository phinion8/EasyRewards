package com.phinion.easyrewards.models

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)