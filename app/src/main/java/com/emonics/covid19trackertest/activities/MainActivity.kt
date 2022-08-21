package com.emonics.covid19trackertest.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.emonics.covid19trackertest.R
import com.emonics.covid19trackertest.helpers.validation.RegistrationFormEvent
import com.emonics.covid19trackertest.viewModel.MainActivityViewModel
import com.emonics.covid19trackertest.viewModel.SignInValidationViewModel
import com.emonics.covid19trackertest.viewModel.UserLogInViewModel
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel:MainActivityViewModel //ViewModel handling the switch the option for signup/signIn
    private lateinit var viewModelValidation:SignInValidationViewModel //ViewModel for the validations SignIn/SignUp page
    private lateinit var userLogInViewModel:UserLogInViewModel //ViewModel for the validations SignIn/SignUp page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --Start implementation  for the toggle menu SignI/SignUp ------
        var signUp = findViewById<TextView>(R.id.signUp)
        var logIn = findViewById<TextView>(R.id.logIn)
        var singUpLayout = findViewById<LinearLayout>(R.id.singUpLayout)
        var logInLayout = findViewById<LinearLayout>(R.id.logInLayout)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.toggle.observe(this, Observer {
            if(it.toString() == "SignIn"){
                signUp?.background = null
                signUp.setTextColor(resources.getColor(R.color.toggle_blue))
                logIn.background = resources.getDrawable(R.drawable.switch_track,null)
                singUpLayout.visibility = View.GONE
                logInLayout.visibility = View.VISIBLE
                logIn.setTextColor(resources.getColor(R.color.textColor))
            } else {
                signUp.background = resources.getDrawable(R.drawable.switch_track,null)
                //signUp.setTextColor(resources.getColor(R.color.textColor,null))
                signUp?.setTextColor(resources.getColor(R.color.textColor))
                logIn?.background = null
                singUpLayout.visibility = View.VISIBLE
                logInLayout.visibility = View.GONE
                logIn.setTextColor(resources.getColor(R.color.toggle_blue))
                //viewModel.changeToggle()
            }
        })

        signUp.setOnClickListener {
            viewModel.changeToggle("SignUp")
        }
        logIn.setOnClickListener {
            viewModel.changeToggle("SignIn")

        }
        /* --------------------- */

        /* Social Media Link */
        var faceBookLink:ImageView = findViewById(R.id.link_fb)
        faceBookLink.setOnClickListener {
            //LogIn to FaceBook
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.facebook.com/")
            startActivity(openURL)

        }

       var googleLink:ImageView = findViewById(R.id.link_google)
        googleLink.setOnClickListener {
            //LogIn to FaceBook
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.google.com/")
            startActivity(openURL)

        }

      var twitterLink:ImageView = findViewById(R.id.link_twitter)
        twitterLink.setOnClickListener {
            //LogIn to FaceBook
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.twitter.com/")
            startActivity(openURL)

        }

        /*---------------------------------------------*/
        /*-------*/

        /* Start Implementation on Validation Functionality */
       viewModelValidation = ViewModelProvider(this).get(SignInValidationViewModel::class.java)
        val state = viewModelValidation.state
        val context = this.applicationContext
        initViewModel()//Intialize the SignIn Validation View Model

        //Code will execute Getting successs from the view after validation
        lifecycleScope.launch(){
            viewModelValidation.validationEvents.collect { event ->
                when (event) {
                    is SignInValidationViewModel.ValidationEvent.Success -> {
                        if(checkForInternet(applicationContext)){
                             getUserFROMAPI(viewModelValidation.state.email.toString(),viewModelValidation.state.password.toString())
                        } else{
                            //To do Implement functionality for getting userDetail from Database
                            Toast.makeText( context,"Check db, No Internet Connection", Toast.LENGTH_LONG ).show()
                        }
                    }
                }
            }
        }

        //Intializing the email and email error field in LogIn page
        var edEmail = findViewById<EditText>(R.id.edMail)
            edEmail.setText(viewModelValidation.state.email.toString())
        var tvEmailError = findViewById<TextView>(R.id.edEmailError)

         if (state.emailError != null) {
            edEmail.error = viewModelValidation.state.emailError.toString()
        }
        viewModelValidation.emailErrorLiveData.observe(this, Observer {
            if(it.toString()!=null && it.toString()!= ""){
                tvEmailError.setTextColor(getResources().getColor(R.color.error_msg))
                tvEmailError.text = it.toString()
                tvEmailError.visibility = VISIBLE
            } else{
                tvEmailError.setTextColor(getResources().getColor(R.color.white))
                tvEmailError.visibility = INVISIBLE

            }
        })

        //Intializing the password and password error Field
        var edPassword = findViewById<EditText>(R.id.edPassword)
        var EdPasswordError = findViewById<TextView>(R.id.EdPasswordError)
        if (state.passwordError != null&&state.passwordError != "") {
            edPassword.error = viewModelValidation.state.passwordError.toString()
        }
        viewModelValidation.passwordErrorLiveData.observe(this, Observer {
            if(it.toString()!= null && it.toString()!= ""){
                EdPasswordError.setTextColor(getResources().getColor(R.color.error_msg))
                EdPasswordError.text = it.toString()
                EdPasswordError.visibility = VISIBLE
            } else{
                EdPasswordError.setTextColor(getResources().getColor(R.color.white))
                EdPasswordError.visibility = INVISIBLE

            }
        })

        //Putting textChange functionality on Email field
        edEmail.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                    //edEmail.error = viewModelValidation.state.emailError.toString()
                viewModelValidation.onEvent(RegistrationFormEvent.EmailChanged(edEmail.text.toString()))

            }
        })

        //TextChange functionality in Password field
        edPassword.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                viewModelValidation.onEvent(RegistrationFormEvent.PasswordChanged(edPassword.text.toString()))
                EdPasswordError.setText(viewModelValidation.state.passwordError.toString())

            }
        })

        //Code will execute when the SignIn button is clicked
        var singIn = findViewById<Button>(R.id.singIn)
        singIn.setOnClickListener {
            viewModelValidation.onEvent(RegistrationFormEvent.Submit)

        }
    }

    //Method to initialize the UserLogIN view model
    fun initViewModel(){
        userLogInViewModel = ViewModelProvider(this).get(UserLogInViewModel::class.java)
        userLogInViewModel.getUserDetailsObserver().observe(this, Observer {
            if (it != null){
                Toast.makeText(this.applicationContext,
                    "Registration successful",
                    Toast.LENGTH_LONG
                ).show()
            } else{
                Toast.makeText(this.applicationContext,
                    "Registration unsuccessful",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

    }

    //Method to get userDetail from API
   private  fun getUserFROMAPI(email:String,password:String){
        userLogInViewModel.getUserDetails(email,password)
    }

    //Function check Internet connection
    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}