package com.striyank.chatwithme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.striyank.chatwithme.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDataseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Hide the action bar
        supportActionBar?.hide()

        /**
         * Initialize Firebase authentification
         */
        auth = FirebaseAuth.getInstance()

        binding.signUpButton.setOnClickListener {
            val userName = binding.userNameEditText.text.toString()
            val userEmail = binding.loginEmailEditText.text.toString()
            val userPassword = binding.loginPasswordEditText.text.toString()

            createNewUser(userName, userEmail, userPassword)
        }
    }

    /**
     * Method to create a new user
     */
    private fun createNewUser(userName : String, email: String, password: String) {

        if (email.isEmpty() || password.isEmpty() || userName.isEmpty()) {
            Toast.makeText(this, "Please, enter your connection information correctly",
                Toast.LENGTH_LONG).show()
        }else{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        addUserToDatabase(userName, email, auth.currentUser!!.uid)
                        Toast.makeText(this, "You are registered successfully",
                            Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()

                    } else { Toast.makeText(this, "Some errors occured",
                        Toast.LENGTH_LONG).show()                }
                }
        }
    }

    /**
     * Method to add user to the database
     */
    private fun addUserToDatabase(userName: String, email: String, uid: String?) {
        //Get reference to the firebase database
        firebaseDataseRef = FirebaseDatabase.getInstance().reference

        //Add user to the firebase database
        firebaseDataseRef.child("user").child(uid!!).setValue(User(userName, email, uid))
    }
}