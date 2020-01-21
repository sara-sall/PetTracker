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
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.card_general_add_pet.*
import kotlinx.android.synthetic.main.card_insurance_add_pet.*
import kotlinx.android.synthetic.main.card_veterinary_add_pet.*
import java.util.*

class AddPetActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var db: PetRoomDatabase

    private lateinit var toolbar: Toolbar
    private lateinit var toolbarTitle : TextView

    private var petUUID: String = ""

    private lateinit var petImage : ImageView
    private lateinit var addPetImageButton : ImageView
    private lateinit var deletePetImageButton : ImageView
    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_CODE = 1001
    private var imageUri :Uri? = null
    private lateinit var uniqueId: String

    private lateinit var nameInput : EditText
    private lateinit var breederInput : EditText
    private lateinit var idInput : EditText
    private lateinit var raceInput : EditText

    private lateinit var cal :Calendar
    private lateinit var birthDateInput : EditText
    private lateinit var birthDateLayout : TextInputLayout
    private var petBirthDate: String = ""

    private lateinit var femaleButton : Button
    private lateinit var maleButton : Button
    private lateinit var petSexError : EditText
    private lateinit var neuteredCheck : CheckBox

    private lateinit var insProviderInput : EditText
    private lateinit var insNrInput : EditText

    private lateinit var vaccineDate : EditText

    private lateinit var saveButton : Button


    private lateinit var petSex :String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pet)

        toolbar = findViewById(R.id.toolbarID)
        toolbar.title= getString(R.string.empty)
        this.setSupportActionBar(toolbar)
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        petImage = this.petImageView
        petImage.setOnClickListener(this)
        addPetImageButton = this.addImageID
        addPetImageButton.setOnClickListener(this)
        deletePetImageButton = this.removeImageID
        deletePetImageButton.setOnClickListener(this)

        nameInput = this.petNameInput
        breederInput = this.petBreederNameInput
        idInput = this.petIdNumberInput
        raceInput = this.petRaceInput

        birthDateInput = this.petAgeInput
        birthDateInput.setOnClickListener(this)
        birthDateLayout = this.textInputLayoutAge
        birthDateLayout.setOnClickListener(this)

        femaleButton = this.buttonFemale
        femaleButton.setOnClickListener(this)
        maleButton = this.buttonMale
        maleButton.setOnClickListener(this)
        petSexError = this.errorPetSex
        neuteredCheck = this.neuteredCheckbox

        insProviderInput = this.insuranceProviderInput
        insNrInput = this.insuranceNumberInput

        vaccineDate = this.vaccineDateInput0
        vaccineDate.setOnClickListener(this)

        saveButton = this.buttonSave
        saveButton.setOnClickListener(this)

        petSex =""

        cal = Calendar.getInstance()

        db = Room.databaseBuilder(applicationContext, PetRoomDatabase::class.java, "pets").build()



    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        var petName = nameInput.text.toString().trim()
        var petBreederName = breederInput.text.toString().trim()
        var petId = idInput.text.toString().trim()
        var petRace = raceInput.text.toString().trim()
        var isNeutered = false

        if (neuteredCheck.isChecked){
            isNeutered = true
        }

        when (v?.id){
            R.id.addImageID->checkPermission()
            R.id.petImageView->checkPermission()
            R.id.removeImageID->{
                imageUri = null
                deletePetImageButton.visibility = View.GONE
                Glide.with(this).load(getDrawable(R.drawable.ic_pets_white_24dp)).into(petImageView)

            }
            R.id.petAgeInput -> {
                pickDate(birthDateInput)

            }
            R.id.textInputLayoutAge -> {
                pickDate(birthDateInput)
                petBirthDate = birthDateInput.text.toString()
            }
            R.id.buttonFemale -> {
                petSex = getString(R.string.pet_sex_female)
                femaleButton.background = getDrawable(R.drawable.button_frame_light)
                maleButton.background = getDrawable(R.drawable.button_frame_dark)
                petSexError.error = null

            }
            R.id.buttonMale -> {
                petSex = getString(R.string.pet_sex_male)
                maleButton.background = getDrawable(R.drawable.button_frame_light)
                femaleButton.background = getDrawable(R.drawable.button_frame_dark)
                petSexError.error = null

            }
            R.id.vaccineDateInput0->pickDate(vaccineDate)
            R.id.buttonSave -> {
                if(petName==""){
                    nameInput.error = getString(R.string.input_error_pet_name)
                    nameInput.requestFocus()
                    return
                }
                if(petSex == ""){
                    petSexError.error = getString(R.string.input_error_pet_sex)
                    petSexError.requestFocus()
                    return
                }
                Log.d("PETS", "petUUID 1: ${petUUID}")

                if (petUUID == "") {
                    petUUID = UUID.randomUUID().toString()
                    Log.d("PETS", "generate petUUID: ${petUUID}")
                }

                var pet = Pet(
                    petUUID,
                    petName,
                    imageUri.toString(),
                    petBreederName,
                    petRace,
                    petBirthDate,
                    petSex,
                    isNeutered
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
                    "Name = $petName, BreederName = $petBreederName, Id = $petId, Race = $petRace, Birthdate = ${birthDateInput.text} Sex = $petSex, Neutered = $isNeutered"
                )
            }
        }


        }

    private fun pickDate(inputField: EditText) {
        var editable: Editable? = null
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                openFileChooser()
            }
        }
        else{
            openFileChooser()
        }
    }

    private fun openFileChooser(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE->{
                if (grantResults.size >0 && grantResults[0] ==
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
            deletePetImageButton.visibility = View.VISIBLE
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
