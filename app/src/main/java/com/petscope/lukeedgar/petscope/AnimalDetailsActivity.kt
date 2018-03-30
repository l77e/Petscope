package com.petscope.lukeedgar.petscope

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.petscope.lukeedgar.petscope.Animals.Animal
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

        fabDetails.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share this animal's information")
            intent.putExtra(Intent.EXTRA_TEXT, animal.toString())
            startActivity(Intent.createChooser(intent, "Share this animal's information"))
        }

        btnWiki.setOnClickListener {
           val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/${animal.Animal_Breed}"))
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        txtDisplayAnimalInformation.text = animal.animalDetails()
        txtAnimalExternalInfoHeader.text = "Find Out More About ${animal.Animal_Breed}s"
        btnWiki.text = "Wikipedia: ${animal.Animal_Breed}"
        try { getCoverImage() } catch (e: Exception) { Toast.makeText(this,"Issue getting an image", Toast.LENGTH_SHORT).show() }
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
