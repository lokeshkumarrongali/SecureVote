package com.example.securevote

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.securevote.adapters.VoteAdapter
import com.example.securevote.utils.PrefsManager

class MyVotesActivity : AppCompatActivity() {

    private lateinit var prefsManager: PrefsManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_votes)

        prefsManager = PrefsManager(this)

        // Setup toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Initialize views
        recyclerView = findViewById(R.id.votesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        emptyTextView = findViewById(R.id.emptyTextView)

        // Check preference
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val showPastVotes = sharedPreferences.getBoolean("show_past_votes", true)

        if (showPastVotes) {
            // Show vote history
            val votes = prefsManager.getVotes()
            val elections = prefsManager.getElections().associateBy { it.id }
            val parties = prefsManager.getParties().associateBy { it.id }
            val adapter = VoteAdapter(votes, elections, parties)
            recyclerView.adapter = adapter

            recyclerView.visibility = View.VISIBLE
            emptyTextView.visibility = View.GONE
        } else {
            // Hide RecyclerView and show message
            recyclerView.visibility = View.GONE
            emptyTextView.visibility = View.VISIBLE
        }
        val nnextvote: ImageButton = findViewById(R.id.imageButton)
        nnextvote.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
