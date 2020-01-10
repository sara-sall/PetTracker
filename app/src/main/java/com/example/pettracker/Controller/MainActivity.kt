package com.example.pettracker.Controller

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pettracker.Adapters.PetListAdapter
import com.example.pettracker.R
import com.example.pettracker.Services.DataService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: PetListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarID)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)

        adapter = PetListAdapter(this, DataService.pets){
            pet ->  startActivity(Intent(this@MainActivity, PetActivity::class.java))//Toast.makeText(this, pet.name, Toast.LENGTH_SHORT).show()
        }
        recyclerViewID.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        recyclerViewID.layoutManager = layoutManager

        fab.setOnClickListener { view -> startActivity(Intent(this@MainActivity, AddPetActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
