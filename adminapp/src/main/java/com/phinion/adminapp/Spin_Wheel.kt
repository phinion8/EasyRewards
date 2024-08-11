package com.phinion.adminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.phinion.adminapp.databinding.ActivitySpinWheelBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Spin_Wheel : AppCompatActivity() {
    private lateinit var binding: ActivitySpinWheelBinding
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpinWheelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.firestore

        database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
            .collection("spinTheWheel").document("vF5XdFOfIA7X0zOBiV1a")
            .addSnapshotListener { value, error ->
                val randomReward1 = value?.get("randomReward1").toString()
                binding.randomReward1.setText(randomReward1)

                val randomReward2 = value?.get("randomReward2").toString()
                binding.randomReward2.setText(randomReward2)

                val randomReward3 = value?.get("randomReward3").toString()
                binding.randomReward3.setText(randomReward3)

                val randomReward4 = value?.get("randomReward4").toString()
                binding.randomReward4.setText(randomReward4)

                val randomReward5 = value?.get("randomReward5").toString()
                binding.randomReward5.setText(randomReward5)

                val randomReward6 = value?.get("randomReward6").toString()
                binding.randomReward6.setText(randomReward6)

                val randomReward7 = value?.get("randomReward7").toString()
                binding.randomReward7.setText(randomReward7)

                val randomReward8 = value?.get("randomReward8").toString()
                binding.randomReward8.setText(randomReward8)
            }

        binding.updateBtn.setOnClickListener {
            val randomReward1 = binding.randomReward1.text.toString().toLong()
            val randomReward2 = binding.randomReward2.text.toString().toLong()
            val randomReward3 = binding.randomReward3.text.toString().toLong()
            val randomReward4 = binding.randomReward4.text.toString().toLong()
            val randomReward5 = binding.randomReward5.text.toString().toLong()
            val randomReward6 = binding.randomReward6.text.toString().toLong()
            val randomReward7 = binding.randomReward7.text.toString().toLong()
            val randomReward8 = binding.randomReward8.text.toString().toLong()
            database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
                .collection("spinTheWheel").document("vF5XdFOfIA7X0zOBiV1a")
                .update("randomReward1" , randomReward1).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }

            database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
                .collection("spinTheWheel").document("vF5XdFOfIA7X0zOBiV1a")
                .update("randomReward2" , randomReward2).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }

            database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
                .collection("spinTheWheel").document("vF5XdFOfIA7X0zOBiV1a")
                .update("randomReward3" , randomReward3).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }

            database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
                .collection("spinTheWheel").document("vF5XdFOfIA7X0zOBiV1a")
                .update("randomReward4" , randomReward4).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }

            database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
                .collection("spinTheWheel").document("vF5XdFOfIA7X0zOBiV1a")
                .update("randomReward5" , randomReward5).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }

            database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
                .collection("spinTheWheel").document("vF5XdFOfIA7X0zOBiV1a")
                .update("randomReward6" , randomReward6).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }

            database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
                .collection("spinTheWheel").document("vF5XdFOfIA7X0zOBiV1a")
                .update("randomReward7" , randomReward7).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }

            database.collection("earningSystems").document("4QkPp047g45DeHGLGIVt")
                .collection("spinTheWheel").document("vF5XdFOfIA7X0zOBiV1a")
                .update("randomReward8" , randomReward8).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }
        }
    }
}