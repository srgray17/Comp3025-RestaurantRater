package com.LH1121192.comp3025_restaurantrater


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.LH1121192.comp3025_restaurantrater.databinding.ActivityRecyclerViewRestaurantListBinding


class RestaurantRecyclerListActivity : AppCompatActivity(), RecyclerViewAdapter.RestaurantItemListener {
    private lateinit var binding : ActivityRecyclerViewRestaurantListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewRestaurantListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get the data from the viewmodel
        val model : RestaurantListViewModel by viewModels()
        model.getRestaurants().observe(this, Observer<List<Restaurant>>{ restaurants->
            var recyclerAdapter = RecyclerViewAdapter(this, restaurants, this)
            binding.verticalRecyclerView.adapter = recyclerAdapter
        })


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
                //startActivity(Intent(applicationContext, RestaurantRecyclerListActivity::class.java))
                return true
            }
            R.id.action_profile -> {
                startActivity(Intent(applicationContext, ProfileActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun restaurantSelected(restaurant: Restaurant) {
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("restaurantID", restaurant.id)
        intent.putExtra("restaurantName", restaurant.name)
        startActivity(intent)
    }
}