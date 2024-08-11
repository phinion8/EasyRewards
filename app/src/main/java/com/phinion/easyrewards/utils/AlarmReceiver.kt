package com.phinion.easyrewards.utils

import android.content.Context
import android.content.Intent

class AlarmReceiver: android.content.BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE")?:return
        println("Alarm triggered: $message")
    }
}