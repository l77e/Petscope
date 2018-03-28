package com.petscope.lukeedgar.petscope

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.petscope.lukeedgar.petscope.Adapters.AnimalCardAdapter
import com.petscope.lukeedgar.petscope.Animals.Animal
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var databaseSnapshot: DataSnapshot? = null
    var ref = database.reference
    var animalList: ArrayList<Animal>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: android.support.v7.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Hamburger menu setup
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        //On floatingActionButton click then launch add item activity
        fabAddAnimal.setOnClickListener {
            val intent = Intent(applicationContext, AddActivity::class.java)
            startActivity(intent)
        }
        //Start firebase listener
        firebaseListener()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String): Boolean {
                //Return Query resuilts in recyclerview
                query.toLowerCase().animalListQuery()
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                query.toLowerCase().animalListQuery()
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        animalList = ArrayList()
        when (item.itemId) {
            R.id.nav_all_animals -> {
                // List all animals
                getAllAnimals()
            }
            R.id.nav_dogs -> {
                //List Dogs
                databaseSnapshot?.children
                        ?.forEach {
                            val animal = it.getValue(Animal::class.java)
                            if (animal?.animal_type == "dog" || animal?.animal_type == "Dog") {
                                animalList?.add(animal)
                            }
                        }
            }
            R.id.nav_cats -> {
                //List Cats
                databaseSnapshot?.children
                        ?.forEach {
                            val animal = it.getValue(Animal::class.java)
                            if (animal?.animal_type == "Cat" || animal?.animal_type == "Cat") {
                                animalList?.add(animal)
                            }
                        }
            }
            R.id.nav_other_animals -> {
                //List other animals that are not cats or dogs Examples: Lions, Tigers and Bears (oh my!)
                databaseSnapshot?.children
                        ?.forEach {
                            val animal = it.getValue(Animal::class.java)
                            if ("cat" in animal?.toString()!! || "Cat" in animal.toString() || "Dog" in animal.toString() || "dog" in animal.toString()) {
                                animalList?.add(animal)
                            }
                        }
            }
            R.id.nav_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                startActivity(Intent.createChooser(intent, "Share this app"))
            }
            R.id.nav_send -> {

            }
        }
        animalList!!.fillRecyclerView()
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun firebaseListener() {
        // Read from the database
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataSnapshot.children
                        ?.sortedByDescending { x -> x.child("Animal_Name").value.toString() }
                        ?.forEach {
                            val animal = it.getValue(Animal::class.java)
                            animalList?.add(animal!!)
                        }

                databaseSnapshot = dataSnapshot

                animalList!!.fillRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                throw error("Failed to update")
            }
        })
    }

    fun getAllAnimals() {
        databaseSnapshot
                ?.children
                ?.forEach {
                    val animal = it.getValue(Animal::class.java)
                    if (animal?.Animal_Name != "") {
                        animalList?.add(animal!!)
                    }
                }
        //Update the recyclerview
        animalList!!.fillRecyclerView()
    }

    fun String.animalListQuery() = if (this != "" || databaseSnapshot != null) {
        val queriedList = ArrayList<Animal>()
        databaseSnapshot
                ?.children
                ?.filter {
                    it.getValue(Animal::class.java)
                            .toString()
                            .toLowerCase()
                            .trim()
                            .contains(this)
                }
                ?.forEach { queriedList += it.getValue(Animal::class.java)!! }
        queriedList.fillRecyclerView()
    } else {
        animalList!!.fillRecyclerView()
    }

    fun List<Animal>.fillRecyclerView() {
        rcvAnimalCards.adapter = AnimalCardAdapter(applicationContext, this)
        rcvAnimalCards.layoutManager = LinearLayoutManager(this@MainActivity)
        val list = ArrayList<Animal>()
        this.forEach { list.add(it) }
    }

}