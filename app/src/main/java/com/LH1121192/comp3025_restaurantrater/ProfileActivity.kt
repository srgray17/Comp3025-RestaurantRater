package com.LH1121192.comp3025_restaurantrater

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.LH1121192.comp3025_restaurantrater.databinding.ActivityProfileBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

import java.io.File
import java.io.FileInputStream

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private val authDb = FirebaseAuth.getInstance()

    //these variables are used for handling the profile picture
    private val REQUEST_CODE = 1000
    private lateinit var filePhoto : File
    private val FILE_NAME = "photo"

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //check that we have a valid user.  If not, go to login screen
        if (authDb.currentUser == null)
            logout()

        //update the user's name and email address
        authDb.currentUser?.let{ user->
            binding.usernameTextView.text = user.displayName
            binding.emailTextView.text = user.email

            //Ask Firebase for the URI to the photo (where on the device is the photo)
            var profilePhoto : Uri? = user.photoUrl

            //if the profile photo URI is returned from Firebase AND it leads to a location on the device
            //load the image
//            if (profilePhoto != null && profilePhoto.path != null)
//                loadProfileImage(profilePhoto.path!!)
        }

        //enable the scroll bars on the textview
        binding.termsTextView.movementMethod = ScrollingMovementMethod()

        //ensure the device has the correct permissions
        //So if we don't have permission we request for permissions from the user, this will execute the overridden onRequestPermissionsResult
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(
                        Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) || checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
            )
        }

        //add a click listener for the camera button
        binding.imageButton.setOnClickListener {
            //create an Intent to navigate to the camera
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            //add the file location to the intent
            filePhoto = getPhotoFile(FILE_NAME)
            val providerFile = FileProvider.getUriForFile(this,
                    "com.LH1121192.comp3025_restaurantrater.fileProvider",
                    filePhoto)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)

            //this will run the intent to call the camera
            if (intent.resolveActivity(this.packageManager) != null)
                startActivityForResult(intent, REQUEST_CODE) //execute the intent
            else
                Toast.makeText(this, "Camera did not open", Toast.LENGTH_LONG).show()

        }

        //connect the logout FAB to the logout method
        binding.logoutFAB.setOnClickListener {
            logout()
        }

        setSupportActionBar(binding.mainToolbar.toolbar)
    }

    //This method will connect the main_menu.xml file with the menu in the toolbar.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //this method will allow the user to select items from the menu
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
//                startActivity(Intent(applicationContext, ProfileActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout(){
        authDb.signOut()
        finish()
        startActivity(Intent(this, SignInActivity::class.java))
    }

    //This method will return the file object for the picture (the actual .jpg)
    private fun getPhotoFile(fileName: String) : File{
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }

    //if the Intent successfully took a photo, convert the photo to a bitmap and display in the imageview
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takenPhoto = BitmapFactory.decodeFile(filePhoto.absolutePath)
            binding.imageView.setImageBitmap(takenPhoto)

            //try saving the photoURI to the user's firebase profile for persistence
            //convert the file path from a String to URI before saving
            var builder = Uri.Builder()
            var localUri = builder.appendPath(filePhoto.absolutePath).build()
            saveProfilePhoto(localUri)
        }
        else
        //this never really excecutes...
            super.onActivityResult(requestCode, resultCode, data)
    }

    //This method will save the image and update Firebase to know where to find it.
    private fun saveProfilePhoto(imageUri: Uri?){
        var profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(imageUri)
                .build()

        //Commit the update to Firebase. This runs ascynronously as a task
        authDb.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener{
            OnCompleteListener<Void?> {
                if (it.isSuccessful)
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun loadProfileImage(pathToImage: String){
        var file : File = File(pathToImage)
        var bitmapImage = BitmapFactory.decodeStream(FileInputStream(file))
        binding.imageView.setImageBitmap(bitmapImage)
    }
}