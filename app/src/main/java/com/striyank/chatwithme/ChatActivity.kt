package com.striyank.chatwithme

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.striyank.chatwithme.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList : ArrayList<Message>
    private lateinit var firebaseDatabaseRef : DatabaseReference

    /**
     * Using sendRoom and receiverRoom will allow to create a unique Room for sender and receiver
     * so that the messages can be private and not shared with all users
     */


    private var receiverRoom : String? = null
    private var senderRoom : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Get user details
         */
        val userName = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        //Get the currently logged in user Id or sender
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        //Create the unique Room for sending
        senderRoom = receiverUid + senderUid

        //Create the unique Room for receiving
        receiverRoom = senderUid + receiverUid


        //Set the action bar title to the user name
        supportActionBar?.title = userName

        //Initialize variables
        firebaseDatabaseRef = FirebaseDatabase.getInstance().reference
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = messageAdapter

        /**
         * Retrieve messages from the database and bind the recyclerView with them
         */

        retrieveMessageFromDatabase()


        /**
         * Storing messages into the database
         */
        binding.sendIcon.setOnClickListener {
            saveMessageInDatabase(senderUid)

        }
    }

    /**
     * Retrieve messages from the database and bind the recyclerView with them
     */
    private fun retrieveMessageFromDatabase() {
        firebaseDatabaseRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {

                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    //Clear existing message list
                    messageList.clear()

                    for (snap in snapshot.children) {
                        //Get message from Message class in the database
                        val retrievedMessage = snap.getValue(Message::class.java)

                        //Add retrieved messages to Message list
                        messageList.add(retrievedMessage!!)
                    }
                    //Notify the adapter about the changes
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


    /**
     * Storing messages into the database
     */
    private fun saveMessageInDatabase(senderUid: String?) {
        //Get message from the user
        val typedmessage = binding.typedMessage.text.toString()

        //Create an object of type Message class
        val message = Message(typedmessage, senderUid)

        /**
         * Insert the message into the database and store it
         * The message is first inserted in [senderRoom] before being inserted in [receiverRoom]
         */
        firebaseDatabaseRef.child("chats").child(senderRoom!!).child("messages").push()
            .setValue(message).addOnSuccessListener {
                firebaseDatabaseRef.child("chats").child(receiverRoom!!).child("messages").push()
                    .setValue(message)
            }
        //Clear the message from edit text when it is sent
        binding.typedMessage.setText("")
    }
}