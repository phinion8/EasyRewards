package com.phinion.adminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.phinion.adminapp.databinding.ActivityLuckyNumberBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LuckyNumber : AppCompatActivity() {
    private lateinit var binding: ActivityLuckyNumberBinding
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLuckyNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.firestore

        database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
            .collection("luckyNumber").document("IXvc2IY3FjvAh18fHjUY")
            .addSnapshotListener { value, error ->
                val amountOfCoins = value?.get("rewardAmount").toString()
                binding.amountOfCoins.setText(amountOfCoins)
            }


        binding.updateBtn.setOnClickListener {
            val amountOfCoins = binding.amountOfCoins.text.toString().toLong()
            database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
                .collection("luckyNumber").document("IXvc2IY3FjvAh18fHjUY")
                .update("rewardAmount" , amountOfCoins).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }
        }
    }
}