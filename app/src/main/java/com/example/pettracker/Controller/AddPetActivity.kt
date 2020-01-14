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
import com.example.pettracker.R
import kotlinx.android.synthetic.main.content_add_pet.*
import java.text.SimpleDateFormat
import java.util.*

class AddPetActivity : AppCompatActivity(), View.OnClickListener{


    private lateinit var nameInput : EditText
    private lateinit var breederInput : EditText
    private lateinit var idInput : EditText
    private lateinit var raceInput : EditText

   // private lateinit var dateSetListener: object

    private lateinit var cal :Calendar
    private lateinit var birthDateInput : EditText

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

        nameInput = this.petNameInput
        breederInput = this.petBreederNameInput
        idInput = this.petIdNumberInput
        raceInput = this.petRaceInput

        birthDateInput = this.petAgeInput
        birthDateInput.setOnClickListener(this)

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
            R.id.petAgeInput->{
                val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_YEAR, dayOfMonth)
                        updateDateInView()
                    }


                }
                DatePickerDialog(this@AddPetActivity, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
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
                Log.d("PET","Name = $petName, BreederName = $petBreederName, Id = $petId, Race = $petRace, Sex = $petSex, Neutered = $isNeutered" )
            }
        }


        }

        private fun updateDateInView(){
            val myFormat = "dd-MM-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
            var editable : Editable
            editable = SpannableStringBuilder(sdf.format(cal.time))
            petAgeInput.text = editable
    }
}
