package com.example.securevote.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.securevote.models.Election
import com.example.securevote.models.Party
import com.example.securevote.models.Vote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class PrefsManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SecureVotePrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Keys for our SharedPreferences
    companion object {
        private const val ELECTIONS_KEY = "elections"
        private const val PARTIES_KEY = "parties"
        private const val VOTES_KEY = "votes"
    }

    // Elections Methods
    fun saveElections(elections: List<Election>) {
        val json = gson.toJson(elections)
        sharedPreferences.edit().putString(ELECTIONS_KEY, json).apply()
    }

    fun getElections(): List<Election> {
        val json = sharedPreferences.getString(ELECTIONS_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Election>>() {}.type
            gson.fromJson(json, type)
        } else {
            // Return empty list if no data found
            emptyList()
        }
    }

    // Parties Methods
    fun saveParties(parties: List<Party>) {
        val json = gson.toJson(parties)
        sharedPreferences.edit().putString(PARTIES_KEY, json).apply()
    }

    fun getParties(): List<Party> {
        val json = sharedPreferences.getString(PARTIES_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Party>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    // Votes Methods
    fun saveVotes(votes: List<Vote>) {
        val json = gson.toJson(votes)
        sharedPreferences.edit().putString(VOTES_KEY, json).apply()
    }

    fun getVotes(): List<Vote> {
        val json = sharedPreferences.getString(VOTES_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Vote>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun addVote(vote: Vote) {
        val votes = getVotes().toMutableList()
        votes.add(vote)
        saveVotes(votes)
    }

    // Helper methods
    fun getElectionById(id: String): Election? {
        return getElections().find { it.id == id }
    }

    fun getPartyById(id: String): Party? {
        return getParties().find { it.id == id }
    }

    fun hasVotedInElection(electionId: String): Boolean {
        return getVotes().any { it.electionId == electionId }
    }
}
