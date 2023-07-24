package com.example.ex.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.ex.MainActivity.Companion.name
import com.example.ex.R
import com.example.ex.uitel.getProgressDrawable
import android.widget.ImageView
import android.widget.TextView
import com.example.ex.adapter.studentslist
import com.example.ex.main
import com.example.ex.uitel.loadImage

class NewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /* Get Data */
        val workIntent = intent
        val workName = workIntent.getStringExtra("name")
        val workText = workIntent.getStringExtra("text")
        val workImage = workIntent.getStringExtra("img")

        /* Call Text and Images */
        val nameTextView: TextView = findViewById(R.id.name)
        val textView: TextView = findViewById(R.id.text)
        val imageView: ImageView = findViewById(R.id.image)

        nameTextView.text = workName
        textView.text = workText
        workImage?.let {
            imageView.loadImage(it, getProgressDrawable(this))
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, studentslist::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}