package com.emonics.covid19trackertest.activities

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.emonics.covid19trackertest.R
import com.emonics.covid19trackertest.dataClass.ValidationResult
import com.emonics.covid19trackertest.databinding.ActivityForgotpasswordBinding
import com.emonics.covid19trackertest.helpers.dbHandler.DBApplication
import com.emonics.covid19trackertest.helpers.validation.ForgotPasswordFormEvent
import com.emonics.covid19trackertest.viewModel.DatabaseViewModel
import com.emonics.covid19trackertest.viewModel.DatabaseViewModelFactory
import com.emonics.covid19trackertest.viewModel.ForgotPassWordViewModel
import com.emonics.covid19trackertest.viewModel.ForgotPasswordViewModelFactory
import kotlinx.android.synthetic.main.activity_forgotpassword.view.*
import kotlinx.coroutines.launch

class ForgotPassWordActivity : AppCompatActivity() {
    lateinit var forgotPassWordViewModel:ForgotPassWordViewModel
    lateinit var viewBinding:ActivityForgotpasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityForgotpasswordBinding.inflate(layoutInflater)
        var view = viewBinding.root
        setContentView(view)

        initViewModel(view)//Intialize the forgotpassword View Model
        //initListeners(view)
        view.changePassword.setOnClickListener {

            forgotPassWordViewModel.onEvent(view.edEmailForgotPassword.text.toString())
        }
        view.tvSignIn.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
   }
    fun initViewModel(view:View) {
        view.forgetPasswordLayout.visibility = VISIBLE
        view.LnAfterMailSentLayOut.visibility = INVISIBLE
        val repository = (application as DBApplication).covidTrackerRepository
        forgotPassWordViewModel = ViewModelProvider(this, ForgotPasswordViewModelFactory(repository)).get(
            ForgotPassWordViewModel::class.java)
        forgotPassWordViewModel.isPasswordChanged.observe(this, Observer {
            Log.i("tag_db","on  create"+it.toString())
            if(it!=""){
                Toast.makeText(this,"password change mail has been sent!!",Toast.LENGTH_SHORT).show()
                view.forgetPasswordLayout.visibility = INVISIBLE
                view.LnAfterMailSentLayOut.visibility = VISIBLE


            }else  {
                    Toast.makeText(this," Error while changing password"+forgotPassWordViewModel.showErrorMessage.value,Toast.LENGTH_SHORT).show()

                }
        }
        )
        forgotPassWordViewModel.showErrorMessage.observe(this, Observer {
            if(it){
                view.ededEmailForgotPasswordError.text = "Please enter valid Email"
            } else {
                view.ededEmailForgotPasswordError.text = ""
            }
        })
    }

    /*private fun initListeners(view:View) {
        view.edEmailForgotPassword.addTextChangedListener {
            //forgotPassWordViewModel.setEmail(it.toString())
        }
    }

     */


}

