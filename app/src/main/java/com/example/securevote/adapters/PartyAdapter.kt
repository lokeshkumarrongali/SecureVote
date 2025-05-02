package com.example.securevote.adapters

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.securevote.R
import com.example.securevote.models.Party

class PartyAdapter(
    private val parties: List<Party>,
    private val onPartyClick: (Party) -> Unit
) : RecyclerView.Adapter<PartyAdapter.PartyViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_party, parent, false)
        return PartyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartyViewHolder, position: Int) {
        holder.bind(parties[position], position == selectedPosition)
    }

    override fun getItemCount() = parties.size

    inner class PartyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val partyLogo: ImageView = itemView.findViewById(R.id.partyLogo)
        private val partyName: TextView = itemView.findViewById(R.id.partyName)
        private val partyLeader: TextView = itemView.findViewById(R.id.partyLeader)
        private val radioButton: RadioButton = itemView.findViewById(R.id.radioButton)

        fun bind(party: Party, isSelected: Boolean) {
            partyLogo.setImageResource(party.logoResId)
            partyName.text = party.name
            partyLeader.text = party.leader
            radioButton.isChecked = isSelected

            itemView.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)

                showPartyDetails(party)
                onPartyClick(party)
            }
        }

        private fun showPartyDetails(party: Party) {
            val context = itemView.context
            val dialog = Dialog(context).apply {
                setContentView(R.layout.party_details_dialog)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                findViewById<ImageView>(R.id.partyLogo).setImageResource(party.logoResId)
                findViewById<TextView>(R.id.partyName).text = party.name
                findViewById<TextView>(R.id.partyInfo).text =
                    "Founded: ${party.foundingYear}\n" +
                            "Ideology: ${party.ideology}\n" +
                            "Current Leader: ${party.leader}"

                findViewById<TextView>(R.id.partyHistory).text = party.history
            }
            dialog.show()
        }
    }
}
