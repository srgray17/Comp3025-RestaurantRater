package com.LH1121192.comp3025_restaurantrater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentViewAdapter (val context: Context,
                          val comments : List<Comment>) : RecyclerView.Adapter<CommentViewAdapter.CommentViewHolder>()    {

    //this connects the view with the view holder
    inner class CommentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val usernameTextView = itemView.findViewById<TextView>(R.id.usernameTextView)
        val bodyTextView = itemView.findViewById<TextView>(R.id.bodyTextView)
    }

    //show how many elements are in the collection we want to traverse
    override fun getItemCount(): Int {
        return comments.size
    }

    //This connects (aka "inflates") the individual ViewHolder (which links to restaurant_item.xml)
    //with the RecyclerView object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    //this binds the individual data elements to each viewHolder
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        with (holder){
            usernameTextView.text = comment.userName
            bodyTextView.text = comment.body
        }
    }
}