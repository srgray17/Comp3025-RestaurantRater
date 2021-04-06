package com.LH1121192.comp3025_restaurantrater

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.LH1121192.comp3025_restaurantrater.databinding.ActivityRestaurantListBinding
import androidx.lifecycle.Observer

class RestaurantListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRestaurantListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model : RestaurantListViewModel by viewModels()
        model.getRestaurants().observe( this, Observer<List<Restaurant>>{restaurantList->
            //clear the exisiting LinearLayout
            binding.linearLayout.removeAllViews()

            for (restaurant in restaurantList)
            {
                val textView = TextView(this)
                textView.text = restaurant.name
                textView.textSize = 20f
                binding.linearLayout.addView(textView)
            }
        }
        )
    }
}