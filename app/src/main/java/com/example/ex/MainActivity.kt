package com.example.ex

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

import android.view.MenuItem
import android.view.View
import android.view.View.inflate
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.ex.adapter.studentslist
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerLayout : DrawerLayout
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var myTextView6: TextView
    private lateinit var myTextView7: TextView



    // send variables to other places
    companion object {
        var name: String = ""
        var email: String = ""
        var uid: String = ""
        var role: String = ""


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(HomeFragment(), title.toString())

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        // set variables to send
        val user = mAuth.currentUser
        email = user?.email ?: ""
        uid = user?.uid ?: ""

        mDbRef.child("user").child(uid).child("role").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                role = (task.result?.value as? String).toString()

            }
        }
        mDbRef.child("user").child(uid).child("name").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                name = (task.result?.value as? String).toString()
                updateNavigationHeader()

            }
        }
        //----------------------------------------------------------------------------------

        //Change menu names



        //Drawer for slideable menu
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        // Menu set up and what each logo will do
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                //Set up what is done with the click of each icon
                R.id.nav_home ->  replaceFragment(HomeFragment(), it.title.toString())
                R.id.nav_message -> intenthold(main())
                R.id.nav_sync -> replaceFragment(Profile(), it.title.toString())
                R.id.nav_trash -> intenthold(Work())
                R.id.nav_settings -> intenthold(studentslist())
                R.id.nav_LogIn -> logOut()
                R.id.nav_share -> replaceFragment(Comments(), it.title.toString())
                R.id.nav_rate_us -> Toast.makeText(applicationContext,"clicked Rate",Toast.LENGTH_SHORT).show()
            }

            it.isChecked = true
            drawerLayout.close()

            true
        }
    }



    private fun replaceFragment(fragment: Fragment, title : String){

        //Change fragment displaying in the screen
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        setTitle(title)
    }


    //function to change activity without causing chaos
    private fun intenthold(place: Activity){
        //Change Activity without affecting fragment change
        val intent = Intent(this@MainActivity, place::class.java)
        startActivity(intent)
    }


    private fun updateNavigationHeader() {
        // change the name and email on navigation menu
        val navView : NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navView.getHeaderView(0)
        myTextView6 = headerView.findViewById(R.id.user_name1)
        myTextView6.text = name
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        if(item.itemId == R.id.logout){

            mAuth.signOut()
            val intent = Intent(this@MainActivity,LogIn::class.java)
            finish()
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    private fun logOut(){
        mAuth.signOut()
        val intent = Intent(this@MainActivity,LogIn::class.java)
        finish()
        startActivity(intent)
    }
}