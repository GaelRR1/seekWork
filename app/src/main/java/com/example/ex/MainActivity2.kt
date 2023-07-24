package com.example.ex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity2 : AppCompatActivity() {

    private lateinit var nameAdd: EditText
    private lateinit var uidAdd: EditText
    private lateinit var changeTeacher: Button
    private lateinit var changeStudent: Button
    private lateinit var buttonAdd: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        //Display action bar and name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add"


        //Variables

        nameAdd = findViewById(R.id.nameAdd)
        uidAdd = findViewById(R.id.uidAdd)
        changeStudent = findViewById(R.id.changeStudent)
        changeTeacher = findViewById(R.id.changeTeacher)
        buttonAdd = findViewById(R.id.buttonAdd)
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        var newRole = "Student"

        changeTeacher.setOnClickListener {
            newRole = "Teacher"
            Toast.makeText(applicationContext,"Change Role to: $newRole", Toast.LENGTH_SHORT).show()
        }

        changeStudent.setOnClickListener {
            newRole = "Student"
            Toast.makeText(applicationContext,"Change Role to: $newRole",Toast.LENGTH_SHORT).show()
        }

        buttonAdd.setOnClickListener {
            change(newRole)
        }




    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun change(newRole: String){
        val writeName = nameAdd.text.toString()
        val writeUid = uidAdd.text.toString()

        mDbRef.child("user").child(writeUid).child("name").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val name = task.result?.value as? String
                if (name == writeName){
                    val userRef = mDbRef.child("user").child(writeUid).child("role")
                    userRef.setValue(newRole)
                } else {
                    Toast.makeText(applicationContext,"Not same name",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext,"Error retrieving data",Toast.LENGTH_SHORT).show()
            }
        }
    }
}