package com.phinion.easyrewards

import android.R
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.phinion.easyrewards.databinding.ActivitySignUpBinding
import com.phinion.easyrewards.databinding.LoadingDialogLayoutBinding
import com.phinion.easyrewards.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var loadingDialog: AlertDialog
    private lateinit var loadingDialogBinding: LoadingDialogLayoutBinding
    private val signUpCoins = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.firestore

        //Loading Dialog




        //Loading Dialog
        loadingDialogBinding  = LoadingDialogLayoutBinding.inflate(LayoutInflater.from(this))
        loadingDialog = AlertDialog.Builder(this)
            .setView(loadingDialogBinding.root)
            .setCancelable(false)
            .create()
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent)))


        binding.loginTextBtn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }
        binding.backBtn.setOnClickListener {
            finish()
        }


        binding.termsBtn.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.google.com"))
            startActivity(intent)
        }


        binding.signupBtn.setOnClickListener {

            if (editTextValidation(binding.nameEditText.text.toString().trim(), "Name")
                && editTextValidation(binding.emailEditText.text.toString().trim(), "Email")
                && editTextValidation(binding.passwordEditText.text.toString().trim(), "Password")
            ) {
                if (binding.checkBox.isChecked) {

                    signUpWithFirebase(
                        binding.nameEditText.text.toString(),
                        binding.emailEditText.text.toString().trim(),
                        binding.passwordEditText.text.toString().trim()
                    )
                } else {
                    Toast.makeText(
                        this,
                        "Please agree to our terms and conditions.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }




    }

    private fun editTextValidation(string: String, field: String): Boolean {
        return if (TextUtils.isEmpty(string)) {
            Toast.makeText(this, "$field field is empty", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun signUpWithFirebase(name: String, email: String, password: String) {



        loadingDialog.show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if (user != null) {
                         addDataToDatabase(user.uid, name, email, password, signUpCoins, false)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    loadingDialog.dismiss()
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }

    private fun addDataToDatabase(
        uid: String,
        name: String,
        email: String,
        password: String,
        signUpCoins: Int,
        b: Boolean
    ) {
        val db = Firebase.firestore
        val user = User(name, email, password, signUpCoins, b)
        db.collection("users")
            .document(uid)
            .set(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    if(binding.referEditText.text.toString().isNotEmpty()){
                        database.collection("users")
                            .document(binding.referEditText.text.toString())
                            .update("coins", FieldValue.increment(5000))
                            .addOnSuccessListener {
                                Toast.makeText(this, "Refer reward granted successfully..", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Sorry refer code is invalid or not working..", Toast.LENGTH_SHORT).show()
                            }
                    }


                    loadingDialog.dismiss()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    Toast.makeText(this, "Account created successfully...", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        this,
                        "Something went wrong.\nPlease try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


    }

}