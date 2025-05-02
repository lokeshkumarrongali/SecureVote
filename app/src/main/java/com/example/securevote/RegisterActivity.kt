package com.example.securevote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val usernameField = findViewById<EditText>(R.id.username)
        val registerButton = findViewById<Button>(R.id.registerBiometric)

        registerButton.setOnClickListener {
            val name = usernameField.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Save the name (and optionally PIN) in SharedPreferences
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            prefs.edit().putString("registered_name", name).apply()

            // Now prompt for biometric enrollment
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
