package com.example.pettracker.Database

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class PetViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: PetRepository = PetRepository(application)

    val petByUuidLiveData = MutableLiveData<Pet?>()
    val pets = MutableLiveData<List<Pet>>()

    fun insert(pet: Pet) {
        viewModelScope.launch {
            mRepository.insert(pet)
        }
    }

    fun getPetById(petUUID: String) {
        viewModelScope.launch {
            val pet = withContext(Dispatchers.IO){
                mRepository.getPetByIdNew(petUUID)
            }
            Log.d("PET", "Pet: $pet")
            petByUuidLiveData.value = pet
        }
    }

    fun deletePet(pet: Pet){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                mRepository.delete(pet)
            }
        }
    }

    fun getAllPets() {
        viewModelScope.launch {
            val petsList = withContext(Dispatchers.IO){
                mRepository.getPets()
            }
            pets.value = petsList
        }
    }

}
