package com.example.pettracker.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pettracker.Database.Pet
import com.example.pettracker.R

class PetListAdapter(val context: Context, val pets: List<Pet>, val itemClick: (Pet) -> Unit) : RecyclerView.Adapter<PetListAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.recycler_item_pet, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return pets.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bindCategory(pets[position], context)
    }

    inner class Holder(itemView: View, val itemClick: (Pet) -> Unit) : RecyclerView.ViewHolder(itemView) {
        //val petImage = itemView?.findViewById<ImageView>(R.id.petImageID)
        val petName = itemView?.findViewById<TextView>(R.id.petNameInput)

        fun bindCategory(pet: Pet, context: Context){
            //val resourceId = context.resources.getIdentifier(pet.image, "drawable", context.packageName)
            //petImage?.setImageResource(resourceId)
            petName?.text = pet.name

            itemView.setOnClickListener { itemClick(pet) }
        }
    }
}