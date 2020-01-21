package com.example.pettracker.Controller

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.FutureTarget
import com.example.pettracker.Database.Pet
import com.example.pettracker.Database.PetRoomDatabase
import com.example.pettracker.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pet.*
import kotlinx.android.synthetic.main.card_general_add_pet.*
import kotlinx.android.synthetic.main.card_general_pet.*
import kotlinx.android.synthetic.main.card_veterinary_add_pet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

class PetActivity : AppCompatActivity() {

    private lateinit var db: PetRoomDatabase
    private var pet: Pet? = null
    private var databaseLoaded: Boolean = false

    private lateinit var toolbar: Toolbar
    private lateinit var toolbarTitle: TextView

    private lateinit var petName: TextView
    private lateinit var petBreederName: TextView
    private lateinit var petBreederNameLayout: LinearLayout
    private lateinit var petRace: TextView
    private lateinit var petRaceLayout: LinearLayout
    private lateinit var petImageView: ImageView
    private lateinit var petBirthDate: TextView
    private lateinit var petBirthDateLayout: LinearLayout
    private lateinit var petAge: TextView
    private lateinit var petSex: ImageView
    private lateinit var isNeutered: TextView





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.empty)
        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        db = PetRoomDatabase.getInstance(this)

        petName = this.petNameText
        petBreederName = this.petBreederNameText
        petBreederNameLayout = this.breederNameLayout
        petRace = this.petRaceText
        petRaceLayout = this.raceLayout
        petImageView = this.pImage
        petBirthDate = this.petBirthdateText
        petBirthDateLayout = this.petAgeLayout
        petAge = this.petAgeText
        petSex = this.petSexImage
        isNeutered = this.neuteredText


        var extras = intent.extras
        val id = extras?.get("id") as String

        Thread {
            pet = db.petRoomDao().getPetById(id)
            addPetData(pet!!)

        }.start()



    }

    override fun onStart() {
        super.onStart()
        if (pet?.petImage != "") {
            loadImage(Uri.parse(pet!!.petImage))
        }
    }

    fun addPetData(pet: Pet) {
        toolbar.title = pet.name

        petName.text = pet.name
        Log.d("PETS", "Petbreedername: ${pet.breederName}")
        if (pet.breederName == "") {
            petBreederNameLayout.visibility = View.GONE
        } else {
            petBreederName.text = pet.breederName
        }

        if (pet.race == "") {
            petRaceLayout.visibility = View.GONE
        } else {
            petRace.text = pet.race
        }

        if (pet.birthDay == "") {
            petBirthDateLayout.visibility = View.GONE
            petAge.visibility = View.GONE
        } else {
            petBirthDate.text = pet.birthDay
            petAge.text = "${calculateAge(pet.birthDay)}"
        }

        when (pet.sex) {
            getString(R.string.pet_sex_female) -> petSex.setImageDrawable(getDrawable(R.drawable.ic_pets_pink_24dp))
            getString(R.string.pet_sex_male) -> petSex.setImageDrawable(getDrawable(R.drawable.ic_pets_blue_24dp))
        }

        if (pet.neutered) {
            isNeutered.visibility = View.VISIBLE
        }

    }

    private fun loadImage(imageUri: Uri) {
        //Glide.with(this).load(imageUri).into(petImageView)
        Picasso.get().load(imageUri).into(petImageView)
    }

    private fun calculateAge(bd: String): Int {
        var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = sdf.parse(bd)
        var cal: Calendar = Calendar.getInstance()

        cal.time = date

        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH) + 1
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)

        val bDate: LocalDate = LocalDate.of(year, month, day)
        val nowDate: LocalDate = LocalDate.now()

        val diff: Period = Period.between(bDate, nowDate)

        return diff.years
    }

    override fun onBackPressed() {
        startActivity(
            Intent(
                this@PetActivity,
                MainActivity::class.java
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
