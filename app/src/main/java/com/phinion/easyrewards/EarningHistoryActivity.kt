package com.phinion.easyrewards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.easyrewards.adapters.EarningHistoryAdapter
import com.phinion.easyrewards.databinding.ActivityEarningHistoryBinding
import com.phinion.easyrewards.models.EarningHistory

class EarningHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEarningHistoryBinding
    private lateinit var adapter: EarningHistoryAdapter
    private lateinit var database: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEarningHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn5.setOnClickListener {
            finish()
        }

        database = Firebase.firestore
        mAuth = Firebase.auth

        val earningHistoryList: ArrayList<EarningHistory> = ArrayList()

        adapter = EarningHistoryAdapter(this, earningHistoryList)

        mAuth.uid?.let {
            database.collection("users")
                .document(it)
                .collection("history")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener{value, error->
                    if (value != null) {
                        for (snapshot in value.documents){

                            val earningHistory: EarningHistory? = snapshot.toObject(EarningHistory::class.java)
                            if (earningHistory != null) {
                                earningHistoryList.add(earningHistory)
                            }
                        }
                        adapter.notifyDataSetChanged()
                        if(adapter.itemCount == 0){
                            binding.noDataText.visibility = View.VISIBLE
                        }else{
                            binding.noDataText.visibility = View.GONE
                        }
                    }
                }
        }

        Log.d("history", earningHistoryList.toString())
        binding.historyList.adapter = adapter
        binding.historyList.layoutManager = LinearLayoutManager(this)

    }
}