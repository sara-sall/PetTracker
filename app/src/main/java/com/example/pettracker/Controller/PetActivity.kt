package com.example.pettracker.Controller

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pettracker.Database.Pet
import com.example.pettracker.Database.PetRoomDatabase
import com.example.pettracker.R
import com.google.android.material.appbar.AppBarLayout
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

        val extras = intent.extras
        val id = extras?.get("id") as String

        Thread {
            pet = db.petRoomDao().getPetById(id)
            addPetData(pet!!)
        }.start()



    }


    private fun addPetData(pet: Pet) {
        toolbar_pet_name.text = pet.name
        petNameText.text = pet.name
        Log.d("PETS", "Petbreedername: ${pet.breederName}")

        try {
            if(pet.petImage != "null"){
                val imgUri: Uri = Uri.parse(pet.petImage)
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

    private fun calculateAge(bd: String): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date: Date? = sdf.parse(bd)
        val cal: Calendar = Calendar.getInstance()

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_pet, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) :Boolean {
        return when(item.itemId) {
            R.id.action_edit -> {
                startActivity(Intent(this@PetActivity, AddPetActivity::class.java).putExtra(
                    "id",
                    pet?.id))
                true
            }
            R.id.action_delete -> {
                alertCheck()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun alertCheck() {
        val alert = AlertDialog.Builder(this)

        alert.setTitle(getString(R.string.alert_warning))
        alert.setMessage(String.format(getString(R.string.alert_delete_message), pet?.name))

        alert.setPositiveButton(getString(R.string.action_delete)) { dialog, which ->
           deletePet()
        }

        alert.setNegativeButton(getString(R.string.alert_cancel)) { dialog, which ->
        }

        alert.show()

    }

    private fun deletePet(){
        Thread {
            db.petRoomDao().deletePet(pet!!)
        }.start()
        Toast.makeText(this, String.format(getString(R.string.alert_delete_toast, pet?.name )), Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@PetActivity, MainActivity::class.java))

    }
}
