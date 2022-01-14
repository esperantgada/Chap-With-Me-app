package com.striyank.chatwithme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.striyank.chatwithme.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            //Hide the action bar
            supportActionBar?.hide()
            /**
             * Initialize Firebase Authentification
             */
            auth = FirebaseAuth.getInstance()

            binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

            binding.signUpButton.setOnClickListener {
                startActivity(Intent(this, SignUpActivity::class.java))
            }

            binding.loginButton.setOnClickListener {
                val userEmail = binding.loginEmailEditText.text.toString()
                val userPassword = binding.loginPasswordEditText.text.toString()
                loginUser(userEmail, userPassword)
            }
    }

    /**
     * Method to login existent user
     */
    private fun loginUser(email : String, password : String) {

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please, enter your connection information",
                Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "You are logged in successfully",
                            Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(this, "User doesn't exist",
                            Toast.LENGTH_LONG).show()

                    }
                }
        }
    }
}