package com.striyank.chatwithme

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context: Context, private val messageList : ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val MESSAGE_RECEIVED = 1
    private val MESSAGE_SENT = 2

    class SentMessageViewHolder(messageView : View) : RecyclerView.ViewHolder(messageView){
        val sentMessage = messageView.findViewById<TextView>(R.id.sent_message)!!
    }

    class ReceivedMessageViewHolder(messageView : View) : RecyclerView.ViewHolder(messageView){
        val receivedMessage = messageView.findViewById<TextView>(R.id.received_message)!!
    }


    /**
     * Inflate the layout based on viewType
     * If viewType is 1, inflate Received message layout
     * If not, Sent message layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1){
            val view : View = LayoutInflater.from(context).inflate(R.layout.received_message,
                parent, false)
            ReceivedMessageViewHolder(view)
        }else{
            val view : View = LayoutInflater.from(context).inflate(R.layout.sent_message,
                parent, false)
            SentMessageViewHolder(view)
        }
    }

    /**
     * Check if the holder class is either SentMessage or ReceivedMessage class
     * Get the currentMessage by its position from the message list
     * Bind the text view with the text accordingly
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass == SentMessageViewHolder::class.java){
            holder as SentMessageViewHolder   //Typecast the holder
            holder.sentMessage.text = currentMessage.message
        }else{
            holder as ReceivedMessageViewHolder
            holder.receivedMessage.text = currentMessage.message
        }
    }

    //Return an integer as based on the currentUser Id and the message sender Id
    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            MESSAGE_SENT
        }else{
            MESSAGE_RECEIVED
        }
    }

    override fun getItemCount() = messageList.size

}