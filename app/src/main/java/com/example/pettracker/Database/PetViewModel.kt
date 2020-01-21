package com.example.pettracker.Database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import java.util.*

class PetViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: PetRepository = PetRepository(application)
    val allPets: ArrayList<Pet> = mRepository.allPets as ArrayList<Pet>

    fun insert(pet: Pet): Unit {
        mRepository.insert(pet)
    }

    fun getPetById(petUUID: String): Pet {
        return mRepository.getPetById(petUUID)
    }

}