package com.example.securevote

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.securevote.utils.PrefsManager

class AnalysisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        val prefsManager = PrefsManager(this)
        val votes = prefsManager.getVotes()
        val parties = prefsManager.getParties()

        val eligibleVoters = 1000 // Replace with your actual count
        val turnout = if (eligibleVoters > 0)
            (votes.size * 100.0 / eligibleVoters)
        else 0.0
        findViewById<TextView>(R.id.turnoutText).text = "Turnout: %.1f%%".format(turnout)

        val partyVoteCounts = parties.associate { party ->
            party.name to votes.count { it.partyId == party.id }
        }
        val totalVotes = votes.size.coerceAtLeast(1)

        val container = findViewById<LinearLayout>(R.id.voteShareContainer)
        container.removeAllViews()

        partyVoteCounts.forEach { (partyName, count) ->
            val row = layoutInflater.inflate(R.layout.item_vote_share, container, false)
            row.findViewById<TextView>(R.id.partyName).text = partyName
            row.findViewById<TextView>(R.id.voteCount).text = "$count"
            row.findViewById<ProgressBar>(R.id.voteProgress).apply {
                max = totalVotes
                progress = count
            }
            container.addView(row)
        }
    }
}
