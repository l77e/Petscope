package com.petscope.lukeedgar.petscope

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.content_add.*
import android.widget.ArrayAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.petscope.lukeedgar.petscope.Animals.Animal
import eltos.simpledialogfragment.color.SimpleColorDialog


class AddActivity : AppCompatActivity() {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var databaseSnapshot: DataSnapshot? = null
    var ref = database.reference
    private var selectedImageUri: Uri = Uri.EMPTY
    private var animal = Animal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setSupportActionBar(toolbar)

        //Initialise Image FloatingAction Button
        fab.setOnClickListener { view ->
            loadImageUploadDialog()
            Snackbar.make(view, "Photo Uploaded!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        //Initialise Gender Spinner
        val spinnerArray: Array<String> = resources.getStringArray(R.array.Genders)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,spinnerArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnGender.adapter = adapter

        //Initialise Add Animal Button
        btnColourPicker.setOnClickListener { onClickBtnColourPicker() }
        //Initialise Add Animal Button
        btnAdd.setOnClickListener { onClickBtnAdd() }
    }

    private fun onClickBtnColourPicker() {
        SimpleColorDialog.build()
                .title("Choose colour")
                .colorPreset(Color.RED)
                .allowCustom(true)
                .show(this)
    }

    private fun loadImageUploadDialog() {
        val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select a photo of your animal"), 123)
    }

    private fun onClickBtnAdd() {
        //Assign user inputs to animal
        animal.Animal_Name = txtAnimalName.text.toString().capitalize()
        animal.animal_type = txtAnimalTypeAuto.text.toString().capitalize()
        animal.Animal_Breed = txtAnimalBreed.text.toString().capitalize()
        animal.Animal_Gender = spnGender.selectedItem.toString().capitalize()
        //Add animal to database
        animal.AddToDatabase(database)
        fab.transitionName = "reveal"
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data.data //The uri with the location of the file
        }
    }

}
