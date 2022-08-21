package com.emonics.covid19trackertest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.emonics.covid19trackertest.R
import com.emonics.covid19trackertest.helpers.validation.ForgotPasswordFormEvent
import com.emonics.covid19trackertest.helpers.validation.RegistrationFormEvent
import com.emonics.covid19trackertest.viewModel.ForgotPassWordViewModel
import com.emonics.covid19trackertest.viewModel.SignInValidationViewModel
import kotlinx.coroutines.launch

class ForgotPassWordActivity : AppCompatActivity() {
    lateinit var forgotPassWordViewModel:ForgotPassWordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        initViewModel()//Intialize the forgotpassword View Model
        val state = forgotPassWordViewModel.stateForgotPassword
        val context = this.applicationContext
        //Code will execute Getting successs from the view after validation
        //Putting textChange functionality on Email field
           var edPassword = findViewById<EditText>(R.id.edPassword)
           var edOldPasswordError = findViewById<TextView>(R.id.edOldPasswordError)
        if (state.passwordErrorForgotPassword != null&&state.passwordErrorForgotPassword != "") {
            edPassword.error = forgotPassWordViewModel.stateForgotPassword.passwordErrorForgotPassword.toString()
        }
        forgotPassWordViewModel.passwordErrorLiveDataForgotPassword.observe(this, Observer {
            if(it.toString()!= null && it.toString()!= ""){
                edOldPasswordError.setTextColor(getResources().getColor(R.color.error_msg))
                edOldPasswordError.text = it.toString()
                edOldPasswordError.visibility = View.VISIBLE
            } else{
                edOldPasswordError.setTextColor(getResources().getColor(R.color.white))
                edOldPasswordError.visibility = View.INVISIBLE

            }
        })

        //Intializing the password and password error Field
        var edConfirmedPassword = findViewById<EditText>(R.id.edConfirmedPassword)
        var edNewPasswordError = findViewById<TextView>(R.id.edNewPasswordError)
        if (state.confirmedPasswordForgotPassword != null&&state.confirmedPasswordForgotPassword != "") {
            edConfirmedPassword.error = forgotPassWordViewModel.stateForgotPassword.confirmedPasswordErrorForgotPassword.toString()
        }
        forgotPassWordViewModel.repeatPasswordErrorLiveDataForgotPassword.observe(this, Observer {
            if(it.toString()!= null && it.toString()!= ""){
                edNewPasswordError.setTextColor(getResources().getColor(R.color.error_msg))
                edNewPasswordError.text = it.toString()
                edNewPasswordError.visibility = View.VISIBLE
            } else{
                edNewPasswordError.setTextColor(getResources().getColor(R.color.white))
                edNewPasswordError.visibility = View.INVISIBLE

            }
        })


        edPassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                //edEmail.error = viewModelValidation.state.emailError.toString()
                forgotPassWordViewModel.onEventForgotPassword(ForgotPasswordFormEvent.PasswordChangedForgotPassword(edPassword.text.toString()))

            }
        })

        //TextChange functionality in Password field
        edConfirmedPassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                forgotPassWordViewModel.onEventForgotPassword(ForgotPasswordFormEvent.ConfirmedPasswordChangedForgotPassword(edConfirmedPassword.text.toString()))
                //EdPasswordError.setText(viewModelValidation.state.passwordError.toString())

            }
        })


        var forgotPasswordSuccessLayout = findViewById<LinearLayout>(R.id.forgotPasswordSuccessLayout)
        var forgetPasswordLayout = findViewById<LinearLayout>(R.id.forgetPasswordLayout)
        var linkSingnIn = findViewById<TextView>(R.id.linkSingnIn)
        linkSingnIn.setOnClickListener {
            //var intent = Intent(this, MainActivity::class.java).apply()
            //startActivity(intent)
            finish()
        }

        lifecycleScope.launch(){
            forgotPassWordViewModel.validationEvents.collect { event ->
                when (event) {
                    is ForgotPassWordViewModel.ValidationEventForgotPassword.Success -> {
                        forgetPasswordLayout.visibility = View.GONE
                       findViewById<TextView>(R.id.linkSingnIn).visibility = View.GONE
                        forgotPasswordSuccessLayout.visibility = View.VISIBLE
                        var linkSingnInSuccess = findViewById<TextView>(R.id.linkSingnInSuccess)
                        linkSingnInSuccess.setOnClickListener {
                            //var intent = Intent(this, MainActivity::class.java).apply()
                            //startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }

        var changePassword = findViewById<Button>(R.id.changePassword)
        changePassword.setOnClickListener {
            forgotPassWordViewModel.submitData()
        }
    }

    fun initViewModel(){
        forgotPassWordViewModel = ViewModelProvider(this).get(ForgotPassWordViewModel::class.java)
     }
}