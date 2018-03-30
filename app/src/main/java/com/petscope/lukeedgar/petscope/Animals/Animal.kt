package com.petscope.lukeedgar.petscope.Animals

import android.content.Context
import android.widget.ImageView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import java.util.*
import java.io.*
import java.net.URL


/**
 * Created by lukeedgar on 21/03/2018.
 */
open class Animal : Serializable {
    var ImageURL = ""
    var Address = ""
    var Animal_Breed = ""
    var Animal_Color = ""
    var Animal_Gender = ""
    var Animal_ID = ""
    var Animal_Name = ""
    var animal_type = ""

    override fun toString(): String {
        return "$Address$Animal_Breed$Animal_Color$Animal_Gender$Animal_ID$Animal_Name$animal_type"
    }

    fun animalDetails(): String =
    //Lists animal with each attribute on each line
            "ID: ${Animal_ID}\nName: ${Animal_Name} \nAnimal: ${animal_type}\nBreed: ${Animal_Breed}\nGender: ${Animal_Gender}\nColour: ${Animal_Color}\nAddress: ${Address}"

    fun addToDatabase(database: FirebaseDatabase) {
        Animal_ID = generateId()
        database.reference
                .child("Animals")
                .push()
                .setValue(this)
    }

    fun loadAnimalIcon(context: Context, imageView: ImageView) {
        if (animal_type != "") {
            Picasso.with(context)
                    .load("file:///android_asset/animal_icons/${animal_type.capitalize()}.png")
                    .into(imageView)
        }
    }

    fun loadAnimalImage(context: Context, imageView: ImageView, url: String = ImageURL) {
        if (url != "") {
            Picasso.with(context)
                    .load(url)
                    .into(imageView)
        }
    }

    private fun generateId(): String = UUID.randomUUID().toString()


}