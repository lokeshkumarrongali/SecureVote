package com.example.securevote

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameField = findViewById<EditText>(R.id.username)
        val biometricLoginButton = findViewById<Button>(R.id.biometric_login)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val termsCheckBox = findViewById<CheckBox>(R.id.termsCheckBox)

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val enteredName = usernameField.text.toString().trim()
                if (enteredName.isEmpty()) {
                    Toast.makeText(applicationContext, "Please enter your name", Toast.LENGTH_SHORT).show()
                    return
                }
                val prefs = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                val registeredName = prefs.getString("registered_name", null)
                if (registeredName == null) {
                    prefs.edit().putString("registered_name", enteredName).apply()
                    loginSuccess()
                } else if (enteredName == registeredName) {
                    loginSuccess()
                } else {
                    Toast.makeText(applicationContext, "Name does not match registered user", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for SecureVote")
            .setSubtitle("Log in using your biometric credential")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        biometricLoginButton.setOnClickListener {
            if (!termsCheckBox.isChecked) {
                Toast.makeText(this, "You must agree to the Terms and Conditions.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    biometricPrompt.authenticate(promptInfo)
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Toast.makeText(this, "No biometric hardware available", Toast.LENGTH_SHORT).show()
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Toast.makeText(this, "Biometric hardware unavailable", Toast.LENGTH_SHORT).show()
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        )
                    }
                    startActivity(enrollIntent)
                }
            }
        }
    }

    private fun loginSuccess() {
        Log.d("LoginActivity", "Login Success Triggered")
        Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
