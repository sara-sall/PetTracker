package com.example.pettracker.Controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pettracker.Adapters.PetListAdapter
import com.example.pettracker.Database.PetViewModel
import com.example.pettracker.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pet.toolbar_pet_name

class MainActivity : AppCompatActivity() {

    lateinit var adapter: PetListAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var mPetViewModel: PetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbarID)
        toolbar.title= getString(R.string.empty)
        this.setSupportActionBar(toolbar)
        toolbar_pet_name.text = getString(R.string.app_name)

        mPetViewModel = ViewModelProviders.of(this).get(PetViewModel::class.java)
        mPetViewModel.getAllPets()
        mPetViewModel.pets.observe(this, Observer {
            adapter = PetListAdapter(this, it) { pet ->
                startActivity(
                    Intent(this@MainActivity, PetActivity::class.java).putExtra(
                        "id",
                        pet.id
                    )
                )
            }

            adapter.updatePetsList(it)
            recyclerViewID.adapter = adapter
        })

        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)

        val layoutManager = LinearLayoutManager(this)
        recyclerViewID.layoutManager = layoutManager

        fab.setOnClickListener {startActivity(Intent(this@MainActivity, AddPetActivity::class.java)) }
    }

    override fun onBackPressed() {
        val a =  Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(a)
    }

}
