package com.phinion.easyrewards

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.phinion.easyrewards.databinding.ActivityReferBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReferActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReferBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private var referCode: String = ""
    private lateinit var referReward: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth
        database = Firebase.firestore

        mAuth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .addSnapshotListener { value, error ->

                    referCode = it.uid
                    binding.referCode.text = it.uid

                }
        }
        binding.backBtn.setOnClickListener {
            finish()
        }



        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("referReward")
            .document("JnZpNNahVp3ATEvhX1WN")
            .addSnapshotListener { value, error ->
                if (value != null) {
                    referReward = value.get("referReward").toString()
                    binding.referDescription.text =
                        this.getString(R.string.you_will_earn_upto) + " " + referReward + " " + this.getString(
                            R.string.refer_description_extra
                        )
                }

            }

        binding.shareButton.setOnClickListener {

            val shareText =
                "Want some uc and diamonds in pubg/bgmi or free fire for free" + "\n" + this.getString(
                    R.string.share_text_2
                ) +
                        " " + this.getString(R.string.app_name) + " " + this.getString(R.string.share_text_3) + "\n" +
                        "" + this.getString(R.string.share_text_4) + " " + referCode + " " + this.getString(
                    R.string.share_text_5
                ) +
                        " " + referReward + " Coins" + "\n" + this.getString(R.string.share_text_6) + " " + this.getString(
                    R.string.app_name
                ) +
                        "" + " " + this.getString(R.string.share_text_7) + "\n" + "https://play.google.com/store/apps/details?id=" +
                        "" + applicationContext.packageName


            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share Through"))
        }


    }
}