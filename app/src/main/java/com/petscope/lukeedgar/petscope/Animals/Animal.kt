package com.petscope.lukeedgar.petscope.Animals

import android.media.Image
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable


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
        return Address + Animal_Breed + Animal_Color + Animal_Gender + Animal_ID + Animal_Name + animal_type
    }

    fun AddToDatabase(database: FirebaseDatabase) {
        database.reference
                .push()
                .setValue(this)
    }
}