package com.phinion.easyrewards

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.easyrewards.databinding.ActivityResultBinding
import com.phinion.easyrewards.models.EarningHistory
import com.phinion.easyrewards.utils.Constants
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    var POINTS: Int = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = Firebase.firestore
        mAuth = Firebase.auth

        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("quizReward")
            .document("LLEKc1aeMHqWA6uw2nLt")
            .addSnapshotListener{value, error->
                if (value != null) {
                    POINTS = value.get("rewardAmountPerQuiz").toString().toInt()
                }
            }

        val correctAnswers = intent.getIntExtra("correct", 0)
        val totalQuestions = intent.getIntExtra("total", 0)

        val points: Int = correctAnswers * POINTS



        binding.score.text = String.format("%d/%d", correctAnswers, totalQuestions)
        binding.earnedCoins.text = points.toString()

        UnityAds.initialize(this, Constants.UNITY_GAME_ID, Constants.UNITY_TEST_MODE)

        if (UnityAds.isInitialized()) {
            UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID)
        } else {
            Handler().postDelayed({ UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID) }, 1500)
        }

        UnityAds.show(
            this,
            Constants.UNITY_INTERSTITIAL_AD_ID,
            object : IUnityAdsShowListener {
                override fun onUnityAdsShowComplete(
                    s: String,
                    unityAdsShowCompletionState: UnityAds.UnityAdsShowCompletionState
                ) {


                    val database = FirebaseFirestore.getInstance()
                    database.collection("users")
                        .document(FirebaseAuth.getInstance().uid!!)
                        .update("coins", FieldValue.increment(points.toLong()))
                        .addOnSuccessListener {
                            mAuth.uid?.let { id ->
                                database.collection("users")
                                    .document(id)
                                    .collection("history")
                                    .add(EarningHistory("Quiz Reward", points))
                            }
                            Toast.makeText(baseContext, "$points Coins Added In Your Account", Toast.LENGTH_SHORT).show()
                        }
                }

                override fun onUnityAdsShowFailure(
                    s: String,
                    unityAdsShowError: UnityAds.UnityAdsShowError,
                    s1: String
                ) {

                    Toast.makeText(baseContext, "Failed to load the ad please try again later...", Toast.LENGTH_SHORT).show()


                }

                override fun onUnityAdsShowStart(s: String) {

                }

                override fun onUnityAdsShowClick(s: String) {}
            })





















        binding.restartBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }








        binding.homeBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        })


    }
}