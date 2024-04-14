package com.khenikumar.wantuc.utils

import com.khenikumar.wantuc.models.AlarmItem

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}