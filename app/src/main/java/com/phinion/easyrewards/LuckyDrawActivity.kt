package com.phinion.easyrewards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.easyrewards.databinding.ActivityLuckyDrawBinding
import com.phinion.easyrewards.models.EarningHistory
import com.phinion.easyrewards.models.User
import com.phinion.easyrewards.models.Winner
import com.phinion.easyrewards.utils.Constants
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds

class LuckyDrawActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLuckyDrawBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var collectionReference: CollectionReference
    private lateinit var winner: Winner
    var isRedeemed: Boolean = false
    var coinCost: Int = 50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLuckyDrawBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth
        database = Firebase.firestore


        UnityAds.initialize(this, Constants.UNITY_GAME_ID, Constants.UNITY_TEST_MODE)

        if (UnityAds.isInitialized()) {
            UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID)
        } else {
            Handler().postDelayed({ UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID) }, 1500)
        }

        binding.backBtn.setOnClickListener{
            finish()
        }

        binding.winnersBtn.setOnClickListener {

            UnityAds.show(
                this,
                Constants.UNITY_INTERSTITIAL_AD_ID,
                object : IUnityAdsShowListener {
                    override fun onUnityAdsShowComplete(
                        s: String,
                        unityAdsShowCompletionState: UnityAds.UnityAdsShowCompletionState
                    ) {

                        startActivity(Intent(baseContext, WinnersActivity::class.java))
                    }

                    override fun onUnityAdsShowFailure(
                        s: String,
                        unityAdsShowError: UnityAds.UnityAdsShowError,
                        s1: String
                    ) {


                        startActivity(Intent(baseContext, WinnersActivity::class.java))
                    }

                    override fun onUnityAdsShowStart(s: String) {

                    }

                    override fun onUnityAdsShowClick(s: String) {}
                })
        }

        database.collection("luckyDrawSystem")
            .document("announceWinners")
            .addSnapshotListener{value, error->

                val imageLink = value?.getString("winnerImage")
                Glide.with(this)
                    .load(imageLink)
                    .into(binding.winnerImage)

            }


        database.collection("luckyDrawSystem")
            .document("owwOywj3ijLecKRafkna")
            .addSnapshotListener{value, error->
                coinCost = value?.get("cost").toString().toInt()
                binding.coinParticipate.setText(coinCost.toString() + " Coins required to participate")

            }

        mAuth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .addSnapshotListener{value, error->
                    if (value != null) {
                        isRedeemed = value.get("redeemed") as Boolean
                        val user: User? = value.toObject<User>(User::class.java)

                        if (user != null) {
                            winner = Winner(user.name, user.email, it.uid)
                        }

                    }



                    binding.participateBtn.isEnabled = isRedeemed


                }
        }

        binding.participateBtn.setOnClickListener {

            mAuth.currentUser?.let {
                database.collection("users")
                    .document(it.uid)
                    .update("redeemed", false).addOnSuccessListener {
                        collectionReference = database.collection("luckyDrawSystem")
                            .document("owwOywj3ijLecKRafkna")
                            .collection("participants")
                        collectionReference.add(winner).addOnSuccessListener {
                            UnityAds.show(
                                this,
                                Constants.UNITY_INTERSTITIAL_AD_ID,
                                object : IUnityAdsShowListener {
                                    override fun onUnityAdsShowComplete(
                                        s: String,
                                        unityAdsShowCompletionState: UnityAds.UnityAdsShowCompletionState
                                    ) {


                                    }

                                    override fun onUnityAdsShowFailure(
                                        s: String,
                                        unityAdsShowError: UnityAds.UnityAdsShowError,
                                        s1: String
                                    ) {



                                    }

                                    override fun onUnityAdsShowStart(s: String) {

                                    }

                                    override fun onUnityAdsShowClick(s: String) {}
                                })
                        }




                        Toast.makeText(this, "Participated Successfully", Toast.LENGTH_SHORT).show()
                    }
            }

            mAuth.currentUser?.let {
                database.collection("users")
                    .document(it.uid)
                    .update("coins", FieldValue.increment(-(coinCost.toLong()))).addOnSuccessListener {
                        Toast.makeText(this, "Participated Successfully", Toast.LENGTH_SHORT).show()
                        mAuth.uid?.let {it2 ->
                            database.collection("users")
                                .document(it2)
                                .collection("history")
                                .add(EarningHistory("Lucky Draw Redeem", coinCost))
                        }
                    }
            }

        }


    }
}