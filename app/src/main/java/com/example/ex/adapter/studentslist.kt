package com.example.ex.adapter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ex.MainActivity
import com.example.ex.R
import com.example.ex.databinding.ActivityStudentslistBinding
import com.example.ex.main
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class studentslist : AppCompatActivity() {
    lateinit var mDataBase: DatabaseReference
    private lateinit var workList: ArrayList<WorkData>
    private lateinit var mAdapter: MyAdapter
    private lateinit var recyclerWork: RecyclerView
    private lateinit var binding: ActivityStudentslistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentslistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        workList = ArrayList()
        mAdapter = MyAdapter(this, workList)
        recyclerWork = binding.recyclerWork
        recyclerWork.layoutManager = LinearLayoutManager(this)
        recyclerWork.setHasFixedSize(true)
        recyclerWork.adapter = mAdapter

        binding.filterText.addTextChangedListener { subjectFilter ->
            val workFilter = workList.filter { work -> work.subject?.lowercase()?.contains(subjectFilter.toString().lowercase()) == true }
            mAdapter.updatework(workFilter as ArrayList<WorkData>)
        }

        getWorkData()
    }

    private fun getWorkData() {
        mDataBase = FirebaseDatabase.getInstance().getReference("images")
        mDataBase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    workList.clear()
                    for (workSnapshot in snapshot.children) {
                        val work = workSnapshot.getValue(WorkData::class.java)
                        work?.let {
                            workList.add(it)
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@studentslist, error.message, Toast.LENGTH_SHORT).show()
            }
        })
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
}