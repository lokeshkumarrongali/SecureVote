package com.example.securevote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.securevote.databinding.ItemPollBinding

class PollsAdapter(
    private val polls: List<Poll>,
    private val onOptionSelected: (Poll, Int) -> Unit
) : RecyclerView.Adapter<PollsAdapter.PollViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PollViewHolder {
        val binding = ItemPollBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PollViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PollViewHolder, position: Int) {
        holder.bind(polls[position])
    }

    override fun getItemCount(): Int = polls.size

    inner class PollViewHolder(private val binding: ItemPollBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val optionButtons = listOf(
            binding.option1,
            binding.option2,
            binding.option3,
            binding.option4,

        )

        fun bind(poll: Poll) {
            binding.tvPollQuestion.text = poll.question
            binding.tvVoteCount.text = binding.root.context.resources.getString(
                R.string.vote_count_format,
                poll.totalVotes
            )

            // Configure option buttons
            configureOptions(poll)
        }

        private fun configureOptions(poll: Poll) {
            // First, hide all option buttons
            optionButtons.forEach { it.visibility = View.GONE }

            // Then set up and show only the options we need
            poll.options.forEachIndexed { index, option ->
                if (index < optionButtons.size) {
                    val button = optionButtons[index]
                    button.visibility = View.VISIBLE
                    button.text = option.text

                    // Calculate and show percentage if applicable
                    if (poll.totalVotes > 0) {
                        val percentage = (option.voteCount * 100) / poll.totalVotes
                        button.text = "${option.text} ($percentage%)"
                    }

                    button.setOnClickListener {
                        onOptionSelected(poll, index)

                        // Update all buttons to show new percentages
                        configureOptions(poll)
                    }
                }
            }
        }
    }
}