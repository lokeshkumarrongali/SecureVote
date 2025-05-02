package com.example.securevote.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.securevote.R
import com.example.securevote.models.Election
import java.text.SimpleDateFormat
import java.util.*

class ElectionAdapter(
    private val elections: List<Election>,
    private val onElectionClick: (Election) -> Unit
) : RecyclerView.Adapter<ElectionAdapter.ElectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_election, parent, false)
        return ElectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        holder.bind(elections[position])
    }

    override fun getItemCount() = elections.size

    inner class ElectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val electionName: TextView = itemView.findViewById(R.id.electionName)
        private val electionTypeInitial: TextView = itemView.findViewById(R.id.electionTypeInitial)
        private val startDate: TextView = itemView.findViewById(R.id.startDate)
        private val endDate: TextView = itemView.findViewById(R.id.endDate)

        fun bind(election: Election) {
            electionName.text = election.name
            electionTypeInitial.text = election.type.first().toString()

            // Format dates
            val dateFormat = SimpleDateFormat("dd MMMM,yyyy HH:mm", Locale.US)
            startDate.text = dateFormat.format(Date(election.startDate))
            endDate.text = dateFormat.format(Date(election.endDate))

            // Set click listener
            itemView.setOnClickListener {
                onElectionClick(election)
            }
        }
    }
}
