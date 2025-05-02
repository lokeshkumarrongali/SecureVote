package com.example.securevote.models

data class Election(
    val id: String,
    val name: String,
    val type: String,
    val startDate: Long,
    val endDate: Long,
    val location: String,
    val description: String = ""
)

enum class ElectionType {
    LOK_SABHA,
    MUNICIPAL_CORPORATION,
    PANCHAYAT,
    STATE_ASSEMBLY
}
