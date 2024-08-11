package com.phinion.adminapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.phinion.adminapp.databinding.ActivityDailyRewardsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DailyRewards : AppCompatActivity() {
    private lateinit var binding: ActivityDailyRewardsBinding
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyRewardsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.firestore


        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("dailyReward")
            .document("kpiuZAtNcIfI6uSGi79P")
            .addSnapshotListener{value, error->
                 val amountOfCoins = value?.get("rewardAmount").toString()
                 binding.amountOfCoins.setText(amountOfCoins)
            }

        binding.updateBtn.setOnClickListener {
            val amountOfCoins = binding.amountOfCoins.text.toString().toLong()
            if(amountOfCoins.toString().isNotEmpty()){
                database.collection("earningSystems")
                    .document("4QkPp047g45DeHGLGIVt")
                    .collection("dailyReward")
                    .document("kpiuZAtNcIfI6uSGi79P")
                    .update("rewardAmount", amountOfCoins)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                    }
            }

        }


    }
}