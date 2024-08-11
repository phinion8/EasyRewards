package com.phinion.adminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.phinion.adminapp.databinding.ActivityWithdrawalUpdateBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WithdrawalUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWithdrawalUpdateBinding
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawalUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.firestore

        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .addSnapshotListener{value, error->

                binding.withdrawalMethod.setText(value?.getString("withdrawalMethod"))
                binding.withdrawalCoinValue.setText(value?.getString("withdrawalCoinValue"))
                binding.withdrawalValue.setText(value?.getString("withdrawalValue"))
                binding.withdrawalLimit.setText(value?.get("withdrawalLimit").toString())

            }

        binding.updateBtn.setOnClickListener {
            database.collection("earningSystems")
                .document("4QkPp047g45DeHGLGIVt")
                .update("withdrawalMethod", binding.withdrawalMethod.text.toString()).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }

            database.collection("earningSystems")
                .document("4QkPp047g45DeHGLGIVt")
                .update("withdrawalCoinValue", binding.withdrawalCoinValue.text.toString()).addOnSuccessListener {
                Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
            }

            database.collection("earningSystems")
                .document("4QkPp047g45DeHGLGIVt")
                .update("withdrawalValue", binding.withdrawalValue.text.toString()).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }

            database.collection("earningSystems")
                .document("4QkPp047g45DeHGLGIVt")
                .update("withdrawalLimit", binding.withdrawalLimit.text.toString().toLong()).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }
        }
    }
}