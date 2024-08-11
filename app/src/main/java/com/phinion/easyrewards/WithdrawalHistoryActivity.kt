package com.phinion.easyrewards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.easyrewards.adapters.WithdrawalHistoryAdapter
import com.phinion.easyrewards.databinding.ActivityWithdrawalHistoryBinding
import com.phinion.easyrewards.models.WithdrawalHistory

class WithdrawalHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWithdrawalHistoryBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var adapter: WithdrawalHistoryAdapter
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawalHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.firestore
        mAuth = Firebase.auth

        binding.backBtn5.setOnClickListener {
            finish()
        }

        database = Firebase.firestore
        mAuth = Firebase.auth

        val withdrawalHistoryList: ArrayList<WithdrawalHistory> = ArrayList()

        adapter = WithdrawalHistoryAdapter(this, withdrawalHistoryList)

        mAuth.uid?.let {
            database.collection("users")
                .document(it)
                .collection("withdrawalHistory")
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener{value, error->
                    if (value != null) {
                        for (snapshot in value.documents){

                            val withdrawalHistory: WithdrawalHistory? = snapshot.toObject(WithdrawalHistory::class.java)
                            if (withdrawalHistory != null) {
                                withdrawalHistoryList.add(withdrawalHistory)
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

        binding.withdrawalHistoryList.adapter = adapter
        binding.withdrawalHistoryList.layoutManager = LinearLayoutManager(this)

    }




    }
