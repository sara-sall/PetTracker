package com.example.pettracker.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pettracker.Database.Pet
import com.example.pettracker.R
import java.lang.Exception

class PetListAdapter(val context: Context, var pets: List<Pet>, val itemClick: (Pet) -> Unit) :
    RecyclerView.Adapter<PetListAdapter.Holder>() {
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
        val petImage = itemView?.findViewById<ImageView>(R.id.petImageMain)
        val petName = itemView?.findViewById<TextView>(R.id.petNameInput)

        fun bindCategory(pet: Pet, context: Context){

            try {
                if(pet.petImage != "null"){
                    var imgUri: Uri = Uri.parse(pet.petImage)
                    petImage.setImageURI(imgUri)
                }else{
                    petImage.visibility = View.GONE
                }
            } catch (e : Exception ){
                petImage.visibility = View.GONE
            }

            petName?.text = pet.name

            itemView.setOnClickListener { itemClick(pet) }
        }
    }

    fun updatePetsList(petsUpdated: List<Pet>) {
        pets = petsUpdated
        notifyDataSetChanged()
    }
}