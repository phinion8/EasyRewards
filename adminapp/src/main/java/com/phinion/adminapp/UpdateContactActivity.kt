package com.phinion.adminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.phinion.adminapp.databinding.ActivityUpdateContactBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateContactBinding
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.firestore

        database.collection("contactEmail")
            .document("ZFBZRlUPSm7lQdPkEOGY")
            .addSnapshotListener{value, error->

                binding.email.setText(value?.getString("email"))

            }

        binding.updateBtn.setOnClickListener {
            database.collection("contactEmail")
                .document("ZFBZRlUPSm7lQdPkEOGY")
                .update("email", binding.email.text.toString()).addOnSuccessListener {
                    Toast.makeText(this, "Successfully updated...", Toast.LENGTH_SHORT).show()
                }

        }

    }
}