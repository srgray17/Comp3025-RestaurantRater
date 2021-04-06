package com.LH1121192.comp3025_restaurantrater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerGridAdapter (val context: Context,
                           val restaurants : List<Restaurant>,
                           val itemListener : RestaurantItemListener) : RecyclerView.Adapter<RecyclerGridAdapter.RestaurantViewHolder>()
{
    //the goal of this class is to create a way to access the variables/views in the item_restaurant.xml layout file
    inner class RestaurantViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
    }

    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_restaurant_grid, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]
        with (holder){
            nameTextView.text = restaurant.name
            ratingBar.rating = restaurant.rating!!.toFloat()
        }
    }

    interface RestaurantItemListener{
        fun restaurantSelected(restaurant : Restaurant)
    }
}