package com.example.securevote.models

// Party.kt
data class Party(
    val id: String,
    val name: String,
    val shortName: String,
    val leader: String,
    val logoResId: Int,
    val description: String,       // Add party description
    val foundingYear: Int,         // Add founding year
    val ideology: String,          // Add political ideology
    val history: String            // Add historical information
)
