package com.example.securevote.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.securevote.R
import com.example.securevote.models.Election
import com.example.securevote.models.Party
import com.example.securevote.models.Vote
import java.text.SimpleDateFormat
import java.util.*

class VoteAdapter(
    private val votes: List<Vote>,
    private val elections: Map<String, Election>,
    private val parties: Map<String, Party>
) : RecyclerView.Adapter<VoteAdapter.VoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vote, parent, false)
        return VoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {
        holder.bind(votes[position])
    }

    override fun getItemCount() = votes.size

    inner class VoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val electionName: TextView = itemView.findViewById(R.id.electionName)
        private val selectedParty: TextView = itemView.findViewById(R.id.selectedParty)
        private val dateTime: TextView = itemView.findViewById(R.id.dateTime)

        fun bind(vote: Vote) {
            // Get election and party names
            val election = elections[vote.electionId]
            val party = parties[vote.partyId]

            electionName.text = election?.name ?: "Unknown Election"
            selectedParty.text = party?.name ?: "Unknown Party"

            // Format date
            val dateFormat = SimpleDateFormat("dd-MMMM-yyyy-HH:mm", Locale.US)
            dateTime.text = dateFormat.format(Date(vote.timestamp))
        }
    }
}
