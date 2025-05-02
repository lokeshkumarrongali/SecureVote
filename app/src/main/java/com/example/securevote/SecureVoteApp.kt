package com.example.securevote

import android.app.Application
import com.example.securevote.utils.DataGenerator

class SecureVoteApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize sample data
        DataGenerator.populateSampleData(this)
    }
}