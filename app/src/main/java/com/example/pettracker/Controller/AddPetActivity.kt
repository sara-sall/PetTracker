package com.example.pettracker.Controller

import android.app.DatePickerDialog
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
import com.example.pettracker.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_add_pet.*
import kotlinx.android.synthetic.main.content_add_pet.*
import kotlinx.android.synthetic.main.content_pet.*
import java.text.SimpleDateFormat
import java.util.*

class AddPetActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarTitle : TextView

    private lateinit var nameInput : EditText
    private lateinit var breederInput : EditText
    private lateinit var idInput : EditText
    private lateinit var raceInput : EditText

    private lateinit var cal :Calendar
    private lateinit var birthDateInput : EditText
    private lateinit var birthDateLayout : TextInputLayout

    private lateinit var femaleButton : Button
    private lateinit var maleButton : Button
    private lateinit var petSexError : EditText
    private lateinit var neuteredCheck : CheckBox

    private lateinit var insProviderInput : EditText
    private lateinit var insNrInput : EditText

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

        saveButton = this.buttonSave
        saveButton.setOnClickListener(this)

        petSex =""

        cal = Calendar.getInstance()




    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        var petName = nameInput.text.toString().trim()
        var petBreederName = breederInput.text.toString().trim()
        var petId = idInput.text.toString().trim()
        var petRace = raceInput.toString().trim()
        var isNeutered = false

        if (neuteredCheck.isChecked){
            isNeutered = true
        }

        when (v?.id){
            R.id.petAgeInput-> pickDate()
            R.id.textInputLayoutAge-> pickDate()

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
                Log.d("PET3","Name = $petName, BreederName = $petBreederName, Id = $petId, Race = $petRace, Sex = $petSex, Neutered = $isNeutered" )
            }
        }


        }

        private fun pickDate(){
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_YEAR, dayOfMonth)
                    var editable : Editable
                    editable = SpannableStringBuilder("$dayOfMonth-${month+1}-$year")
                    petAgeInput.text = editable
                   // updateDateInView()
                    Log.d("PET1", "$year, $month, $dayOfMonth")
                    Log.d("PETspan",editable.toString())
                }
            DatePickerDialog(this@AddPetActivity, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            Log.d("PET2", "${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH)+1}-${cal.get(Calendar.YEAR)}")
        }

        private fun updateDateInView(){
            val myFormat = "dd-mm-yyyy"
            val sdf = SimpleDateFormat(myFormat)

            //editable = SpannableStringBuilder(sdf.format(cal.time))
           // editable = SpannableStringBuilder("${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH)+1}-${cal.get(Calendar.YEAR)}")

            //
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
