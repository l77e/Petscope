package com.petscope.lukeedgar.petscope

import android.os.Bundle
import android.os.StrictMode
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.petscope.lukeedgar.petscope.Animals.Animal
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_animal_details.*
import kotlinx.android.synthetic.main.content_animal_details.*
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.net.URL
import java.nio.charset.Charset

class AnimalDetailsActivity : AppCompatActivity() {

    private var animal = Animal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_details)
        setSupportActionBar(mainToolbar)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //Set the user selected animal
        animal = intent.extras.get("animal") as Animal

        //Set the toolbar title
        toolbar_layout.title = "${animal.Animal_Name} The ${animal.animal_type}"

        fabDetails.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

    }

    override fun onStart() {
        super.onStart()
        txtDisplayAnimalInformation.text = getAnimalDetails()
        try {
            getCoverImage()
        } catch (e: Exception) {
        }
    }

    private fun getAnimalDetails(): String {
        //Lists animal with each attribute on each line
        return "ID: ${animal.Animal_ID}\nName: ${animal.Animal_Name} \nAnimal: ${animal.animal_type}\nBreed: ${animal.Animal_Breed}\nGender: ${animal.Animal_Gender}\nColour: ${animal.Animal_Color}\nAddress: ${animal.Address}"
    }

    private fun getCoverImage() {
        //There is no image for this animal, load placeholder
        if (animal.ImageURL == "") {
            //Get the image from API
            val url = "https://api.qwant.com/api/search/images?count=1&offset=1&q=${animal.Animal_Breed}"
            val json = JSONObject(IOUtils.toString(URL(url), Charset.forName("UTF-8")))
            val imageUrl = json.getJSONObject("data")
                    .getJSONObject("result")
                    .getJSONArray("items")
                    .getJSONObject(0)
                    .getString("media")

            //Load the Placeholder Image into toolbar
            animal.loadAnimalImage(applicationContext,imgToolbar,imageUrl)
        }
        //There is an uploaded animal image, load animal image
        else {
            //Load the True Animal Image into toolbar
            animal.loadAnimalImage(applicationContext,imgToolbar)
        }
    }
}
