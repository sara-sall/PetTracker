package com.example.pettracker.Controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pettracker.Adapters.PetListAdapter
import com.example.pettracker.Database.Pet
import com.example.pettracker.Database.PetViewModel
import com.example.pettracker.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pet.toolbar_pet_name

class MainActivity : AppCompatActivity() {

    lateinit var adapter: PetListAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var mPetViewModel: PetViewModel
    private var pets: ArrayList<Pet> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbarID)
        toolbar.title= getString(R.string.empty)
        this.setSupportActionBar(toolbar)
        toolbar_pet_name.text = getString(R.string.app_name)


        Thread {
            mPetViewModel = ViewModelProviders.of(this).get(PetViewModel::class.java)
            pets = mPetViewModel.allPets
            adapter.updatePetsList(pets)
            Log.d("PETS", "on Create2: $pets")
        }.start()


        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)

        adapter = PetListAdapter(this, pets) { pet ->
            startActivity(
                Intent(this@MainActivity, PetActivity::class.java).putExtra(
                    "id",
                    pet.id
                )
            )
            Log.d("PETS", "$pet.petImage")
        }
        recyclerViewID.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        recyclerViewID.layoutManager = layoutManager

        fab.setOnClickListener { view -> startActivity(Intent(this@MainActivity, AddPetActivity::class.java))
        }
    }

}
