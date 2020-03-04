package com.example.pettracker.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "pets"
)
data class Pet (
    @PrimaryKey
    val id: String,
    var name: String,
    var petID: String,
    var petImage: String,
    var breederName: String,
    var race: String,
    var birthDay: String,
    var sex: String,
    var neutered: Boolean,
    var insuranceProvider: String,
    var insuranceNumber: String
)