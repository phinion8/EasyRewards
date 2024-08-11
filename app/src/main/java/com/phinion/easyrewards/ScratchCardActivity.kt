package com.phinion.easyrewards

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anupkumarpanwar.scratchview.ScratchView
import com.anupkumarpanwar.scratchview.ScratchView.IRevealListener
import com.phinion.easyrewards.databinding.ActivityScratchCardBinding
import com.phinion.easyrewards.utils.Constants.Companion.UNITY_GAME_ID
import com.phinion.easyrewards.utils.Constants.Companion.UNITY_INTERSTITIAL_AD_ID
import com.phinion.easyrewards.utils.Constants.Companion.UNITY_TEST_MODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.easyrewards.models.EarningHistory
import com.phinion.easyrewards.utils.CheckForInternet
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAds.UnityAdsShowCompletionState
import com.unity3d.ads.UnityAds.UnityAdsShowError

class ScratchCardActivity : AppCompatActivity() {
    lateinit var binding: ActivityScratchCardBinding
    lateinit var database: FirebaseFirestore
    lateinit var mAuth: FirebaseAuth
    private lateinit var loadingDialog: AlertDialog.Builder
    private lateinit var successDialog: AlertDialog.Builder
    private lateinit var checkForInternet: CheckForInternet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScratchCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.firestore
        mAuth = Firebase.auth
        var count = 0

        checkForInternet = CheckForInternet(this)

        UnityAds.initialize(this, UNITY_GAME_ID, UNITY_TEST_MODE)

        if (UnityAds.isInitialized()) {
            UnityAds.load(UNITY_INTERSTITIAL_AD_ID)
        } else {
            Handler().postDelayed({ UnityAds.load(UNITY_INTERSTITIAL_AD_ID) }, 1500)
        }

        val successBoxView = LayoutInflater.from(this).inflate(R.layout.success_dialog_layout, null)

        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.loading_dialog_layout, null)

        loadingDialog = AlertDialog.Builder(this).setView(messageBoxView)
        val loadingDialogInstance = loadingDialog.create()
        loadingDialogInstance.setCancelable(false)
        loadingDialogInstance.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))




        successBoxView.findViewById<Button>(R.id.ok_btn).setOnClickListener {


            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()



        }
        successDialog = AlertDialog.Builder(this).setView(successBoxView)
        val successDialogInstance = successDialog.create()
        successDialogInstance.setCancelable(false)
        successDialogInstance.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("scratchCard")
            .document("NR4YwjqCb6TzLGn64MZZ")
            .addSnapshotListener{value, error->
                val scratchRewardAmount = value?.get("randomRewardUpto").toString().toInt()
                val randomCoinUpto = scratchRewardAmount

                val randomCoin = (0..randomCoinUpto).random()
                binding.randomCoins.text = randomCoin.toString()
                binding.earnUptoText.text = this.getString(R.string.earn) + " " + randomCoinUpto.toString() + " " + this.getString(R.string.lucky_number_description)

                successBoxView.findViewById<TextView>(R.id.success_text).text = randomCoin.toString() + " " + this.getString(R.string.final_spin_reward_text)

                binding.scratchView.setRevealListener(object : IRevealListener {
                    override fun onRevealed(scratchView: ScratchView) {
                        scratchView.visibility = View.INVISIBLE
                        count++

                        loadingDialogInstance.show()

                        UnityAds.show(this@ScratchCardActivity, UNITY_INTERSTITIAL_AD_ID, object : IUnityAdsShowListener {
                            override fun onUnityAdsShowComplete(
                                s: String,
                                unityAdsShowCompletionState: UnityAdsShowCompletionState
                            ) {
                                mAuth.currentUser?.uid?.let {
                                    database.collection("users")
                                        .document(it)
                                        .update("coins", FieldValue.increment(randomCoin.toLong()))
                                        .addOnSuccessListener{
                                            loadingDialogInstance.dismiss()
                                            Toast.makeText(applicationContext, "You Won $randomCoin Coins", Toast.LENGTH_LONG)
                                                .show()
                                            successDialogInstance.show()
                                            mAuth.uid?.let { id ->
                                                database.collection("users")
                                                    .document(id)
                                                    .collection("history")
                                                    .add(EarningHistory("Scratch Card", randomCoin))
                                            }
                                        }
                                }

                            }

                            override fun onUnityAdsShowFailure(
                                s: String,
                                unityAdsShowError: UnityAdsShowError,
                                s1: String
                            ) {


                                Toast.makeText(baseContext, "Ads not available please try again later..", Toast.LENGTH_SHORT).show()

                                startActivity(Intent(baseContext, MainActivity::class.java))
                                finish()
                            }

                            override fun onUnityAdsShowStart(s: String) {

                            }

                            override fun onUnityAdsShowClick(s: String) {}
                        })


                    }

                    override fun onRevealPercentChangedListener(scratchView: ScratchView, percent: Float) {
                        if (percent >= 0.5) {
                            Log.d("Reveal Percentage", "onRevealPercentChangedListener: $percent")
                        }
                    }
                })
            }


        binding.backBtn2.setOnClickListener{
            finish()
        }












    }
}


