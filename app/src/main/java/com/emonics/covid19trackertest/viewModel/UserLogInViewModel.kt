package com.emonics.covid19trackertest.viewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emonics.covid19trackertest.helpers.retrofit.Results
import com.emonics.covid19trackertest.helpers.retrofit.RetroInstance
import com.emonics.covid19trackertest.helpers.retrofit.RetroServiceInterFace
import com.emonics.covid19trackertest.helpers.retrofit.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.AccessController.getContext

class UserLogInViewModel:ViewModel() {
    var userData: MutableLiveData<List<User?>?> = MutableLiveData()
    //var userData: MutableLiveData<Results?> = MutableLiveData()

    fun getUserDetailsObserver(): MutableLiveData<List<User?>?> {
        return userData
    }

    fun getUserDetails(email:String,password:String){
        val email:String = "Nathan@yesenia.net"
        //val password:String = "1-463-123-4447"
        val password:String = password

        var retroService =  RetroInstance.getRetroInstance().create(RetroServiceInterFace::class.java)
        var call = retroService.getUserDetails()
        val userLoggedIn = mapOf("email" to email,
            "phone" to password)
        println(userLoggedIn)
        //(email=Nathan@yesenia.net, phone=1-463-123-4447)

        //Log.i("tag","Inside api response"+call)
        call.enqueue(object:Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if(response.isSuccessful){
                    userData?.value = response.body()

                    userData.value?.forEachIndexed{index,data->
                        Log.i("tag","=forEachIndexed=="+index+"--"+data?.email+"==="+data?.phone)
                        if((data?.email == email) && (data?.phone == password)){
                            Log.i("tag","success")

                        } else {
                            //Toast.makeText(this@UserLogInViewModel,"You are not a valid user, Please SignUp").show()
                            userData.postValue(null)

                        }
                    }
                } else {
                    userData.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })



        }




    }

