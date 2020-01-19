package com.example.pettracker.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "pets"
)
class Pet (
    @PrimaryKey
    val id : UUID,
    var name: String){
    //, val breederName: String, val birthday: Int, val birthMonth: Int, val birthYear: Int, val race: String, val insuranceProvider: String, val insuranceNr: String, val petId: String
}