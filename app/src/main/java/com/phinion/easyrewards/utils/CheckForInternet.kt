package com.phinion.easyrewards.utils

import android.R
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.phinion.easyrewards.MainActivity
import com.phinion.easyrewards.databinding.NetworkCheckDialogBinding


class CheckForInternet(private val context: Context) {
    private lateinit var networkCheckDialog: AlertDialog
    private var networkCheckDialogBinding: NetworkCheckDialogBinding = NetworkCheckDialogBinding.inflate(LayoutInflater.from(context))
    fun checkInternetConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return (connectivityManager.activeNetworkInfo != null
                && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting)
    }
    init {
        networkCheckDialog = AlertDialog.Builder(context)
            .setView(networkCheckDialogBinding.root)
            .setCancelable(false)
            .create()
        networkCheckDialog.window?.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent)))
        if (!checkInternetConnection()){
            networkCheckDialog.show()
            networkCheckDialogBinding.retryBtn.setOnClickListener {
                context.startActivity(Intent(context, MainActivity::class.java))
            }
        }else{
            networkCheckDialog.dismiss()
        }
    }
}