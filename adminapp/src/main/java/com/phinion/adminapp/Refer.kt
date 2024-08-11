package com.phinion.adminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.phinion.adminapp.databinding.ActivityReferBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Refer : AppCompatActivity() {
    lateinit var binding: ActivityReferBinding
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.firestore

        database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
            .collection("referReward").document("JnZpNNahVp3ATEvhX1WN")
            .addSnapshotListener { value, error ->
                val amountOfCoins = value?.get("referReward").toString()
                binding.amountOfCoins.setText(amountOfCoins)
            }

        binding.updateBtn.setOnClickListener {
            val amount_of_coins = binding.amountOfCoins.text.toString().toLong()
            database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
                .collection("referReward").document("JnZpNNahVp3ATEvhX1WN")
                .update("referReward", amount_of_coins).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }

        }
    }
}