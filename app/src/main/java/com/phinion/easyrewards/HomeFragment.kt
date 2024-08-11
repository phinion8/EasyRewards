package com.phinion.easyrewards

import android.R
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.phinion.easyrewards.databinding.FragmentHomeBinding
import com.phinion.easyrewards.databinding.LoadingDialogLayoutBinding
import com.phinion.easyrewards.databinding.SuccessDialogLayoutBinding
import com.phinion.easyrewards.databinding.WarningDialogBinding
import com.phinion.easyrewards.models.User
import com.phinion.easyrewards.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.easyrewards.models.EarningHistory
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import java.util.*
import kotlin.let


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var warningDialog: AlertDialog
    private lateinit var warningDialogBinding: WarningDialogBinding
    private lateinit var successDialogLayoutBinding: SuccessDialogLayoutBinding
    private lateinit var successDialog: AlertDialog
    private lateinit var loadingDialog: AlertDialog
    private lateinit var loadingDialogBinding: LoadingDialogLayoutBinding
    private var dailyRewardAmount = 50

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        mAuth = Firebase.auth
        database = Firebase.firestore




        binding.participateNumberTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        binding.participateNumberTextView.setMarqueeRepeatLimit(-1);
        binding.participateNumberTextView.setSingleLine(true);
        binding.participateNumberTextView.setSelected(true);

        binding.participateDesTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        binding.participateDesTextView.setMarqueeRepeatLimit(-1);
        binding.participateDesTextView.setSingleLine(true);
        binding.participateDesTextView.setSelected(true);


        UnityAds.initialize(requireContext(), Constants.UNITY_GAME_ID, Constants.UNITY_TEST_MODE)

        if (UnityAds.isInitialized()) {
            UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID)
        } else {
            Handler().postDelayed({ UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID) }, 1500)
        }
        binding.contactBtn.setOnClickListener{
            startActivity(Intent(requireContext(), ContactActivity::class.java))
        }

        loadLocale()

        binding.changeLanguageBtn2.setOnClickListener {

            changeLanguage()
        }

        binding.participateCardView.setOnClickListener {
            startActivity(Intent(requireContext(), LuckyDrawActivity::class.java))
        }

        binding.mathsQuizCardView.setOnClickListener {
            startActivity(Intent(requireContext(), QuizActivity::class.java))
        }

        //Daily Reward
        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("dailyReward")
            .document("kpiuZAtNcIfI6uSGi79P")
            .addSnapshotListener{value, error->
                if (value != null) {
                    val rewardAmount = value.getString("rewardAmount") ?: "0"
                    dailyRewardAmount = rewardAmount.toInt()
                    binding.dailyRewardCoinText.text = dailyRewardAmount.toString()
                    val dailyRewardDes: String = this.getString(com.phinion.easyrewards.R.string.earn) + " " + dailyRewardAmount.toString() + " " + this.getString(com.phinion.easyrewards.R.string.daily_reward_description)
                    binding.dailyRewardDescription.text = dailyRewardDes
                }

            }

        //Participate Reward
        database.collection("luckyDrawSystem")
            .document("owwOywj3ijLecKRafkna")
            .addSnapshotListener{value, error->
                if (value != null) {
                    val rewardMethod = value.getString("rewardMethod")
                    binding.participateNumberCoin.text = rewardMethod
                    val participateRewardDes: String? = value.getString("rewardDes")
                    binding.participateDesTextView.text = participateRewardDes
                }

            }

        //Scratch And Earn
        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("scratchCard")
            .document("NR4YwjqCb6TzLGn64MZZ")
            .addSnapshotListener{value, error->
                val randomRewardUpto = value?.getString("randomRewardUpto") ?: "0"
                val scratchRewardAmount = randomRewardUpto.toInt()
                binding.scratchCoin.text = scratchRewardAmount.toString()
                val dailyRewardDes: String = this.getString(com.phinion.easyrewards.R.string.earn) + " " + scratchRewardAmount.toString() + " " + this.getString(com.phinion.easyrewards.R.string.scratch_card_description)
                binding.scratchDesTextView.text = dailyRewardDes


            }

        //Spin The Wheel
        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("spinTheWheel")
            .document("vF5XdFOfIA7X0zOBiV1a")
            .addSnapshotListener{value, error->
                val randomReward8 = value?.getString("randomReward8") ?: "0"
                val spinRewardAmount = randomReward8.toInt()
                binding.spinCoin.text = spinRewardAmount.toString()
                val spinRewardDes: String = this.getString(com.phinion.easyrewards.R.string.earn) + " " + spinRewardAmount.toString() + " " + this.getString(com.phinion.easyrewards.R.string.spin_wheel_description)
                binding.spinDesTextView.text = spinRewardDes

            }

        //Lucky Number
        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("luckyNumber")
            .document("IXvc2IY3FjvAh18fHjUY")
            .addSnapshotListener{value, error->
                val rewardAmount = value?.getString("rewardAmount") ?: "0"
                val luckyRewardAmount = rewardAmount.toInt()
                binding.luckyNumberCoin.text = luckyRewardAmount.toString()
                val luckyNumberDes: String = this.getString(com.phinion.easyrewards.R.string.earn) + " " + luckyRewardAmount.toString() + " " + this.getString(com.phinion.easyrewards.R.string.lucky_number_description)
                binding.luckyDesTextView.text = luckyNumberDes

            }

        //Refer and earn
        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("referReward")
            .document("JnZpNNahVp3ATEvhX1WN")
            .addSnapshotListener{value, error->
                val referReward = value?.getString("referReward") ?: "0"
                val rewardAmount = referReward.toInt()
                binding.referCoin.text = rewardAmount.toString()
                val referDes: String = this.getString(com.phinion.easyrewards.R.string.earn) + " " + rewardAmount.toString() + " " + this.getString(com.phinion.easyrewards.R.string.refer_description)
                binding.referDesTextView.text = referDes

            }


        //Warning Dialog
        warningDialogBinding = WarningDialogBinding.inflate(LayoutInflater.from(requireContext()))
        warningDialog = AlertDialog.Builder(requireContext())
            .setView(warningDialogBinding.root)
            .setCancelable(false)
            .create()
        warningDialogBinding.okButton.setOnClickListener { warningDialog.dismiss() }
        warningDialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent)))

        //Success Dialog
        successDialogLayoutBinding =
            SuccessDialogLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        successDialog = AlertDialog.Builder(requireContext())
            .setView(successDialogLayoutBinding.root)
            .setCancelable(false)
            .create()
        successDialogLayoutBinding.okBtn.setOnClickListener {
            UnityAds.show(requireActivity(), Constants.UNITY_INTERSTITIAL_AD_ID)
            successDialog.dismiss()
        }

        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .collection("dailyReward")
            .document("kpiuZAtNcIfI6uSGi79P")
            .addSnapshotListener{value, error->
                val dailyReward = value?.get("rewardAmount").toString()
                successDialogLayoutBinding.successText.text = dailyReward + " " + this.getString(com.phinion.easyrewards.R.string.final_spin_reward_text)
            }


        successDialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent)))

        //Loading Dialog
        loadingDialogBinding =
            LoadingDialogLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        loadingDialog = AlertDialog.Builder(requireContext())
            .setView(loadingDialogBinding.root)
            .setCancelable(false)
            .create()
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent)))


        mAuth.currentUser?.let {
            database.collection("users").document(it.uid).addSnapshotListener { value, error ->
                val user: User = value!!.toObject(User::class.java)!!
                binding.userCoins.text = (user.coins).toString()
                binding.userName.text = "Hello " + user.name
                binding.userEmail.text = user.email
            }
        }

        binding.scratchCardView.setOnClickListener {
            startActivity(Intent(requireContext(), ScratchCardActivity::class.java))

        }

        binding.spinWheelCardView.setOnClickListener {
            val intent = Intent(requireContext(), LuckyWheelActivity::class.java)
            startActivity(intent)
        }

        binding.luckyNumberCardView.setOnClickListener {
            startActivity(Intent(requireContext(), LuckyNumberActivity::class.java))
        }

        binding.surveyCardView.setOnClickListener {
            showUnityAds(ReferActivity::class.java)
        }

        binding.dailyRewardCardView.setOnClickListener {
            loadingDialog.show()
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]


            val todaystring = year.toString() + "" + month + "" + day + ""

            val preferences = requireActivity().getSharedPreferences("PREFS", 0)
            val currentDay = preferences.getBoolean(todaystring, false)

            if (!currentDay) {
                database.collection("users")
                    .document(FirebaseAuth.getInstance().uid!!)
                    .update("coins", FieldValue.increment(dailyRewardAmount.toLong())).addOnSuccessListener {

                        loadingDialog.dismiss()
                        successDialog.show()
                        mAuth.uid?.let { id ->
                            database.collection("users")
                                .document(id)
                                .collection("history")
                                .add(EarningHistory("Daily Reward", dailyRewardAmount))
                        }
                        Toast.makeText(context, "Reward Granted", Toast.LENGTH_SHORT).show()
                    }

                val editor = preferences.edit()
                editor.putBoolean(todaystring, true)
                editor.apply()
            } else {
                loadingDialog.dismiss()
                warningDialog.show()
                Toast.makeText(
                    context,
                    "You have already received your today's reward",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }






        return binding.root
    }

    private fun showUnityAds(ActivityToOpen: Class<out Activity?>) {
        UnityAds.show(
            requireActivity(),
            Constants.UNITY_INTERSTITIAL_AD_ID,
            object : IUnityAdsShowListener {
                override fun onUnityAdsShowComplete(
                    s: String,
                    unityAdsShowCompletionState: UnityAds.UnityAdsShowCompletionState
                ) {
                    startActivity(Intent(requireContext(), ActivityToOpen))

                }

                override fun onUnityAdsShowFailure(
                    s: String,
                    unityAdsShowError: UnityAds.UnityAdsShowError,
                    s1: String
                ) {


                    startActivity(Intent(requireContext(), ActivityToOpen))

                }

                override fun onUnityAdsShowStart(s: String) {

                }

                override fun onUnityAdsShowClick(s: String) {}
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
    }


    fun changeLanguage(){
        val language = arrayOf("English", "Spanish")
        val mBuilder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        val dialog = mBuilder.create()
        mBuilder.setTitle("Select Language")
        mBuilder.setSingleChoiceItems(language, -1, DialogInterface.OnClickListener{ dialog, which ->
            if (which == 0){
                setLocale("en")
                activity?.recreate()
                dialog.dismiss()
            }else if (which == 1){
                setLocale("es");
                activity?.recreate()
                dialog.dismiss()
            }
        })
        mBuilder.create()
        mBuilder.show()

    }

    fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        context?.resources?.updateConfiguration(configuration, requireContext().resources.displayMetrics)

        val editor: SharedPreferences.Editor? =
            context?.getSharedPreferences("Settings", Context.MODE_PRIVATE)?.edit()
        editor?.putString("app_lang", language)
        editor?.apply()

    }

    fun loadLocale(){
        val prefs: SharedPreferences? = context?.getSharedPreferences("Settings",
            Context.MODE_PRIVATE
        )
        val language =
            prefs?.getString("app_lang", "en")
        if (language != null) {
            setLocale(language)
        }
    }


}