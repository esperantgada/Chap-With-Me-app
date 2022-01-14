package com.striyank.chatwithme

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(private val context : Context, private val userList : ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    class UserViewHolder(userView : View) : RecyclerView.ViewHolder(userView){
        val userName = userView.findViewById<TextView>(R.id.name)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.user_list, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        //Get current user
        val currentUser = userList[position]

        //Bind the text view with his name
        holder.userName.text = currentUser.name

        /**
         * When on sets click on an user, navigate to Chat Activity
         * Send user details to ChatActivity
         */
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = userList.size
}