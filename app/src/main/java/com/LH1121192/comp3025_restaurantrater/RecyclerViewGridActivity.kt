package com.LH1121192.comp3025_restaurantrater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.LH1121192.comp3025_restaurantrater.databinding.ActivityRecyclerViewGridBinding


class RecyclerViewGridActivity : AppCompatActivity(), RecyclerGridAdapter.RestaurantItemListener {
    private lateinit var binding : ActivityRecyclerViewGridBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewGridBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get the data from the viewmodel
        val model : RestaurantListViewModel by viewModels()
        model.getRestaurants().observe(this, Observer<List<Restaurant>>{ restaurants->
            var recyclerAdapter = RecyclerGridAdapter(this, restaurants, this)
            binding.gridRecyclerView.adapter = recyclerAdapter
        })
    }

    override fun restaurantSelected(restaurant: Restaurant) {
        
    }
}