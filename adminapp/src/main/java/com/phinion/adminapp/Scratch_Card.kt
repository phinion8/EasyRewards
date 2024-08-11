package com.phinion.adminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.phinion.adminapp.databinding.ActivityScratchCardBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Scratch_Card : AppCompatActivity() {
    private lateinit var binding: ActivityScratchCardBinding
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScratchCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.firestore

        database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
            .collection("scratchCard").document("NR4YwjqCb6TzLGn64MZZ")
            .addSnapshotListener { value, error ->
            val amountOfCoins = value?.get("randomRewardUpto").toString()
            binding.amountOfCoins.setText(amountOfCoins)
        }


        binding.updateBtn.setOnClickListener {
            val amountOfCoins = binding.amountOfCoins.text.toString().toLong()
            database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
                .collection("scratchCard").document("NR4YwjqCb6TzLGn64MZZ")
                .update("randomRewardUpto" , amountOfCoins).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }
        }
    }
}