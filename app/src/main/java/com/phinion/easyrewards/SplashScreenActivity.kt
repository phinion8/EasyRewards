package com.phinion.easyrewards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

class SplashScreenActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        firebaseAuth = Firebase.auth
        database = Firebase.firestore




        FirebaseMessaging.getInstance().subscribeToTopic("notification")

        var currentTime = Date()




        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = firebaseAuth.currentUser
            if (currentUser!=null){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("currentTime", currentTime)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 1500)
    }
}