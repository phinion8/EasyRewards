package com.phinion.easyrewards

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.phinion.easyrewards.databinding.ActivityLuckyNumberBinding
import com.phinion.easyrewards.databinding.WarningDialogBinding
import com.phinion.easyrewards.utils.Constants
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

class LuckyNumberActivity : AppCompatActivity() {
    lateinit var binding: ActivityLuckyNumberBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var loadingDialog: AlertDialog.Builder
    private lateinit var successDialog: AlertDialog.Builder
    private lateinit var warningDialog: androidx.appcompat.app.AlertDialog
    private lateinit var warningDialogBinding: WarningDialogBinding
    private var chances: Int = 3
    private lateinit var checkForInternet: CheckForInternet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLuckyNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkForInternet = CheckForInternet(this)

        mAuth = Firebase.auth
        database = Firebase.firestore

        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("luckyNumber")
            .document("IXvc2IY3FjvAh18fHjUY")
            .addSnapshotListener { value, error ->
                val luckyRewardAmount = value?.get("rewardAmount").toString().toInt()
                binding.luckyNumberTitle.text =
                    this.getString(R.string.pick_a_number_and_try_your_luck_to_win) + " " + luckyRewardAmount.toString() + " " + this.getString(
                        R.string.lucky_number_description
                    )

            }

        UnityAds.initialize(this, Constants.UNITY_GAME_ID, Constants.UNITY_TEST_MODE)

