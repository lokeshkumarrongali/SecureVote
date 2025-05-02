package com.example.securevote

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_DELAY: Long = 3000 // 2 seconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Set padding for system bars
        val splashView = findViewById<android.view.View>(R.id.splash)
        splashView?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // Animate "SecureVote" TextView
        val splashText = findViewById<TextView>(R.id.splash_text)
        val moveAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        splashText.startAnimation(moveAnimation)

        // Move to Login screen after delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, SPLASH_DELAY)
    }
}
