package com.example.securevote

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.securevote.adapters.ElectionAdapter
import com.example.securevote.utils.PrefsManager

class MainActivity : AppCompatActivity() {

    private lateinit var prefsManager: PrefsManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var analysisButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefsManager = PrefsManager(this)

        recyclerView = findViewById(R.id.electionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get elections from SharedPreferences
        val elections = prefsManager.getElections()

        // Set up adapter
        val adapter = ElectionAdapter(elections) { election ->
            // Handle election click - open details screen
            val intent = Intent(this, ElectionDetailsActivity::class.java).apply {
                putExtra("ELECTION_ID", election.id)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter

//        // Initialize analysis button
//        analysisButton = findViewById(R.id.analysisButton)
//        analysisButton.setOnClickListener {
//            val intent = Intent(this, AnalysisActivity::class.java)
//            startActivity(intent)
//        }
        val settingsButton: ImageButton = findViewById(R.id.settingsbutton)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        val analysisButt: ImageButton = findViewById(R.id.analysisButton)
        analysisButt.setOnClickListener {
            val intent = Intent(this, AnalysisActivity::class.java)
            startActivity(intent)
        }


    }
}
