package com.khenikumar.wantuc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.khenikumar.wantuc.R
import com.khenikumar.wantuc.models.Winner
import com.khenikumar.wantuc.models.WithdrawalMethod

class WinnersAdapter(private val requireContext: Context, private val winnersList: List<Winner>): RecyclerView.Adapter<WinnersAdapter.WinnersViewHolder>() {

    inner class WinnersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WinnersViewHolder {
        val view = LayoutInflater.from(requireContext).inflate(R.layout.winners_layout, parent, false)
        return WinnersViewHolder(view)
    }

    override fun onBindViewHolder(holder: WinnersViewHolder, position: Int) {
        val name = holder.itemView.findViewById<TextView>(R.id.name)

        name.text = winnersList[position].name

    }

    override fun getItemCount(): Int {
        return winnersList.size
    }
}