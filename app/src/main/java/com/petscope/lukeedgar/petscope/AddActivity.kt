package com.petscope.lukeedgar.petscope

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.petscope.lukeedgar.petscope.Animals.Animal
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.content_add.*
import java.util.*
import java.util.UUID.randomUUID




class AddActivity : AppCompatActivity() {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var storage = FirebaseStorage.getInstance().reference
    private var selectedImageUri: Uri = Uri.EMPTY
    private var animal = Animal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Initialise Image FloatingAction Button
        fab.setOnClickListener {
            loadImageUploadDialog()
        }

        //Initialise Gender Spinner
        val spinnerArray: Array<String> = resources.getStringArray(R.array.Genders)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,spinnerArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnGender.adapter = adapter

        //Initialise Add Animal Button
        btnAdd.setOnClickListener { onClickBtnAdd() }
    }

    private fun loadImageUploadDialog() {
        val intent = Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select a photo of your animal"), 123)
    }

    private fun onClickBtnAdd() {
        //Assign user inputs to animal
        animal.Animal_ID = generateRandomString()
        animal.Animal_Name = txtAnimalName.text.toString().capitalize()
        animal.animal_type = txtAnimalTypeAuto.text.toString().capitalize()
        animal.Animal_Breed = txtAnimalBreed.text.toString().capitalize()
        animal.Animal_Gender = spnGender.selectedItem.toString().capitalize()
        animal.Address = txtAnimalAddress.text.toString().capitalize()
        animal.Animal_Color = hexCodeColourToWord(color_slider.selectedColor.toString())
        //Add animal to database
        animal.addToDatabase(database)
        fab.transitionName = "reveal"
        finish()
    }

    fun hexCodeColourToWord(hexColour:String):String{
        return when(hexColour){
            "cc6f18" -> "Orange"
            "b2953e" -> "Golden"
            "#54524c" -> "Grey"
            "#262626" -> "Black"
            else -> ""
        }
        //Improvement: Use machine learning
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {

            selectedImageUri = data.data //The uri with the location of the file

            val filePath = storage.child("images").child(selectedImageUri.lastPathSegment)

            filePath.putFile(selectedImageUri).addOnSuccessListener {
                animal.ImageURL = it.downloadUrl.toString()
                Snackbar.make(fab, "Photo Uploaded! " + animal.ImageURL, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                getCoverImage()
            }
        }
    }

    fun getCoverImage() {
            //Load the Image into toolbar
            Picasso.with(applicationContext)
                    .load(animal.ImageURL)
                    .fit()
                    .into(imgUploadToolbar)
    }

    fun generateRandomString(): String {
        val uuid = UUID.randomUUID().toString()
        return "uuid = $uuid"
    }
}
