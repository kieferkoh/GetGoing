package com.example.getgoing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BillsDisplayAdapter(
    private val eventList: List<Event>) :
    RecyclerView.Adapter<BillsDisplayAdapter.BillViewHolder>() {

    inner class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventNameTextView: TextView = itemView.findViewById(R.id.eventNameTextView_bill)
        val amountTextView: TextView = itemView.findViewById(R.id.totalAmountTextView_bill)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.bill_event_item, parent, false)
        return BillViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val currentItem = eventList[position]
        holder.eventNameTextView.text = currentItem.name
        holder.amountTextView.text = currentItem.totalAmount.toString()
    }

    override fun getItemCount() = eventList.size
}