        if (UnityAds.isInitialized()) {
            UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID)
        } else {
            Handler().postDelayed({ UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID) }, 1500)
        }
        binding.backBtn4.setOnClickListener {
            finish()
        }


        //Warning Dialog
        warningDialogBinding = WarningDialogBinding.inflate(LayoutInflater.from(this))
        warningDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(warningDialogBinding.root)
            .setCancelable(false)
            .create()
        warningDialogBinding.okButton.setOnClickListener {
            UnityAds.show(this, Constants.UNITY_INTERSTITIAL_AD_ID)
            warningDialog.dismiss()
        }
        warningDialogBinding.errorTitle.text = "Sorry :( , better luck next time..."
        warningDialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))






        binding.button1.setOnClickListener {
            checkWon(binding.button1)
        }

        binding.button2.setOnClickListener {
            checkWon(binding.button2)
        }

        binding.button3.setOnClickListener {
            checkWon(binding.button3)
        }

        binding.button4.setOnClickListener {
            checkWon(binding.button4)
        }

        binding.button5.setOnClickListener {
            checkWon(binding.button5)
        }


        binding.button6.setOnClickListener {
            checkWon(binding.button6)
        }


        binding.button7.setOnClickListener {
            checkWon(binding.button7)
        }


        binding.button8.setOnClickListener {
            checkWon(binding.button8)
        }



        binding.button9.setOnClickListener {
            checkWon(binding.button9)
        }

        binding.button10.setOnClickListener {
            checkWon(binding.button10)
        }

        binding.button11.setOnClickListener {
            checkWon(binding.button11)
        }

        binding.button12.setOnClickListener {
            checkWon(binding.button12)
        }

        binding.button13.setOnClickListener {
            checkWon(binding.button13)
        }

        binding.button14.setOnClickListener {
            checkWon(binding.button14)
        }

        binding.button15.setOnClickListener {
            checkWon(binding.button15)
        }

        binding.button16.setOnClickListener {
            checkWon(binding.button16)
        }


    }

    private fun checkWon(button: Button) {

        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("luckyNumber")
            .document("IXvc2IY3FjvAh18fHjUY")
            .addSnapshotListener { value, error ->
                val luckyRewardAmount = value?.get("rewardAmount").toString().toInt()
                val randomPlace = (1..16).random()
                Log.d("DBPS", randomPlace.toString())

                chances--
                Log.d("DBPS", chances.toString())
                val messageBoxView =
                    LayoutInflater.from(this).inflate(R.layout.loading_dialog_layout, null)
                loadingDialog = AlertDialog.Builder(this).setView(messageBoxView)
                val loadingDialogInstance = loadingDialog.create()
                loadingDialogInstance.setCancelable(false)
                loadingDialogInstance.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                loadingDialogInstance.show()


                val successBoxView =
                    LayoutInflater.from(this).inflate(R.layout.success_dialog_layout, null)
                successBoxView.findViewById<TextView>(R.id.success_text).text =
                    luckyRewardAmount.toString() + " " + this.getString(R.string.final_spin_reward_text)
                successBoxView.findViewById<Button>(R.id.ok_btn).setOnClickListener {
                    finish()
                }
                successDialog = AlertDialog.Builder(this).setView(successBoxView)
                val successDialogInstance = successDialog.create()
                successDialogInstance.setCancelable(false)
                successDialogInstance.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                if (chances <= 0) {
                    if (button.text == randomPlace.toString()) {
                        UnityAds.show(
                            this,
                            Constants.UNITY_INTERSTITIAL_AD_ID,
                            object : IUnityAdsShowListener {
                                override fun onUnityAdsShowComplete(
                                    s: String,
                                    unityAdsShowCompletionState: UnityAds.UnityAdsShowCompletionState
                                ) {
                                    mAuth.currentUser?.uid?.let {
                                        database.collection("users")
                                            .document(it)
                                            .update(
                                                "coins",
                                                FieldValue.increment(luckyRewardAmount.toLong())
                                            )
                                            .addOnSuccessListener {
                                                mAuth.uid?.let {it2 ->
                                                    database.collection("users")
                                                        .document(it2)
                                                        .collection("history")
                                                        .add(EarningHistory("Lucky Number", luckyRewardAmount))
                                                }
                                                loadingDialogInstance.dismiss()
                                                successDialogInstance.show()

                                                Toast.makeText(
                                                    baseContext,
                                                    baseContext.getString(R.string.you_won_text) + " " + baseContext.getString(
                                                        R.string.lucky_number_description
                                                    ),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                startActivity(
                                                    Intent(
                                                        baseContext,
                                                        MainActivity::class.java
                                                    )
                                                )
                                                finish()
                                            }
                                    }

                                }

                                override fun onUnityAdsShowFailure(
                                    s: String,
                                    unityAdsShowError: UnityAds.UnityAdsShowError,
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

                    } else {
                        warningDialogBinding.errorTitle.text =
                            this.getString(R.string.sorry_try_again_text) + " " + "\n" + this.getString(
                                R.string.you_have_text
                            ) + " " + chances + " " + this.getString(R.string.chances_left)
                        warningDialog.show()
                        warningDialogBinding.okButton.setOnClickListener {
                            UnityAds.show(
                                this,
                                Constants.UNITY_INTERSTITIAL_AD_ID,
                                object : IUnityAdsShowListener {
                                    override fun onUnityAdsShowComplete(
                                        s: String,
                                        unityAdsShowCompletionState: UnityAds.UnityAdsShowCompletionState
                                    ) {
                                        warningDialog.dismiss()
                                        startActivity(Intent(baseContext, MainActivity::class.java))
                                        finish()
                                    }

                                    override fun onUnityAdsShowFailure(
                                        s: String,
                                        unityAdsShowError: UnityAds.UnityAdsShowError,
                                        s1: String
                                    ) {


                                        warningDialog.dismiss()
                                        startActivity(Intent(baseContext, MainActivity::class.java))
                                        finish()
                                    }

                                    override fun onUnityAdsShowStart(s: String) {

                                    }

                                    override fun onUnityAdsShowClick(s: String) {}
                                })
                        }
                    }
                } else {

                    if (chances == 2 || chances == 1) {
                        loadingDialogInstance.dismiss()
                        warningDialogBinding.errorTitle.text =
                            this.getString(R.string.sorry_try_again_text) + " " + "\n" + this.getString(
                                R.string.you_have_text
                            ) + " " + chances + " " + this.getString(R.string.chances_left)
                        warningDialog.show()


                    }
                }

            }


    }
}