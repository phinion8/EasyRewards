package com.phinion.easyrewards

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.phinion.easyrewards.databinding.ActivityContactBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth
        database = Firebase.firestore

        database.collection("contactEmail")
            .document("ZFBZRlUPSm7lQdPkEOGY")
            .addSnapshotListener{value, error->
                binding.emailText.text = value?.getString("email")

            }

        binding.backBtn.setOnClickListener{
            finish()
        }

        binding.button17.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.google.com"))
            startActivity(intent)
        }

    }
}