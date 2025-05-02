package com.example.securevote

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.securevote.models.Election
import com.example.securevote.models.Party
import com.example.securevote.utils.PrefsManager
import java.text.SimpleDateFormat
import java.util.*

class ElectionDetailsActivity : AppCompatActivity() {

    private lateinit var prefsManager: PrefsManager
    private lateinit var election: Election

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_election_details)

        prefsManager = PrefsManager(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val electionId = intent.getStringExtra("ELECTION_ID") ?: return
        val election = prefsManager.getElectionById(electionId) ?: return
        this.election = election

        val titleText = findViewById<TextView>(R.id.electionTitle)
        val startDateText = findViewById<TextView>(R.id.startDateText)
        val endDateText = findViewById<TextView>(R.id.endDateText)
        val descriptionText = findViewById<TextView>(R.id.descriptionText)
        val voteButton = findViewById<Button>(R.id.voteButton)
        val partiesContainer = findViewById<LinearLayout>(R.id.partiesContainer)

        titleText.text = election.name
        supportActionBar?.title = election.name

        val dateFormat = SimpleDateFormat("dd MMMM, yyyy HH:mm", Locale.US)
        startDateText.text = dateFormat.format(Date(election.startDate))
        endDateText.text = dateFormat.format(Date(election.endDate))
        descriptionText.text = election.description

        voteButton.setOnClickListener {
            val intent = Intent(this, PartySelectionActivity::class.java).apply {
                putExtra("ELECTION_ID", election.id)
            }
            startActivity(intent)
        }

        displayParties(partiesContainer)
    }

    private fun displayParties(container: LinearLayout) {
        val parties = prefsManager.getParties()

        parties.take(4).forEach { party ->
            val partyView = LayoutInflater.from(this)
                .inflate(R.layout.item_party_icon, container, false)

            partyView.findViewById<ImageView>(R.id.partyIcon).setImageResource(party.logoResId)
            partyView.findViewById<TextView>(R.id.partyShortName).text = party.shortName

            partyView.setOnClickListener {
                showPartyDetailsDialog(party)
            }

            container.addView(partyView)
        }
    }

    private fun showPartyDetailsDialog(party: Party) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_party_details, null, false)

        val logo = dialogView.findViewById<ImageView>(R.id.partyLogo)
        val name = dialogView.findViewById<TextView>(R.id.partyName)
        val leader = dialogView.findViewById<TextView>(R.id.partyLeader)
        val description = dialogView.findViewById<TextView>(R.id.partyDescription)

        logo.setImageResource(party.logoResId)
        name.text = party.name
        leader.text = "Leader: ${party.leader}"
        description.text = party.description // Make sure your Party model has a description field

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Close", null)
            .show()
    }

}
