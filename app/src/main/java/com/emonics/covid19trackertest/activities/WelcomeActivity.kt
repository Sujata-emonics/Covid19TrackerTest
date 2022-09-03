package com.emonics.covid19trackertest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.emonics.covid19trackertest.R
import com.emonics.covid19trackertest.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {
    lateinit var viewBinding:ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_welcome)
        viewBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.btnAddCity.setOnClickListener {
            startAdminActivity()

        }

        viewBinding.tvCovidGlobalLink.setOnClickListener {
            startGlobalActivity()
        }

       viewBinding.btnLogOut.setOnClickListener {
           signOut()
        }
    }

    fun startAdminActivity(){
        var intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }

    fun startGlobalActivity(){
        var intent = Intent(this, DetailActivity::class.java)
        startActivity(intent)
    }

    fun signOut(){
        Log.i("tag_","signOut")
        var mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()

        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}