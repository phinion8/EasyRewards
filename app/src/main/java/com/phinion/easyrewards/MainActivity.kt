package com.phinion.easyrewards

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.phinion.easyrewards.databinding.ActivityMainBinding
import com.phinion.easyrewards.databinding.ExitAppDialogBinding
import com.phinion.easyrewards.utils.CheckForInternet

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var exitAppDialogBinding: ExitAppDialogBinding
    private lateinit var eDialog: AlertDialog
    private lateinit var checkForInternet: CheckForInternet


    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkForInternet = CheckForInternet(this)


        navController= Navigation.findNavController(this,R.id.navHostFragment)
        setupWithNavController(binding.bottomNav,navController)

        exitAppDialogBinding = ExitAppDialogBinding.inflate(LayoutInflater.from(this))
        eDialog = AlertDialog.Builder(this)
            .setView(exitAppDialogBinding.root)
            .setCancelable(false)
            .create()
        exitAppDialogBinding.yesButton.setOnClickListener { finishAffinity() }
        exitAppDialogBinding.noButton.setOnClickListener { eDialog.dismiss() }
        eDialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))


    }

    override fun onBackPressed() {
        eDialog.show()
    }


}