package com.example.ktp.ui.adapters

import android.animation.LayoutTransition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.ktp.R
import com.example.ktp.model.KptRecord
import com.example.ktp.utils.toDate2DigitsString
import com.google.android.material.progressindicator.CircularProgressIndicator

class KptRecordsAdapter(val kptRecords: List<KptRecord>,
                        val expand: (textView: TextView, imageButton: ImageButton) -> Unit,
                        val onItemClick: (kptRecordId: Int) -> Unit
)
    : RecyclerView.Adapter<KptRecordsAdapter.KptRecordsViewHolder>() {

    inner class KptRecordsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(kptRecord: KptRecord){
            val constraintLayout = itemView as ConstraintLayout
            val mainTextView = itemView.findViewById<TextView>(R.id.text)
            val dateTextView = itemView.findViewById<TextView>(R.id.date)
            val expandButton = itemView.findViewById<ImageButton>(R.id.expand_button)
            val progress = itemView.findViewById<CircularProgressIndicator>(R.id.progress)

            constraintLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

            mainTextView.text = kptRecord.situation
            dateTextView.text = kptRecord.changeDate.toDate2DigitsString()
            expandButton.setOnClickListener {
                expand(mainTextView, expandButton)
            }

            constraintLayout.setOnClickListener {
                onItemClick(kptRecord.id!!)
            }

            progress.progress = kptRecord.truthOfThought?.toInt() ?: 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KptRecordsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.el1_1, parent, false)

        return KptRecordsViewHolder(view)
    }

    override fun onBindViewHolder(holder: KptRecordsViewHolder, position: Int) {
        holder.bind(kptRecords[position])
    }

    override fun getItemCount(): Int = kptRecords.size
}

