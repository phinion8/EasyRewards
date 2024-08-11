package com.phinion.easyrewards

import android.R
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.easyrewards.adapters.WithdrawalMethodAdapter
import com.phinion.easyrewards.databinding.FragmentWalletBinding
import com.phinion.easyrewards.databinding.LoadingDialogLayoutBinding
import com.phinion.easyrewards.databinding.SuccessDialogLayoutBinding
import com.phinion.easyrewards.models.User
import com.phinion.easyrewards.models.WithdrawalMethod
import com.phinion.easyrewards.models.WithdrawalRequest
import com.phinion.easyrewards.utils.CheckForInternet
import com.phinion.easyrewards.utils.Constants
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds


class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var collectionReference: CollectionReference
    private var minimumCoin: Int = 5000
    private lateinit var user: User
    private lateinit var successDialogLayoutBinding: SuccessDialogLayoutBinding
    private lateinit var successDialog: AlertDialog
    private lateinit var loadingDialog: AlertDialog
    private lateinit var loadingDialogBinding: LoadingDialogLayoutBinding
    private lateinit var adapter: WithdrawalMethodAdapter
    var plarform: String  = ""
    private lateinit var checkForInternet: CheckForInternet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkForInternet = CheckForInternet(requireContext())

        // Inflate the layout for this fragment
        binding = FragmentWalletBinding.inflate(layoutInflater, container, false)

        mAuth = Firebase.auth
        database = Firebase.firestore

        val withdrawalList: ArrayList<WithdrawalMethod> = ArrayList<WithdrawalMethod>()

        UnityAds.initialize(requireContext(), Constants.UNITY_GAME_ID, Constants.UNITY_TEST_MODE)

        if (UnityAds.isInitialized()) {
            UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID)
        } else {
            Handler().postDelayed({ UnityAds.load(Constants.UNITY_INTERSTITIAL_AD_ID) }, 1500)
        }

        UnityAds.show(
            requireActivity(),
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




        database.collection("withdrawalMethods")
            .addSnapshotListener { value, error ->
                withdrawalList.clear()
                for (snapshot in value!!.documents) {
                    val withdrawalItem: WithdrawalMethod? =
                        snapshot.toObject(WithdrawalMethod::class.java)
                    if (withdrawalItem != null) {
                        withdrawalItem.id = snapshot.id


                    }
                    if (withdrawalItem != null) {
                        withdrawalList.add(withdrawalItem)
                    }
                }
                adapter.notifyDataSetChanged()
            }

        adapter = WithdrawalMethodAdapter(requireContext(), withdrawalList,
            object : WithdrawalItemOnClick {
                override fun WithdrawalItemOnClickListener(position: Int) {

                    val minimumCoin = withdrawalList[position].amountOfCoins
                    val provider = withdrawalList[position].platform
                    if (user.coins >= minimumCoin) {


                        binding.submitBtn.isEnabled = false


                        loadingDialog.show()
                        val intent = Intent(requireContext(), WithdrawalActivity::class.java)
                        intent.putExtra("platform", withdrawalList[position].platform)
                        intent.putExtra("amountOfCoins", withdrawalList[position].amountOfCoins)
                        startActivity(intent)
                        requireActivity().finish()




//                        loadingDialog.show()
//
//                        database.collection("users")
//                            .document(FirebaseAuth.getInstance().uid!!)
//                            .update(
//                                "coins",
//                                FieldValue.increment(-(minimumCoin.toLong()))
//                            ).addOnSuccessListener {
//                                val withdrawalRequest = mAuth.currentUser?.let { it1 ->
//                                    WithdrawalRequest(
//                                        it1.uid,
//                                        user.email,
//                                        minimumCoin,
//                                        provider
//                                    )
//                                }
//                                collectionReference = database.collection("withdraws")
//                                if (withdrawalRequest != null) {
//                                    collectionReference.add(withdrawalRequest)
//                                        .addOnSuccessListener(
//                                            OnSuccessListener<DocumentReference?> {
//
//                                                //Success Dialog
//                                                binding.submitBtn.isEnabled = true
//                                                mAuth.currentUser?.uid?.let { it1 ->
//                                                    database.collection("users")
//                                                        .document(it1)
//                                                        .update("redeemed", true)
//                                                }
//                                                loadingDialog.dismiss()
//                                                successDialogLayoutBinding.successText.text =
//                                                    "You will receive your money within 24 hours."
//                                                successDialog.show()
//                                                Toast.makeText(
//                                                    requireContext(),
//                                                    "Request sent successfully.",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
//                                            })
//
//                                }
//
//
//                            }
                    } else {
                        loadingDialog.dismiss()
                        Toast.makeText(requireContext(), "Insufficient coins", Toast.LENGTH_SHORT)
                            .show()
                    }


                }
            })
        binding.withdrawalList.adapter = adapter
        binding.withdrawalList.layoutManager = GridLayoutManager(requireContext(), 2)

        //Success Dialog
        successDialogLayoutBinding =
            SuccessDialogLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        successDialog = AlertDialog.Builder(requireContext())
            .setView(successDialogLayoutBinding.root)
            .setCancelable(false)
            .create()
        successDialogLayoutBinding.okBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
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


        database.collection("earningSystems")
            .document("4QkPp047g45DeHGLGIVt")
            .addSnapshotListener { value, error ->
                minimumCoin = value?.get("withdrawalLimit").toString().toInt()
                val withdrawalValue = value?.get("withdrawalValue").toString()
                val withdrawalCoinValue = value?.get("withdrawalCoinValue").toString()
                val withdrawalMethodText = value?.get("withdrawalMethod").toString()
                binding.withdrawalMethodText.text = withdrawalMethodText
                binding.withdrawalEmail.hint =
                    this.getString(com.phinion.easyrewards.R.string.enter_your) + " " + withdrawalMethodText + " " + this.getString(
                        com.phinion.easyrewards.R.string.email_address
                    )
                binding.minimumDes.text =
                    this.getString(com.phinion.easyrewards.R.string.minimum_des) + " " + minimumCoin.toString()

                binding.withdrawalValueText.text =
                    this.getString(com.phinion.easyrewards.R.string.note) + " " +
                            "" + withdrawalCoinValue + " " + this.getString(com.phinion.easyrewards.R.string.withdrawal_value_1) + " " + withdrawalValue


            }

        mAuth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .addSnapshotListener { value, error ->
                    user = value!!.toObject(User::class.java)!!
                    binding.userName.text = "Hello " + user.name
                    binding.userEmail.text = user.email
                    binding.userCoins.text = user.coins.toString()
                }
        }


        binding.submitBtn.setOnClickListener(View.OnClickListener {


            val coinAmount = binding.withdrawalCoins.text.toString()

            if (coinAmount.isNotEmpty()) {


                if (coinAmount.toInt() >= minimumCoin) {


                    if (coinAmount.toInt() <= user.coins) {
                        if (user.coins >= minimumCoin) {
                            if (isValidEmail(binding.withdrawalEmail.text.toString())) {


                                binding.submitBtn.isEnabled = false



                                loadingDialog.show()

                                database.collection("users")
                                    .document(FirebaseAuth.getInstance().uid!!)
                                    .update(
                                        "coins",
                                        FieldValue.increment(-(coinAmount.toLong()))
                                    ).addOnSuccessListener {
                                        val withdrawalRequest = mAuth.currentUser?.let { it1 ->
                                            WithdrawalRequest(
                                                it1.uid,
                                                binding.withdrawalEmail.text.toString(),
                                                coinAmount.toInt()
                                            )
                                        }
                                        collectionReference = database.collection("withdraws")
                                        if (withdrawalRequest != null) {
                                            collectionReference.add(withdrawalRequest)
                                                .addOnSuccessListener(
                                                    OnSuccessListener<DocumentReference?> {

                                                        //Success Dialog
                                                        binding.submitBtn.isEnabled = true
                                                        loadingDialog.dismiss()
                                                        successDialogLayoutBinding.successText.text =
                                                            "You will receive your money within 24 hours."
                                                        successDialog.show()
                                                        Toast.makeText(
                                                            requireContext(),
                                                            "Request sent successfully.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    })
                                        }

                                    }

                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Please enter a valid email address.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.submitBtn.isEnabled = true
                            }

                        } else {

                            Toast.makeText(
                                requireContext(),
                                "Insufficient Coins",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } else {
                        binding.submitBtn.isEnabled = true
                        Toast.makeText(
                            requireContext(),
                            "Insufficient Coins",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.submitBtn.isEnabled = true

                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please enter coins greater than $minimumCoin",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.submitBtn.isEnabled = true
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    "Coin field is empty.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.submitBtn.isEnabled = true
            }
        })


        return binding.root
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && target?.let {
            Patterns.EMAIL_ADDRESS.matcher(it).matches()
        } == true
    }

}