package com.striyank.chatwithme

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.striyank.chatwithme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var userList : ArrayList<User>
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var firebaseDatabaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get reference to firebase database
        firebaseDatabaseRef = FirebaseDatabase.getInstance().reference

        //Get reference to firebase authentification
        auth = FirebaseAuth.getInstance()

        userList = ArrayList()
        adapter = UserAdapter(this, userList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        /**
         * Get user from the database and add it to the created list
         * snapshot is a schema of user in the database
         */
        firebaseDatabaseRef.child("user").addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                //Clear existing user list before add others users
                userList.clear()

                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    //Only connected users are shown in the user list
                    if (auth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                //Notify the adapter about the data change
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    /**
     * Inflate created menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Set click listener on item in menu
     * Log out logic
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return true
        }
        return true
    }
}