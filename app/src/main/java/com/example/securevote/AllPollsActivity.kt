package com.example.securevote

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.securevote.databinding.ActivityAllPollsBinding

class AllPollsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllPollsBinding
    private lateinit var pollsAdapter: PollsAdapter
    private val polls = mutableListOf<Poll>()

    // Simulating network request to fetch polls
    private var isLoading = false
    private var currentPage = 1
    private val itemsPerPage = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllPollsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadInitialPolls()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.searchIcon.setOnClickListener {
            // Handle search functionality
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
            // In a real app, you might launch a search activity or show a search dialog
        }
    }

    private fun setupRecyclerView() {
        pollsAdapter = PollsAdapter(polls, ::handleVoteSelection)

        binding.pollsRecyclerView.apply {
            adapter = pollsAdapter
            layoutManager = LinearLayoutManager(this@AllPollsActivity)

            // Add scroll listener for pagination
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                        loadMorePolls()
                    }
                }
            })
        }
    }

    private fun loadInitialPolls() {
        showLoading(true)
        // Simulate network delay
        binding.root.postDelayed({
            polls.addAll(generateDummyPolls(currentPage, itemsPerPage))
            pollsAdapter.notifyDataSetChanged()
            showLoading(false)
        }, 1500)
    }

    private fun loadMorePolls() {
        if (isLoading) return

        isLoading = true
        showLoading(true)
        currentPage++

        // Simulate network delay for pagination
        binding.root.postDelayed({
            val startPosition = polls.size
            polls.addAll(generateDummyPolls(currentPage, itemsPerPage))
            pollsAdapter.notifyItemRangeInserted(startPosition, itemsPerPage)
            isLoading = false
            showLoading(false)
        }, 1500)
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun handleVoteSelection(poll: Poll, optionIndex: Int) {
        // In a real app, you would send this vote to your backend
        // For now, we'll just update the UI
        poll.options[optionIndex].voteCount++
        poll.totalVotes++
        pollsAdapter.notifyItemChanged(polls.indexOf(poll))

        Toast.makeText(
            this,
            "Voted for: ${poll.options[optionIndex].text}",
            Toast.LENGTH_SHORT
        ).show()
    }

    // Helper method to generate dummy polls for demonstration
    private fun generateDummyPolls(page: Int, count: Int): List<Poll> {
        val dummyPolls = mutableListOf<Poll>()
        val startId = (page - 1) * count

        for (i in startId until (startId + count)) {
            when (i % 5) {
                0 -> dummyPolls.add(
                    Poll(
                        id = i,
                        question = "What is your favorite programming language?",
                        options = listOf(
                            PollOption("Kotlin", 142),
                            PollOption("Java", 120),
                            PollOption("Python", 189),
                            PollOption("JavaScript", 156)
                        ),
                        totalVotes = 607
                    )
                )
                1 -> dummyPolls.add(
                    Poll(
                        id = i,
                        question = "Which Android UI toolkit do you prefer?",
                        options = listOf(
                            PollOption("Jetpack Compose", 256),
                            PollOption("XML layouts", 187),
                            PollOption("Flutter", 93)
                        ),
                        totalVotes = 536
                    )
                )
                2 -> dummyPolls.add(
                    Poll(
                        id = i,
                        question = "How many hours do you spend coding per day?",
                        options = listOf(
                            PollOption("Less than 4 hours", 120),
                            PollOption("4-8 hours", 245),
                            PollOption("More than 8 hours", 78)
                        ),
                        totalVotes = 443
                    )
                )
                3 -> dummyPolls.add(
                    Poll(
                        id = i,
                        question = "What's your preferred Android development IDE?",
                        options = listOf(
                            PollOption("Android Studio", 375),
                            PollOption("IntelliJ IDEA", 86),
                            PollOption("VS Code", 43),
                            PollOption("Other", 12)
                        ),
                        totalVotes = 516
                    )
                )
                4 -> dummyPolls.add(
                    Poll(
                        id = i,
                        question = "Which architecture pattern do you use most?",
                        options = listOf(
                            PollOption("MVVM", 267),
                            PollOption("MVP", 134),
                            PollOption("MVC", 98),
                            PollOption("Clean Architecture", 178),
                            PollOption("Other", 22)
                        ),
                        totalVotes = 699
                    )
                )
            }
        }

        return dummyPolls
    }
}