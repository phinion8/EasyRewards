package com.phinion.easyrewards

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phinion.easyrewards.databinding.FragmentProfileBinding
import com.phinion.easyrewards.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        mAuth = Firebase.auth
        database = Firebase.firestore

        binding.withdrawalHistoryBtn.setOnClickListener {
            startActivity(Intent(requireContext(), WithdrawalHistoryActivity::class.java))
        }

        mAuth.currentUser?.let {
            database.collection("users")
                .document(it.uid)
                .addSnapshotListener{value, error->
                    val user: User = value!!.toObject(User::class.java)!!

                    binding.userName.text = user.name
                    binding.userEmail.text = user.email

                }
        }

        binding.logOutBtn.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(requireContext(), LogInActivity::class.java))
            requireActivity().finish()

        }

        binding.historyBtn.setOnClickListener{

            startActivity(Intent(requireContext(), EarningHistoryActivity::class.java))
        }




        return binding.root
    }



}