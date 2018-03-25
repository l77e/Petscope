package com.petscope.lukeedgar.petscope.Adapters

import android.content.Context
import android.os.StrictMode
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.petscope.lukeedgar.petscope.Animals.Animal
import com.petscope.lukeedgar.petscope.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pet_card.view.*
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.net.URL
import java.nio.charset.Charset

/**
 * Created by lukeedgar on 21/03/2018.
 */
open class AnimalCardAdapter(context : Context, data : List<Animal>) : RecyclerView.Adapter<AnimalCardAdapter.ViewHolder>(){
    var data = emptyList<Animal>()
    var _context = context
    init {
        this.data = data
    }

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.pet_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val currentAnimal = data[position]
        holder?.animalName?.text = currentAnimal.Animal_Name
        holder?.animalType?.text = "${currentAnimal.animal_type} (${currentAnimal.Animal_Breed})"

        //getCoverImage(currentAnimal.Animal_Breed,holder?.imgProfile!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        var animalName = itemView.txtName
        var animalType = itemView.txtAnimal
        //var imgProfile = itemView.findViewById<ImageView>(R.id.imgProfile)
        override fun onClick(p0: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    fun getCoverImage(animalString: String, imageView: ImageView) {

        val url = "https://api.qwant.com/api/search/images?count=1&offset=1&q=${animalString}"
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val json = JSONObject(IOUtils.toString(URL(url), Charset.forName("UTF-8")))
        val imageUrl = json.getJSONObject("data").getJSONObject("result").getJSONArray("items").getJSONObject(0).getString("media")
        Picasso.with(_context).load(imageUrl).into(imageView)
    }

}