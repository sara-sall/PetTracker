package com.example.pettracker.Database

import android.app.Application
import android.os.AsyncTask

class PetRepository(application: Application) {
    val db: PetRoomDatabase = PetRoomDatabase.getInstance(application)
    val petDao: PetDao = db.petRoomDao()
    val allPets: List<Pet> = petDao.getPets()

    fun getPetById(petUUID: String): Pet {
        return petByIdTask(petDao).doInBackground(petUUID)
    }

    fun insert(pet: Pet): Unit {
        insertAsyncTask(petDao).execute(pet)
    }

    fun delete(pet: Pet){
        deletePetById(petDao).execute(pet)
    }

    private class insertAsyncTask(petDao: PetDao) : AsyncTask<Pet, Void, Void>() {
        private val mAsyncTaskDao: PetDao = petDao
        override fun doInBackground(vararg params: Pet?): Void? {
            mAsyncTaskDao.addOrUpdatePet(params[0])
            return null
        }

    }

    private class petByIdTask(petDao: PetDao) : AsyncTask<String, Void, Pet>() {
        private val mTaskDao: PetDao = petDao
        public override fun doInBackground(vararg params: String?): Pet {
            return mTaskDao.getPetById(params.toString())
        }
    }

    private class deletePetById(petDao: PetDao) : AsyncTask<Pet, Void, Void>() {
        private val mAsyncTaskDao: PetDao = petDao
        override fun doInBackground(vararg params: Pet): Void? {
            mAsyncTaskDao.deletePet(params[0])
            return null
        }

    }
}