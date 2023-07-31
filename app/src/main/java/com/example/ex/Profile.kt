package com.example.ex

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.ex.MainActivity.Companion.email
import com.example.ex.MainActivity.Companion.name
import com.example.ex.MainActivity.Companion.role
import com.example.ex.MainActivity.Companion.uid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {
    private lateinit var myTextView: TextView
    private lateinit var myTextView1: TextView
    private lateinit var myTextView2: TextView
    private lateinit var myTextView3: TextView
    private lateinit var myTextView4: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var addProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        addProfile = view.findViewById(R.id.addProfile)

        var tr = false

        //Change name on logo

        val firstLetter = name?.capitalize() ?: ""

        //Change name on logo

        myTextView = view.findViewById(R.id.profile5)
        myTextView.text = firstLetter.toString()

        //Change name to actual

        myTextView4 = view.findViewById(R.id.name_profile)
        myTextView4.text = "Name: Hello $name."



        //Change email to actual
        myTextView1 = view.findViewById(R.id.email_profile)
        myTextView1.text = "Email: Your email is \"$email\"."

        //Change uid to actual
        myTextView2 = view.findViewById(R.id.uid_profile)
        myTextView2.text = "Uid: Your uid is \"$uid\"."


        // Change role to Actual
        myTextView3 = view.findViewById(R.id.role_profile)
        myTextView3.text = "Role: Your role is \"$role\" ."

        //Set tr
        if (role == "owner" || role == "teacher") {
            tr = true
        }

        //Button role
        addProfile.setOnClickListener {
            if (tr) {
                val intent = Intent(requireActivity(), MainActivity2::class.java)
                requireActivity().finish()
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Only Authorized Personnel", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}