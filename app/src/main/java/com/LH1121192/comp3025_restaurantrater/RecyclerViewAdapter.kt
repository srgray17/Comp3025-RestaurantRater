package com.LH1121192.comp3025_restaurantrater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter (val context: Context, val restaurants : List<Restaurant>,val itemListener : RecyclerViewAdapter.RestaurantItemListener) : RecyclerView.Adapter<RecyclerViewAdapter.RestaurantViewHolder>() {

    inner class RestaurantViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    //this method will tell the adapter how many items need to be
    override fun getItemCount(): Int {
        return restaurants.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]
        with(holder){
            nameTextView.text = restaurant.name
            ratingBar.rating = restaurant.rating!!.toFloat()
        }
        holder.itemView.setOnClickListener{
            itemListener.restaurantSelected(restaurant)
        }
    }

    interface RestaurantItemListener{
        fun restaurantSelected(restaurant : Restaurant)
    }
}