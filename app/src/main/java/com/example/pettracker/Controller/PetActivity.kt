package com.example.pettracker.Controller

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.pettracker.Database.Pet
import com.example.pettracker.Database.PetViewModel
import com.example.pettracker.R
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_pet.*
import kotlinx.android.synthetic.main.card_general_pet.*
import kotlinx.android.synthetic.main.card_insurance_pet.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

class PetActivity : AppCompatActivity() {

    private lateinit var petG: Pet
    private lateinit var toolbar: Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var petViewModel: PetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet)

        petViewModel = ViewModelProviders.of(this)[PetViewModel::class.java]

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.empty)
        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        appBarLayout = this.app_bar

        val extras = intent.extras
        val id = extras?.get("id") as String

        petViewModel.getPetById(id)
        petViewModel.petByUuidLiveData.observe(this, androidx.lifecycle.Observer {
            it?.let { addPetData(it) }
        })
    }


    private fun addPetData(pet: Pet) {
        petG = pet
        toolbar_pet_name.text = pet.name
        petNameText.text = pet.name

        try {
            if (pet.petImage != "null") {
                val imgUri: Uri = Uri.parse(pet.petImage)
                loadImage(imgUri)
            } else {
                pImage.setImageResource(R.drawable.ic_pets_white_24dp)
            }
        } catch (e: Exception) {
            pImage.setImageResource(R.drawable.ic_pets_white_24dp)

        }

        if (pet.breederName == "") visGone(breederNameLayout)
        else petBreederNameText.text = pet.breederName

        if (pet.race == "") visGone(raceLayout)
        else petRaceText.text = pet.race

        if (pet.birthDay == "") {
            visGone(petAgeLayout)
            visGone(petAgeText)
        } else {
            petBirthdateText.text = pet.birthDay
            petAgeText.text = "${calculateAge(pet.birthDay)}"
        }

        when (pet.sex) {
            getString(R.string.pet_sex_female) -> petSexImage.setImageDrawable(getDrawable(R.mipmap.ic_female_icon))
            getString(R.string.pet_sex_male) -> petSexImage.setImageDrawable(getDrawable(R.mipmap.ic_male_icon))
        }

        if (!pet.neutered) visGone(neuteredText)

        if (pet.insuranceProvider == "" && pet.insuranceNumber == "") visGone(petInsuranceInfoCard)

        if (pet.insuranceProvider == "") visGone(insuranceProviderLayout)
        else petInsuranceProviderText.text = pet.insuranceProvider

        if (pet.insuranceNumber == "") visGone(insuranceNumberLayout)
        else petInsuranceNumberText.text = pet.insuranceNumber

    }

    private fun visGone(view: View) {
        view.visibility = View.GONE
    }

    private fun loadImage(image: Any) {
        Glide.with(this).load(image).into(pImage)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                startActivity(
                    Intent(this@PetActivity, AddPetActivity::class.java).putExtra(
                        "id",
                        petG.id
                    )
                )
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
        alert.setMessage(String.format(getString(R.string.alert_delete_message), petG.name))

        alert.setPositiveButton(getString(R.string.action_delete)) { _, _ -> deletePet() }

        alert.setNegativeButton(getString(R.string.alert_cancel)) { _, _ -> }

        alert.show()

    }

    private fun deletePet() {

        petViewModel.deletePet(petG)
        Toast.makeText(
            this@PetActivity,
            String.format(getString(R.string.alert_delete_toast, petG.name)),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@PetActivity, MainActivity::class.java))

    }
}
