package com.phinion.adminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.phinion.adminapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dailyRewards.setOnClickListener {
            startActivity(Intent(this, DailyRewards::class.java))
        }

        binding.spinWheel.setOnClickListener {

            startActivity(Intent(this, Spin_Wheel::class.java))
        }

        binding.scratchCard.setOnClickListener {
            startActivity(Intent(this , Scratch_Card::class.java))

        }

        binding.refer.setOnClickListener {
            startActivity(Intent(this, Refer::class.java))
        }

        binding.luckyNumber.setOnClickListener {
            startActivity(Intent(this, LuckyNumber::class.java))

        }



        binding.updateContactBtn.setOnClickListener {
            startActivity(Intent(this, UpdateContactActivity::class.java))
        }
    }

}