package com.example.pettracker.Database

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class PetViewModel(application: Application, var petDB : PetRoomDatabase, var petDAO: PetDao):AndroidViewModel(application) {

}