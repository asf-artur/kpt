package com.example.ktp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.ktp.R
import com.example.ktp.model.repository.StringBoolean

class AddKptThoughtAdapter(val items: List<StringBoolean>, val onToggleButtonClick: (item: String, selected: Boolean) -> Unit) : RecyclerView.Adapter<AddKptThoughtAdapter.AddKptThoughtViewHolder>() {

    inner class AddKptThoughtViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: StringBoolean, position: Int){
            val toggleButton = itemView.findViewById<ToggleButton>(R.id.button)
            with(item.text){
                toggleButton.text = this
                toggleButton.textOff = this
                toggleButton.textOn = this
                toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
                    onToggleButtonClick(this, isChecked)
                }
            }
            if(item.checked){
                toggleButton.isChecked = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddKptThoughtViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.el2, parent, false)

        return AddKptThoughtViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) 0 else 1
    }

    override fun onBindViewHolder(holder: AddKptThoughtViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size
}