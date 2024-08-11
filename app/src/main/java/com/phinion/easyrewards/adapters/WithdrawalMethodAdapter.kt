package com.phinion.easyrewards.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phinion.easyrewards.R
import com.phinion.easyrewards.WithdrawalItemOnClick
import com.phinion.easyrewards.models.WithdrawalMethod


class WithdrawalMethodAdapter(private val requireContext: Context, private val withdrawalList: List<WithdrawalMethod>,
private val withdrawalItemOnClick: WithdrawalItemOnClick) :
    RecyclerView.Adapter<WithdrawalMethodAdapter.WithdrawalMethodViewHolder>() {


    class WithdrawalMethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WithdrawalMethodViewHolder {
        val view = LayoutInflater.from(requireContext).inflate(R.layout.withdrawal_method_layout, parent, false)
        return WithdrawalMethodViewHolder(view)
    }

    override fun onBindViewHolder(holder: WithdrawalMethodViewHolder, position: Int) {

        val platformText = holder.itemView.findViewById<TextView>(R.id.platform)

        val amountOfCoinsText = holder.itemView.findViewById<TextView>(R.id.amount_of_coins)

        val coinValue = holder.itemView.findViewById<TextView>(R.id.coin_value)

        val withdrawalMethod = withdrawalList[position]

        platformText.text = withdrawalMethod.platform
        amountOfCoinsText.text = withdrawalMethod.amountOfCoins.toString()
        coinValue.text = withdrawalMethod.coinValue

        holder.itemView.setOnClickListener {
            withdrawalItemOnClick.WithdrawalItemOnClickListener(position)
        }


    }

    override fun getItemCount(): Int {
        return withdrawalList.size
    }
}