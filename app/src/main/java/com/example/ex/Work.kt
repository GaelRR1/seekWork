
package com.example.ex

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ex.databinding.ActivityWorkBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class Work : AppCompatActivity() {

    private lateinit var binding: ActivityWorkBinding
    private lateinit var nameUP: EditText
    private lateinit var textUP: EditText

    private val imageReference = Firebase.storage.reference

    private var currentFile: Uri? = null
    private var filename = ""

    private var subject1 = "Announcement"
    private var count = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameUP = findViewById(R.id.nameUP)
        textUP = findViewById(R.id.textUP)

        val categories = arrayOf("Code", "Document", "Work Description", "Announcement")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        binding.categorySpinner.adapter = adapter

        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                subject1 = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                subject1 = "all"
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.imageView.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                imageLauncher.launch(it)
            }
        }

        binding.btnUpload.setOnClickListener {
            val name1 = nameUP.text.toString()
            val text1 = textUP.text.toString()
            count = count +1
            val newName = "$name1 ___$count"

            uploadImageToStorage(newName).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result.toString()
                    uploadFolder(newName, subject1, text1, downloadUrl)
                }
            }
        }
    }

    private val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let {
                currentFile = it
                filename = getFilenameFromUri(this, it).toString()
                binding.imageView.setImageURI(it)
            }
        } else {
            Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadFolder(name: String, subject: String, text: String, downloadUrl: String) {
        val database = FirebaseDatabase.getInstance()
        val imagesRef = database.getReference("images")
        val folderRef = imagesRef.push()
        folderRef.child("name").setValue(name)
        folderRef.child("subject").setValue(subject)
        folderRef.child("text").setValue(text)
        folderRef.child("image").setValue(downloadUrl)
    }

    private fun uploadImageToStorage(filename: String): Task<Uri> {
        return currentFile?.let {
            val fileRef = imageReference.child("images/$filename")
            val uploadTask = fileRef.putFile(it)
            uploadTask.continueWithTask { task ->
                fileRef.downloadUrl
            }
        } ?: throw IllegalArgumentException("Current file is null")
    }

    private fun getFilenameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return fileName
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}