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
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.petscope.lukeedgar.petscope.Adapters.AnimalCardAdapter
import com.petscope.lukeedgar.petscope.Animals.Animal
import com.petscope.lukeedgar.petscope.R.id.async
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseSnapshot: DataSnapshot? = null
    private var ref = database.reference.child("Animals")
    private var animalList: ArrayList<Animal> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: android.support.v7.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Hamburger menu setup
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
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
                //Return Query results in recyclerview
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
        when (item.itemId) {
            R.id.nav_all_animals -> {
                // List all animals
                getAllAnimals()
            }
            R.id.nav_dogs -> {
                //List Dogs
                "dog".animalListQuery()
            }
            R.id.nav_cats -> {
                //List Cats
                "cat".animalListQuery()
            }
            R.id.nav_other_animals -> {
                //List other animals that are not cats or dogs Examples: Lions, Tigers and Bears (oh my!)
                databaseSnapshot?.children
                        ?.forEach {
                            val animal = it.getValue(Animal::class.java)
                            if ("cat" != animal?.toString()!! || "Cat" != animal.toString() || "Dog" != animal.toString() || "dog" != animal.toString()) {
                                animalList.add(animal)
                            }
                        }
            }
            R.id.nav_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
                intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/l77e/Petscope")
                startActivity(Intent.createChooser(intent, "Share this app"))
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun firebaseListener() {
        // Read from the database
        ref.orderByPriority()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dataSnapshot.children
                        .mapNotNullTo(animalList) { it.getValue(Animal::class.java) }

                databaseSnapshot = dataSnapshot

                animalList.fillRecyclerView()

                prbLoad.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                throw error("Failed to update")
            }
        })
    }

    private fun getAllAnimals() {
        databaseSnapshot
                ?.children
                ?.mapNotNullTo(animalList) { it.getValue(Animal::class.java) }
        //Update the RecyclerView
        animalList.fillRecyclerView()
    }

    fun String.animalListQuery() = if (this != "" || databaseSnapshot != null) {
        val queriedList = ArrayList<Animal>()
        Runnable {
            databaseSnapshot
                    ?.children
                    ?.filter {
                        it.getValue(Animal::class.java)
                                .toString()
                                .toLowerCase()
                                .containsWords(this)
                    }
                    ?.mapNotNullTo(queriedList) { it.getValue(Animal::class.java) }
            queriedList.fillRecyclerView()
        }.run()

    } else {
        animalList.fillRecyclerView()
    }

    private fun String.containsWords(string: String): Boolean{
        val splitedWords = string.split(" ")
        for (word in splitedWords){
            if (this.contains(word)){
                return true
            }
        }
        return false
    }

    fun List<Animal>.fillRecyclerView() {
        rcvAnimalCards.adapter = AnimalCardAdapter(applicationContext, this)
        rcvAnimalCards.layoutManager = LinearLayoutManager(this@MainActivity)
    }

}