package com.phinion.easyrewards.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phinion.easyrewards.R
import com.phinion.easyrewards.models.EarningHistory

class EarningHistoryAdapter(
    private val context: Context, private val earningHistoryList: ArrayList<EarningHistory>
) : RecyclerView.Adapter<EarningHistoryAdapter.EarningHistoryViewHolder>() {

    class EarningHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarningHistoryViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.earning_history_layout, parent, false)
        return EarningHistoryViewHolder(view)

    }

    override fun onBindViewHolder(holder: EarningHistoryViewHolder, position: Int) {

        val earningMethod = holder.itemView.findViewById<TextView>(R.id.earning_method)
        val earningAmount = holder.itemView.findViewById<TextView>(R.id.earning_amount)
        val time = holder.itemView.findViewById<TextView>(R.id.time)

        val earningHistory: EarningHistory = earningHistoryList[position]


        earningMethod.text = earningHistory.earningMethod
        earningAmount.text = earningHistory.amountEarned.toString()
        time.text = earningHistory.date.toString().subSequence(0, 20)


    }

    override fun getItemCount(): Int {
        return earningHistoryList.size
    }
}