package com.example.securevote


data class Poll(
    val id: Int,
    val question: String,
    val options: List<PollOption>,
    var totalVotes: Int = options.sumOf { it.voteCount }
)

data class PollOption(
    val text: String,
    var voteCount: Int = 0
)