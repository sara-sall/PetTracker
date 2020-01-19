package com.example.pettracker.Database

import androidx.room.*
import java.util.*

@Dao
interface PetDao {
    @Query("SELECT * FROM pets")
    suspend fun getPets(): List<Pet>

    @Query("SELECT * FROM pets WHERE id = :id")
    suspend fun getPetById(id: UUID): Pet

    @Delete
    suspend fun deletePet(pet: Pet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdatePet(pet: Pet): Long
}