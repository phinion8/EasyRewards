package com.phinion.easyrewards

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.easyrewards.SpinWheel.LuckyWheelView
import com.phinion.easyrewards.SpinWheel.model.LuckyItem
import com.phinion.easyrewards.databinding.LoadingDialogLayoutBinding
import com.phinion.easyrewards.databinding.SuccessDialogLayoutBinding
import com.phinion.easyrewards.models.EarningHistory
import com.phinion.easyrewards.utils.CheckForInternet
import com.phinion.easyrewards.utils.Constants
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import java.util.*


class LuckyWheelActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var successDialogLayoutBinding: SuccessDialogLayoutBinding
    private lateinit var successDialog: AlertDialog
    private lateinit var loadingDialog: AlertDialog
    private lateinit var loadingDialogBinding: LoadingDialogLayoutBinding
    private var spinRewardAmount1: Int = 0
    private var spinRewardAmount2: Int = 5
    private var spinRewardAmount3: Int = 10
    private var spinRewardAmount4: Int = 15
    private var spinRewardAmount5: Int = 20
    private var spinRewardAmount6: Int = 25
    private var spinRewardAmount7: Int = 30
    private var spinRewardAmount8: Int = 35
    private lateinit var checkForInternet: CheckForInternet




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lucky_wheel)

        checkForInternet = CheckForInternet(this)
        val wheelview = findViewById<LuckyWheelView>(R.id.wheelview)
        val spinBtn = findViewById<Button>(R.id.spin_btn)





        mAuth = Firebase.auth
        database = Firebase.firestore

        //Loading Dialog
        loadingDialogBinding =
            LoadingDialogLayoutBinding.inflate(LayoutInflater.from(this))
        loadingDialog = AlertDialog.Builder(this)
            .setView(loadingDialogBinding.root)
            .setCancelable(false)
            .create()
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))




        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("spinTheWheel")
            .document("vF5XdFOfIA7X0zOBiV1a")
            .addSnapshotListener { value, error ->
                spinRewardAmount1 = value?.get("randomReward1").toString().toInt()
                spinRewardAmount2 = value?.get("randomReward2").toString().toInt()
                spinRewardAmount3 = value?.get("randomReward3").toString().toInt()
                spinRewardAmount4 = value?.get("randomReward4").toString().toInt()
                spinRewardAmount5 = value?.get("randomReward5").toString().toInt()
                spinRewardAmount6 = value?.get("randomReward6").toString().toInt()
                spinRewardAmount7 = value?.get("randomReward7").toString().toInt()
                spinRewardAmount8 = value?.get("randomReward8").toString().toInt()


                val data: MutableList<LuckyItem> = ArrayList<LuckyItem>()

                val luckyItem1 = LuckyItem()
                luckyItem1.topText = spinRewardAmount1.toString()
                luckyItem1.secondaryText = "COINS"
                luckyItem1.textColor = Color.parseColor("#212121")
                luckyItem1.color = Color.parseColor("#eceff1")
                data.add(luckyItem1)

                val luckyItem2 = LuckyItem()
                luckyItem2.topText = spinRewardAmount2.toString()
                luckyItem2.secondaryText = "COINS"
                luckyItem2.color = Color.parseColor("#00cf00")
                luckyItem2.textColor = Color.parseColor("#ffffff")
                data.add(luckyItem2)

                val luckyItem3 = LuckyItem()
                luckyItem3.topText = spinRewardAmount3.toString()
                luckyItem3.secondaryText = "COINS"
                luckyItem3.textColor = Color.parseColor("#212121")
                luckyItem3.color = Color.parseColor("#eceff1")
                data.add(luckyItem3)

                val luckyItem4 = LuckyItem()
                luckyItem4.topText = spinRewardAmount4.toString()
                luckyItem4.secondaryText = "COINS"
                luckyItem4.color = Color.parseColor("#7f00d9")
                luckyItem4.textColor = Color.parseColor("#ffffff")
                data.add(luckyItem4)

                val luckyItem5 = LuckyItem()
                luckyItem5.topText = spinRewardAmount5.toString()
                luckyItem5.secondaryText = "COINS"
                luckyItem5.textColor = Color.parseColor("#212121")
                luckyItem5.color = Color.parseColor("#eceff1")
                data.add(luckyItem5)

                val luckyItem6 = LuckyItem()
                luckyItem6.topText = spinRewardAmount6.toString()
                luckyItem6.secondaryText = "COINS"
                luckyItem6.color = Color.parseColor("#dc0000")
                luckyItem6.textColor = Color.parseColor("#ffffff")
                data.add(luckyItem6)

                val luckyItem7 = LuckyItem()
                luckyItem7.topText = spinRewardAmount7.toString()
                luckyItem7.secondaryText = "COINS"
                luckyItem7.textColor = Color.parseColor("#212121")
                luckyItem7.color = Color.parseColor("#eceff1")
                data.add(luckyItem7)

                val luckyItem8 = LuckyItem()
                luckyItem8.topText = spinRewardAmount8.toString()
                luckyItem8.secondaryText = "COINS"
                luckyItem8.color = Color.parseColor("#008bff")
                luckyItem8.textColor = Color.parseColor("#ffffff")
                data.add(luckyItem8)


                wheelview.setData(data)
                wheelview.setRound(5)


            }




        UnityAds.initialize(this, Constants.UNITY_GAME_ID, Constants.UNITY_TEST_MODE)

        if (UnityAds.isInitialized()) {
            UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID)
        } else {
            Handler().postDelayed({ UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID) }, 1500)
        }

        findViewById<ImageView>(R.id.back_btn3).setOnClickListener {
            finish()
        }
        //Success Dialog
        successDialogLayoutBinding =
            SuccessDialogLayoutBinding.inflate(LayoutInflater.from(this))
        successDialog = AlertDialog.Builder(this)
            .setView(successDialogLayoutBinding.root)
            .setCancelable(false)
            .create()
        successDialogLayoutBinding.okBtn.setOnClickListener {

            successDialog.dismiss()
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()

        }
        successDialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))





        spinBtn.setOnClickListener(View.OnClickListener {


            spinBtn.isEnabled = false

            val r = Random()
            val randomNumber = r.nextInt(8)
            wheelview.startLuckyWheelWithTargetIndex(randomNumber)


        })

        wheelview.setLuckyRoundItemSelectedListener { index ->
            updateCash(index)
            loadingDialog.show()
        }


    }

    private fun updateCash(index: Int) {
        var cash: Long = 0
        when (index) {
            0 -> cash = spinRewardAmount1.toLong()
            1 -> cash = spinRewardAmount2.toLong()
            2 -> cash = spinRewardAmount3.toLong()
            3 -> cash = spinRewardAmount4.toLong()
            4 -> cash = spinRewardAmount5.toLong()
            5 -> cash = spinRewardAmount6.toLong()
            6 -> cash = spinRewardAmount7.toLong()
            7 -> cash = spinRewardAmount8.toLong()
        }
        val finalCash = cash

        UnityAds.show(this, Constants.UNITY_INTERSTITIAL_AD_ID, object : IUnityAdsShowListener {
            override fun onUnityAdsShowComplete(
                s: String,
                unityAdsShowCompletionState: UnityAds.UnityAdsShowCompletionState
            ) {

                mAuth.currentUser?.let {
                    database.collection("users")
                        .document(it.uid)
                        .update("coins", FieldValue.increment(cash)).addOnSuccessListener {

                            mAuth.uid?.let {it2 ->
                                database.collection("users")
                                    .document(it2)
                                    .collection("history")
                                    .add(EarningHistory("Lucky Wheel", finalCash.toInt()))



                            }


                            mAuth.uid?.let { it1 ->
                                database.collection("users")
                                    .document(it1)
                                    .update("spinCount", FieldValue.increment(-1)).addOnSuccessListener {

                                    }
                            }

                            loadingDialog.dismiss()
                            successDialogLayoutBinding.successText.text =
                                finalCash.toString() + " " + baseContext.getString(R.string.final_spin_reward_text)
                            successDialog.show()




                            Toast.makeText(
                                baseContext,
                                finalCash.toString() + " " + baseContext.getString(R.string.final_spin_reward_text),
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                }

            }

            override fun onUnityAdsShowFailure(
                s: String,
                unityAdsShowError: UnityAds.UnityAdsShowError,
                s1: String
            ) {

                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()
                Toast.makeText(baseContext, "Ads not available please try again later...", Toast.LENGTH_SHORT).show()
                Log.d("DBPS", "Ad failed to load")
            }

            override fun onUnityAdsShowStart(s: String) {

            }

            override fun onUnityAdsShowClick(s: String) {}
        })



    }


}