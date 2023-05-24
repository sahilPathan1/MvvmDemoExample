package com.example.mvvmdemoexample.login.viewmodal

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdemoexample.R
import com.example.mvvmdemoexample.const.*
import com.example.mvvmdemoexample.login.LoginModal
import com.example.mvvmdemoexample.sharepreference.SharePreference
import com.example.mvvmdemoexample.utils.Resource
import com.example.mvvmdemoexample.utils.ResponseCodeCheck
import retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModal(application: Application) : AndroidViewModel(application) {
    var email: String = ""
    var password: String = ""


    var emailError: String = ""
    var passwordError: String = ""

    var responseCodeCheck: ResponseCodeCheck = ResponseCodeCheck()
    private lateinit var sharePreference: SharePreference
    private var mutabableLiveData: MutableLiveData<Resource<LoginModal>> = MutableLiveData()
    var liveData: LiveData<Resource<LoginModal>> = mutabableLiveData

    fun getSomeStrig(result: Int): String {
        return getApplication<Application>().resources.getString(result)
    }

    fun validation(): Boolean {

        emailError = ""
        passwordError = ""

        when {

            email.isEmpty() -> {
                emailError = getSomeStrig(R.string.emailEroor)
                return false
            }
            !email.matches(emailValide.toRegex()) -> {
                emailError = getSomeStrig((R.string.invalidmail))
                return false
            }
            password.isEmpty() -> {
                passwordError = getSomeStrig(R.string.passEmpty)
                return false
            }
            password.length < 6 -> {
                passwordError = getSomeStrig(R.string.passError)
                return false
            }
            else -> {
                return true
            }
        }
    }

    fun loginUser() {

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.apply {
            put(EMAIL, email)
            put(PASSWORD, password)

        }
        mutabableLiveData.postValue(Resource.loading(null))

        RetrofitInstance.apiInterface.loginUser(hashMap)
            .enqueue(object : Callback<LoginModal> {
                override fun onResponse(
                    call: Call<LoginModal?>, response: Response<LoginModal?>
                ) {

                    val result = response.body()!!
                    Log.e("Result", "${result.message}")

                    try {
                        mutabableLiveData.postValue(
                            responseCodeCheck.getResponseResult(
                                Response.success(
                                    response.body()
                                )
                            )
                        )
                    } catch (e: Exception) {
                        mutabableLiveData.postValue(
                            Resource.error(
                                e.message.toString(), response.body()
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<LoginModal?>, t: Throwable) {
                    mutabableLiveData.postValue(
                        Resource.error(
                            t.message.toString(), null
                        )
                    )
                    Toast.makeText(getApplication(), t.message, Toast.LENGTH_SHORT).show()

                }

            })
    }

}
