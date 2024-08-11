package com.phinion.easyrewards

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.phinion.easyrewards.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {
    lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.signupBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.backBtn.setOnClickListener {
            finish()
        }

        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.loading_dialog_layout, null)

        //AlertDialogBuilder
        loadingDialog = AlertDialog.Builder(this).setView(messageBoxView)
        val loadingDialogInstance = loadingDialog.create()
        loadingDialogInstance.setCancelable(false)
        loadingDialogInstance.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))


        binding.loginBtn.setOnClickListener {
            val email1 = binding.emailEditText.text.toString()
            val pass1 = binding.passwordEditText.text.toString()
            if (editTextValidation(email1, "Email") && editTextValidation(pass1, "Password")) {


                loadingDialogInstance.show()
                //Sign in with firebase
                auth.signInWithEmailAndPassword(
                    binding.emailEditText.text.toString().trim(),
                    binding.passwordEditText.text.toString().trim()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success")
                            loadingDialogInstance.dismiss()

                            startActivity(Intent(this, MainActivity::class.java))

                            Toast.makeText(
                                baseContext, "LogIn Success...",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.exception)

                            loadingDialogInstance.dismiss()
                            Toast.makeText(
                                baseContext, task.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
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


}