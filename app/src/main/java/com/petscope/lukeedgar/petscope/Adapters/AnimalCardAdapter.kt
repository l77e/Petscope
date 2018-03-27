package com.petscope.lukeedgar.petscope.Adapters

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petscope.lukeedgar.petscope.AnimalDetailsActivity
import com.petscope.lukeedgar.petscope.Animals.Animal
import com.petscope.lukeedgar.petscope.R
import kotlinx.android.synthetic.main.pet_card.view.*
import java.util.*

/**
 * Created by lukeedgar on 21/03/2018.
 */
class AnimalCardAdapter(context: Context, private val data: List<Animal>) : RecyclerView.Adapter<AnimalCardAdapter.ViewHolder>() {
    val _context = context

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAnimal = data[position]
        holder.animalName?.text = currentAnimal.Animal_Name
        holder.animalType?.text = "${currentAnimal.animal_type} (${currentAnimal.Animal_Breed})"

        //getCoverImage(currentAnimal.Animal_Breed,holder?.imgProfile!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.pet_card, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var animalName = itemView.txtName
        var animalType = itemView.txtAnimal

        init {
            itemView.setOnClickListener(this)
        }

        //var imgProfile = itemView.findViewById<ImageView>(R.id.imgProfile)
        override fun onClick(p0: View?) {
                val intent = Intent(_context, AnimalDetailsActivity::class.java)
                    intent.flags =Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("animal",data[position])
                _context.startActivity(intent)
            }
        }
    }
