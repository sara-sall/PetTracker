package com.example.pettracker.Database

import android.app.Application

class PetRepository(application: Application) {
    val db: PetRoomDatabase = PetRoomDatabase.getInstance(application)
    val petDao: PetDao = db.petRoomDao()

    suspend fun getPetByIdNew(petUUID: String): Pet? {
        return petDao.getPetById(petUUID)
    }

    suspend fun insert(pet: Pet) {
        petDao.addOrUpdatePet(pet)
    }

    suspend fun delete(pet: Pet){
        petDao.deletePet(pet)
    }

    suspend fun getPets() : ArrayList<Pet>{
        return petDao.getPets() as ArrayList<Pet>
    }
}