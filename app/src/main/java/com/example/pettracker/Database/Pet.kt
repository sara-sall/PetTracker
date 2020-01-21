package com.example.pettracker.Database

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "pets"
)
class Pet (
    @PrimaryKey
    val id: String,
    var name: String,
    var petImage: String,
    var breederName: String,
    var race: String,
    var birthDay: String,
    var sex: String,
    var neutered: Boolean
) {
    //, val breederName: String, val birthday: Int, val birthMonth: Int, val birthYear: Int, val race: String, val insuranceProvider: String, val insuranceNr: String, val petId: String
}