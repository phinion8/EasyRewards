package com.phinion.easyrewards.utils

import com.phinion.easyrewards.models.AlarmItem

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}