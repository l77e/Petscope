package com.petscope.lukeedgar.petscope.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.petscope.lukeedgar.petscope.AnimalDetailsActivity
import com.petscope.lukeedgar.petscope.Animals.Animal
import com.petscope.lukeedgar.petscope.R
import kotlinx.android.synthetic.main.pet_card.view.*

/**
 * Created by lukeedgar on 21/03/2018.
 */
class AnimalCardAdapter(val context: Context, private val data: List<Animal>) : RecyclerView.Adapter<AnimalCardAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.pet_card, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAnimal = data[position]
        holder.animalName?.text = currentAnimal.Animal_Name
        holder.animalType?.text = "${currentAnimal.animal_type} (${currentAnimal.Animal_Breed})"
        //Load the animal icon into imgAnimalIcon ItemView
        currentAnimal.loadAnimalIcon(context,holder.imgAnimalIcon)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val animalName = itemView.txtName
        val animalType = itemView.txtAnimal
        val imgAnimalIcon = itemView.imgAnimalIcon

        init { itemView.setOnClickListener(this) }

        override fun onClick(p0: View?) {
                val intent = Intent(context, AnimalDetailsActivity::class.java)
                    intent.flags =Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("animal",data[position])
                context.startActivity(intent)
            }
        }
    }
