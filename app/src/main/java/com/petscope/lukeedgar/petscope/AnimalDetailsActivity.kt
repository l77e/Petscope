package com.petscope.lukeedgar.petscope

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.petscope.lukeedgar.petscope.Animals.Animal
import kotlinx.android.synthetic.main.activity_animal_details.*
import kotlinx.android.synthetic.main.content_animal_details.*

class AnimalDetailsActivity : AppCompatActivity() {

    var animal = Animal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_details)
        setSupportActionBar(toolbar)

        //Set the user selected animal
        animal = intent.extras.get("animal") as Animal

        //Set the toolbar title
        toolbar_layout.title = "${animal.Animal_Name} The ${animal.animal_type}"

        fabDetails.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            txtAnimalDetails.text = getAnimalDetails()
        }

    }

    private fun getAnimalDetails(): String {
        return "Animal Name: ${animal.Animal_Name} \n Animal Type: ${animal.animal_type} \n Animal Gender: ${animal.Animal_Gender}"
    }
}
