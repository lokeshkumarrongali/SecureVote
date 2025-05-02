package com.example.securevote.utils

import android.content.Context
import com.example.securevote.R
import com.example.securevote.models.Election
import com.example.securevote.models.ElectionType
import com.example.securevote.models.Party
import java.text.SimpleDateFormat
import java.util.*

object DataGenerator {

    fun populateSampleData(context: Context) {
        val prefsManager = PrefsManager(context)

        if (prefsManager.getParties().isEmpty()) {
            prefsManager.saveParties(createSampleParties())
        }

        if (prefsManager.getElections().isEmpty()) {
            prefsManager.saveElections(createSampleElections())
        }
    }

    private fun createSampleParties(): List<Party> {
        return listOf(
            Party(
                id = "1",
                name = "Aam Aadami Party (AAP)",
                shortName = "AAP",
                leader = "Arvind Kejriwal",
                logoResId = R.drawable.party_aap,
                description = "Anti-corruption party focused on grassroots democracy.",
                foundingYear = 2012,
                ideology = "Anti-corruption, Social liberalism",
                history = "Founded during India Against Corruption movement."
            ),
            Party(
                id = "2",
                name = "Bharatiya Janata Party (BJP)",
                shortName = "BJP",
                leader = "Narendra Modi",
                logoResId = R.drawable.party_bjp,
                description = "Right-wing party promoting Hindutva ideology.",
                foundingYear = 1980,
                ideology = "Hindutva, Conservatism",
                history = "Emerged from Bharatiya Jana Sangh."
            ),
            Party(
                id = "3",
                name = "Indian National Congress (INC)",
                shortName = "INC",
                leader = "Mallikarjun Kharge",
                logoResId = R.drawable.party_inc,
                description = "One of India's oldest parties advocating secularism and democracy.",
                foundingYear = 1885,
                ideology = "Secularism, Social Democracy",
                history = "Led India's independence movement; ruled for decades post-independence."
            ),
            Party(
                id = "4",
                name = "Telugu Desam Party (TDP)",
                shortName = "TDP",
                leader = "N. Chandrababu Naidu",
                logoResId = R.drawable.party_telgu,
                description = "Regional party in Andhra Pradesh and Telangana.",
                foundingYear = 1982,
                ideology = "Regionalism, Developmentalism",
                history = "Founded by N.T. Rama Rao to challenge Congress dominance in Andhra Pradesh."
            )
        )
    }

    private fun createSampleElections(): List<Election> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        return listOf(
            Election(
                id = "1",
                name = "Andhra Pradesh Assembly Elections 2024",
                type = ElectionType.STATE_ASSEMBLY.name,
                startDate = dateFormat.parse("10/05/2024 08:00")?.time ?: Date().time,
                endDate = dateFormat.parse("10/05/2024 18:00")?.time ?: Date().time,
                location = "Andhra Pradesh",
                description = "State Legislative Assembly elections for Andhra Pradesh."
            ),
            Election(
                id = "2",
                name = "General Elections 2024",
                type = ElectionType.LOK_SABHA.name,
                startDate = dateFormat.parse("20/04/2024 07:00")?.time ?: Date().time,
                endDate = dateFormat.parse("20/04/2024 19:00")?.time ?: Date().time,
                location = "All India",
                description = "Lok Sabha elections to elect Members of Parliament."
            )
        )
    }

}
