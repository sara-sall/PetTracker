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
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.FutureTarget
import com.example.pettracker.Database.Pet
import com.example.pettracker.Database.PetRoomDatabase
import com.example.pettracker.R
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pet.*
import kotlinx.android.synthetic.main.card_general_pet.*
import kotlinx.android.synthetic.main.card_insurance_pet.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

class PetActivity : AppCompatActivity() {

    private lateinit var db: PetRoomDatabase
    private var pet: Pet? = null
    private var databaseLoaded: Boolean = false

    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.empty)
        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        appBarLayout = this.app_bar

        db = PetRoomDatabase.getInstance(this)

        var extras = intent.extras
        val id = extras?.get("id") as String

        Thread {
            pet = db.petRoomDao().getPetById(id)
            addPetData(pet!!)

        }.start()



    }


    fun addPetData(pet: Pet) {
        toolbar_pet_name.text = pet.name
        petNameText.text = pet.name
        Log.d("PETS", "Petbreedername: ${pet.breederName}")

        try {
            if(pet.petImage != "null"){
                var imgUri: Uri = Uri.parse(pet.petImage)
                pImage.setImageURI(imgUri)
            }else{
                pImage.setImageResource(R.drawable.ic_pets_white_24dp)

            }
        } catch (e : Exception){
            pImage.setImageResource(R.drawable.ic_pets_white_24dp)

        }

        if (pet.breederName == "") {
            breederNameLayout.visibility = View.GONE
        } else {
            petBreederNameText.text = pet.breederName
        }

        if (pet.race == "") {
            raceLayout.visibility = View.GONE
        } else {
            petRaceText.text = pet.race
        }

        if (pet.birthDay == "") {
            petAgeLayout.visibility = View.GONE
            petAgeText.visibility = View.GONE
        } else {
            petBirthdateText.text = pet.birthDay
            petAgeText.text = "${calculateAge(pet.birthDay)}"
        }

        when (pet.sex) {
            getString(R.string.pet_sex_female) -> petSexImage.setImageDrawable(getDrawable(R.drawable.ic_pets_pink_24dp))
            getString(R.string.pet_sex_male) -> petSexImage.setImageDrawable(getDrawable(R.drawable.ic_pets_blue_24dp))
        }

        if (pet.neutered) {
            neuteredText.visibility = View.VISIBLE
        }

        if(pet.insuranceProvider == "" && pet.insuranceNumber == ""){
            petInsuranceInfoCard.visibility = View.GONE
        }

        if(pet.insuranceProvider == ""){
            insuranceProviderLayout.visibility = View.GONE
        }else{
            petInsuranceProviderText.text = pet.insuranceProvider
        }

        if(pet.insuranceNumber == ""){
            insuranceNumberLayout.visibility = View.GONE
        }else{
            petInsuranceNumberText.text = pet.insuranceNumber
        }

    }

    private fun loadImage(imageUri: Uri) {
        Glide.with(this).load(imageUri).into(pImage)
        //Picasso.get().load(imageUri).into(pImage)
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
