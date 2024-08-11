package com.phinion.easyrewards.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phinion.easyrewards.R
import com.phinion.easyrewards.models.WithdrawalHistory

class WithdrawalHistoryAdapter(
    private val context: Context, private val withdrawalHistoryList: ArrayList<WithdrawalHistory>
) : RecyclerView.Adapter<WithdrawalHistoryAdapter.WithdrawalHistoryViewHolder>() {

    class WithdrawalHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WithdrawalHistoryViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.withdrawal_history_layout, parent, false)
        return WithdrawalHistoryViewHolder(view)

    }

    override fun onBindViewHolder(holder: WithdrawalHistoryViewHolder, position: Int) {

        val earningMethod = holder.itemView.findViewById<TextView>(R.id.earning_method)
        val earningAmount = holder.itemView.findViewById<TextView>(R.id.earning_amount)
        val time = holder.itemView.findViewById<TextView>(R.id.time)
        val status = holder.itemView.findViewById<TextView>(R.id.status)

        val withdrawalHistory: WithdrawalHistory = withdrawalHistoryList[position]



        earningMethod.text = withdrawalHistory.title
        earningAmount.text = withdrawalHistory.amountOfCoin.toString()
        time.text = withdrawalHistory.time.toString().subSequence(0, 20)
        status.text = withdrawalHistory.status



    }

    override fun getItemCount(): Int {
        return withdrawalHistoryList.size
    }
}