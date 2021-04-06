package com.LH1121192.comp3025_restaurantrater

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class CommentViewModel(restaurantID : String) : ViewModel() {
    private val comments = MutableLiveData<List<Comment>>()

    init{
        //query the DB (firestore) to get a list of comments
        val db = FirebaseFirestore.getInstance().collection("comments")
            .whereEqualTo("restaurantID", restaurantID)

        db.addSnapshotListener{ documents, exception ->
            if (exception != null){
                Log.w("DBQUERY", "Listen failed")
                return@addSnapshotListener
            }
            documents?.let{
                val commentList = ArrayList<Comment>()
                for(document in documents)
                {
                    val comment = document.toObject(Comment::class.java)
                    commentList.add(comment)
                }
                comments.value = commentList
            }
        }
    }

    fun getComments() : LiveData<List<Comment>>{
        return comments
    }

}