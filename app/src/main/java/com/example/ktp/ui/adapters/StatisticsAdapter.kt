package com.example.ktp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.ktp.R

class StatisticsAdapter(val items: List<String>, val itemLayoutId: Int) : RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>() {

    inner class StatisticsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: String){
            val toggleButton = itemView.findViewById<ToggleButton>(R.id.button)
            with(item){
                toggleButton.text = this
                toggleButton.textOff = this
                toggleButton.textOn = this
                toggleButton.isChecked = false
                toggleButton.isEnabled = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(itemLayoutId, parent, false)

        return StatisticsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}