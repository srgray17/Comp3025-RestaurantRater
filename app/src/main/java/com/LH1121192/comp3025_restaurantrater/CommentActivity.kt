package com.LH1121192.comp3025_restaurantrater

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.LH1121192.comp3025_restaurantrater.databinding.ActivityCommentBinding
import com.google.firebase.firestore.FirebaseFirestore

class CommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBinding

    private lateinit var viewModel: CommentViewModel
    private lateinit var viewModelFactory: CommentViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.restaurantNameTextView.text = intent.getStringExtra("restaurantName")
        val restaurantID = intent.getStringExtra("restaurantID")

        //saving new comment
        binding.saveCommentButton.setOnClickListener {
            if (binding.usernameEditText.text.toString()
                    .isNotEmpty() && binding.bodyEditText.text.toString().isNotEmpty()
            ) {
                //save
                val db = FirebaseFirestore.getInstance().collection("comments")
                val id = db.document().id

                val newComment = Comment(
                    id,
                    binding.usernameEditText.text.toString(),
                    binding.bodyEditText.text.toString(),
                    restaurantID
                )
                db.document(id).set(newComment)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Added to DB", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to add", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(
                    this,
                    "Both username and comment must be populated",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        //
        restaurantID?.let {
            viewModelFactory = CommentViewModelFactory(restaurantID)

            viewModel = ViewModelProvider(this, viewModelFactory).get(CommentViewModel::class.java)
            viewModel.getComments().observe(this, Observer<List<Comment>> {comments ->
                var recyclerAdapter = CommentViewAdapter(this, comments)
                binding.commentsRecyclerView.adapter = recyclerAdapter
            })
        }
        binding.mapsFAB.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("restaurantName", binding.restaurantNameTextView.text.toString())
            startActivity(intent)
        }
        setSupportActionBar(binding.mainToolbar.toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add -> {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                return true
            }
            R.id.action_list -> {
                startActivity(Intent(applicationContext, RestaurantRecyclerListActivity::class.java))
                return true
            }
            R.id.action_profile -> {
                startActivity(Intent(applicationContext, ProfileActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}