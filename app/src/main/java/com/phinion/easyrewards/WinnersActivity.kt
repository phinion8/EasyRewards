package com.phinion.easyrewards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.easyrewards.adapters.WinnersAdapter
import com.phinion.easyrewards.databinding.ActivityWinnersBinding
import com.phinion.easyrewards.models.Winner
import kotlin.collections.ArrayList

class WinnersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWinnersBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: WinnersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWinnersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = Firebase.auth
        database = Firebase.firestore


        binding.backBtn.setOnClickListener{
            finish()
        }



        val allWinnersList: ArrayList<Winner> = ArrayList<Winner>()





        database.collection("luckyDrawSystem")
            .document("announceWinners")
            .collection("winners")
            .addSnapshotListener { value, error ->
                allWinnersList.clear()
                for (snapshot in value!!.documents) {
                    val winner: Winner? =
                        snapshot.toObject(Winner::class.java)


                    if (winner != null) {
                        allWinnersList.add(winner)
                    }
                }
                adapter.notifyDataSetChanged()
            }

        adapter = WinnersAdapter(this, allWinnersList)
        binding.winnersList.adapter = adapter
        binding.winnersList.layoutManager = LinearLayoutManager(this)




    }
}



