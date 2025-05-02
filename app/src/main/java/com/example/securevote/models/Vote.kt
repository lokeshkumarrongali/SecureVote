package com.example.securevote.models

// Vote.kt
data class Vote(
        val id: String,
        val electionId: String,
        val partyId: String,
        val timestamp: Long
)
