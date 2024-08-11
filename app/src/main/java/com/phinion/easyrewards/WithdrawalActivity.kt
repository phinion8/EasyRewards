package com.phinion.easyrewards

import android.R
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.easyrewards.databinding.ActivityWithdrawalBinding
import com.phinion.easyrewards.databinding.LoadingDialogLayoutBinding
import com.phinion.easyrewards.databinding.SuccessDialogLayoutBinding
import com.phinion.easyrewards.models.EarningHistory
import com.phinion.easyrewards.models.User
import com.phinion.easyrewards.models.WithdrawalHistory
import com.phinion.easyrewards.models.WithdrawalRequest

class WithdrawalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWithdrawalBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var user: User
    private lateinit var collectionReference: CollectionReference
    private lateinit var successDialogLayoutBinding: SuccessDialogLayoutBinding
    private lateinit var successDialog: AlertDialog
    private lateinit var loadingDialog: AlertDialog
    private lateinit var loadingDialogBinding: LoadingDialogLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth
        database = Firebase.firestore


        binding.ccp.setDialogCornerRaius(5f)
        binding.ccp.setAutoDetectedCountry(true)


        //Success Dialog
        successDialogLayoutBinding =
            SuccessDialogLayoutBinding.inflate(LayoutInflater.from(this))
        successDialog = AlertDialog.Builder(this)
            .setView(successDialogLayoutBinding.root)
            .setCancelable(false)
            .create()
        successDialogLayoutBinding.okBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        successDialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent)))

        //Loading Dialog
        loadingDialogBinding =
            LoadingDialogLayoutBinding.inflate(LayoutInflater.from(this))
        loadingDialog = AlertDialog.Builder(this)
            .setView(loadingDialogBinding.root)
            .setCancelable(false)
            .create()
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent)))


        val platform = intent.getStringExtra("platform")
        val amountOfCoins = intent.getIntExtra("amountOfCoins", 0)

        mAuth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .addSnapshotListener { value, error ->
                    user = value!!.toObject(User::class.java)!!
                }
        }

        if (platform == "PUBG") {
            binding.withdrawalId.hint = "Enter Your PUBG ID"
        }else if (platform == "Free Fire"){
            binding.withdrawalId.hint = "Enter Your Free Fire ID"
        }else if (platform == "Google Pay"){
            binding.withdrawalId.hint = "Enter Your Google Pay Number"
        }else{
            Toast.makeText(this, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.submitBtn.setOnClickListener {


            if (binding.withdrawalId.text.toString().isNotEmpty()) {

                loadingDialog.show()

                database.collection("users")
                    .document(FirebaseAuth.getInstance().uid!!)
                    .update(
                        "coins",
                        FieldValue.increment(-(amountOfCoins.toLong()))
                    ).addOnSuccessListener {
                        val withdrawalRequest = mAuth.currentUser?.let { it1 ->
                            WithdrawalRequest(
                                it1.uid,
                                user.email,
                                amountOfCoins,
                                platform.toString(),
                                binding.withdrawalId.text.toString(),
                                binding.ccp.selectedCountryEnglishName.toString()
                                )
                        }
                        collectionReference = database.collection("withdraws")
                        if (withdrawalRequest != null) {
                            collectionReference.add(withdrawalRequest)
                                .addOnSuccessListener(
                                    OnSuccessListener<DocumentReference?> {

                                        mAuth.uid?.let { it1 ->
                                            database.collection("users")
                                                .document(it1)
                                                .collection("history")
                                                .add(EarningHistory("Redeemed", amountOfCoins))
                                                .addOnSuccessListener {
                                                    mAuth.uid?.let {it3->

                                                        database.collection("users")
                                                            .document(it3)
                                                            .collection("withdrawalHistory")
                                                            .add(WithdrawalHistory("Pending", platform.toString(), amountOfCoins))
                                                    }
                                                }


                                        }

                                        //Success Dialog
                                        binding.submitBtn.isEnabled = true
                                        mAuth.currentUser?.uid?.let { it1 ->
                                            database.collection("users")
                                                .document(it1)
                                                .update("redeemed", true)
                                        }
                                        loadingDialog.dismiss()
                                        successDialogLayoutBinding.successText.text =
                                            "You will receive your money within 1 to 7 working days and you will also be notified on your registered email id."
                                        successDialog.show()
                                        Toast.makeText(
                                            this,
                                            "Request sent successfully.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })

                        }


                    }

            }else{
                Toast.makeText(this, "Please enter a valid id", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
