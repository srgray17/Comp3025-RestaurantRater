package com.LH1121192.comp3025_restaurantrater

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.LH1121192.comp3025_restaurantrater.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveRestaurantButton.setOnClickListener {

            //check to make sure the fields are populated
            if(binding.restaurantEditText.text.toString().isNotEmpty() && binding.ratingsSpinner.selectedItemPosition>0){
                val restaurant = Restaurant()
                restaurant.name = binding.restaurantEditText.text.toString()
                restaurant.rating = binding.ratingsSpinner.selectedItemPosition.toString().toInt()

                val db = FirebaseFirestore.getInstance().collection("restaurants")
                restaurant.id = db.document().id

                //our restaurant now has a name, rating and id
                db.document(restaurant.id!!).set(restaurant)
                    .addOnSuccessListener {
                        //show confirmation
                        Toast.makeText(this, "Restaurant Added", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, RestaurantRecyclerListActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
            }
            else{
                Toast.makeText(this, "Restaurant name & rating required", Toast.LENGTH_LONG).show()
            }
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
                //tartActivity(Intent(applicationContext, MainActivity::class.java))
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