package com.example.securevote

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.securevote.adapters.PartyAdapter
import com.example.securevote.models.Vote
import com.example.securevote.utils.PrefsManager
import java.util.*
import java.util.concurrent.Executor

class PartySelectionActivity : AppCompatActivity() {

    private lateinit var prefsManager: PrefsManager
    private var selectedPartyId: String? = null
    private lateinit var electionId: String
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var executor: Executor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_party_selection)

        prefsManager = PrefsManager(this)

        // Setup toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Get election ID from intent
        electionId = intent.getStringExtra("ELECTION_ID") ?: return

        // Get election details
        val election = prefsManager.getElectionById(electionId) ?: return

        // Set election title
        val titleText = findViewById<TextView>(R.id.electionTitle)
        titleText.text = election.name
        supportActionBar?.title = election.name

        // Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.partiesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val parties = prefsManager.getParties()
        val adapter = PartyAdapter(parties) { party ->
            selectedPartyId = party.id
        }
        recyclerView.adapter = adapter

        // Setup biometric prompt
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                processCastVote()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Confirm your identity to cast vote")
            .setNegativeButtonText("Cancel")
            .build()

        // Setup confirm button
        val confirmButton = findViewById<ImageView>(R.id.voteConfirmButton)
        confirmButton.setOnClickListener {
            if (selectedPartyId != null) {
                if (checkBiometricPreference() && checkBiometricSupport()) {
                    biometricPrompt.authenticate(promptInfo)
                } else {
                    processCastVote()
                }
            } else {
                Toast.makeText(this, "Please select a party", Toast.LENGTH_SHORT).show()
            }
        }


        // Setup cancel button
        val cancelButton = findViewById<ImageView>(R.id.voteCancelButton)
        cancelButton.setOnClickListener {
            finish()
        }
        val nnextvote: ImageButton = findViewById(R.id.nextvote)
        nnextvote.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun processCastVote() {
        val vote = Vote(
            id = UUID.randomUUID().toString(),
            electionId = electionId,
            partyId = selectedPartyId!!,
            timestamp = System.currentTimeMillis()
        )

        prefsManager.addVote(vote)

        AlertDialog.Builder(this)
            .setTitle("Vote Confirmation")
            .setMessage("Your vote has been recorded successfully!")
            .setPositiveButton("View My Votes") { _, _ ->
                startActivity(Intent(this, MyVotesActivity::class.java))
                finish()
            }
            .setNegativeButton("Return to Elections") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun checkBiometricPreference(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getBoolean("require_biometric", true)
    }

    private fun checkBiometricSupport(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }


}
