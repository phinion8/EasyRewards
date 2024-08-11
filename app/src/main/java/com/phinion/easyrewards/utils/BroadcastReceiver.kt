package com.phinion.easyrewards.utils

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log


open class BroadcastReceiver: Service() {
    private val TAG = "BroadcastService"

    companion object{
        const val COUNTDOWN_BR = "your_package_name.countdown_br"
    }
    var bi = Intent(COUNTDOWN_BR)


    var cdt: CountDownTimer? = null

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Starting timer...")
        cdt = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000)
                bi.putExtra("countdown", millisUntilFinished)
                sendBroadcast(bi)
            }

            override fun onFinish() {
                Log.i(TAG, "Timer finished")
            }
        }
        (cdt as CountDownTimer).start()
    }

    override fun onDestroy() {

        Log.i(TAG, "Timer cancelled")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }



}