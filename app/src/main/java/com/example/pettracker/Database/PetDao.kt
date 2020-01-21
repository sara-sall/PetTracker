package com.example.pettracker.Database

import androidx.room.*

@Dao
interface PetDao {
    @Query("SELECT * FROM pets")
    fun getPets(): List<Pet>

    @Query("SELECT * FROM pets WHERE id = :id")
    fun getPetById(id: String): Pet

    @Delete
    fun deletePet(pet: Pet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdatePet(pet: Pet?): Long

    //
    //suspend fun
}