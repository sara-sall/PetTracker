package com.example.pettracker.Controller

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.pettracker.Database.Pet
import com.example.pettracker.Database.PetRoomDatabase
import com.example.pettracker.Database.PetViewModel
import com.example.pettracker.R
import kotlinx.android.synthetic.main.activity_pet.*
import kotlinx.android.synthetic.main.card_general_add_pet.*
import kotlinx.android.synthetic.main.card_insurance_add_pet.*
import kotlinx.android.synthetic.main.card_veterinary_add_pet.*
import java.util.*

class AddPetActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var db: PetRoomDatabase
    private lateinit var toolbar: Toolbar
    private var petUUID: String = ""
    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_CODE = 1001
    private var imageUri :Uri? = null
    private lateinit var cal :Calendar
    private var petBirthDate: String = ""
    private var petSex =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        toolbar = findViewById(R.id.toolbarID)
        toolbar.title= getString(R.string.empty)
        this.setSupportActionBar(toolbar)
        toolbar_pet_name.text = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        petImageView.setOnClickListener(this)
        addImageID.setOnClickListener(this)
        removeImageID.setOnClickListener(this)

        petAgeInput.setOnClickListener(this)
        textInputLayoutAge.setOnClickListener(this)

        buttonFemale.setOnClickListener(this)
        buttonMale.setOnClickListener(this)

        vaccineDateInput0.setOnClickListener(this)

        buttonSave.setOnClickListener(this)

        cal = Calendar.getInstance()

        db = Room.databaseBuilder(applicationContext, PetRoomDatabase::class.java, "pets").build()



    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {

        when (v?.id){
            R.id.addImageID->checkPermission()
            R.id.petImageView->checkPermission()
            R.id.removeImageID->{
                imageUri = null
                removeImageID.visibility = View.GONE
                Glide.with(this).load(getDrawable(R.drawable.ic_pets_white_24dp)).into(petImageView)

            }
            R.id.petAgeInput -> {
                pickDate(petAgeInput)

            }
            R.id.textInputLayoutAge -> {
                pickDate(petAgeInput)
                petBirthDate = petAgeInput.text.toString()
            }
            R.id.buttonFemale -> {
                petSex = getString(R.string.pet_sex_female)
                buttonFemale.background = getDrawable(R.drawable.button_frame_light)
                buttonMale.background = getDrawable(R.drawable.button_frame_dark)
                errorPetSex.error = null

            }
            R.id.buttonMale -> {
                petSex = getString(R.string.pet_sex_male)
                buttonMale.background = getDrawable(R.drawable.button_frame_light)
                buttonFemale.background = getDrawable(R.drawable.button_frame_dark)
                errorPetSex.error = null

            }
            R.id.vaccineDateInput0->pickDate(vaccineDateInput0)
            R.id.buttonSave -> { savePet()

            }
        }
    }

    private fun savePet(){
        val petName = petNameInput.text.toString().trim()
        val petBreederName = petBreederNameInput.text.toString().trim()
        val petId = petIdNumberInput.text.toString().trim()
        val petRace = petRaceInput.text.toString().trim()
        var isNeutered = false

        if (neuteredCheckbox.isChecked){
            isNeutered = true
        }

        val insProvider = insuranceProviderInput.text.toString().trim()
        val insNr = insuranceNumberInput.text.toString().trim()


        if(petName==""){
            petNameInput.error = getString(R.string.input_error_pet_name)
            petNameInput.requestFocus()
            return
        }
        if(petSex == ""){
            errorPetSex.error = getString(R.string.input_error_pet_sex)
            errorPetSex.requestFocus()
            return
        }
        Log.d("PETS", "petUUID 1: $petUUID")

        if (petUUID == "") {
            petUUID = UUID.randomUUID().toString()
            Log.d("PETS", "generate petUUID: $petUUID")
        }

        val pet = Pet(
            petUUID,
            petName,
            petId,
            imageUri.toString(),
            petBreederName,
            petRace,
            petBirthDate,
            petSex,
            isNeutered,
            insProvider,
            insNr

        )

        Thread {
            val mPetViewModel = ViewModelProviders.of(this).get(PetViewModel::class.java)
            mPetViewModel.insert(pet)

            startActivity(
                Intent(
                    this@AddPetActivity,
                    PetActivity::class.java
                ).putExtra("id", petUUID)
            )

        }.start()


        Log.d(
            "PET3",
            "Name = $petName, BreederName = $petBreederName, Id = $petId, Race = $petRace, Birthdate = ${petAgeInput.text} Sex = $petSex, Neutered = $isNeutered"
        )

    }

    private fun pickDate(inputField: EditText) {
        var editable: Editable?
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_YEAR, dayOfMonth)
                    editable = SpannableStringBuilder("$year-${month + 1}-$dayOfMonth")
                    inputField.text = editable
                    petBirthDate = editable.toString()
                }
            DatePickerDialog(this@AddPetActivity, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun checkPermission(){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_DENIED){
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, PERMISSION_CODE)
        }
        else{
            openFileChooser()
        }
    }

    private fun openFileChooser(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE->{
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    openFileChooser()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST){
            imageUri = data?.data
            loadImage()
            removeImageID.visibility = View.VISIBLE
        }
    }

    private fun loadImage(){
        Glide.with(this).load(imageUri).into(petImageView)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
