package com.example.mvvmdemoexample.modal.signup

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdemoexample.R
import com.example.mvvmdemoexample.const.emailValide
import com.example.mvvmdemoexample.modal.RegisterModal
import com.example.mvvmdemoexample.utils.Resource
import com.example.mvvmdemoexample.utils.ResponseCodeCheck
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SignUpViewModal(application: Application) : AndroidViewModel(application) {

    var name: String = ""
    var email: String = ""
    var password: String = ""

    var nameError: String = ""
    var emailError: String = ""
    var passwordError: String = ""

    var responseCodeCheck: ResponseCodeCheck = ResponseCodeCheck()
    private var mutabableLiveData: MutableLiveData<Resource<RegisterModal>> = MutableLiveData()
    var liveData: LiveData<Resource<RegisterModal>> = mutabableLiveData


    private fun getSomeString(result: Int): String {
        return getApplication<Application>().resources.getString(result)
    }

    fun validation(imagePath: String, context: Context): Boolean {
        nameError = ""
        emailError = ""
        passwordError = ""

        when {
            name.isEmpty() -> {

                nameError = getSomeString(R.string.nameError)
                return false
            }
            email.isEmpty() -> {
                emailError = getSomeString(R.string.emailEroor)
                return false
            }
            !email.matches(emailValide.toRegex()) -> {
                emailError = getSomeString((R.string.invalidmail))
                return false
            }
            password.isEmpty() -> {
                passwordError = getSomeString(R.string.passEmpty)
                return false
            }
            password.length < 6 -> {
                passwordError = getSomeString(R.string.passError)
                return false
            }
            imagePath.isEmpty() -> {
                makeText(context, "please select Image", LENGTH_LONG).show()
                return false
            }
            else -> {
                return true
            }
        }
    }

    fun registerUser(imagePath: String) {

        mutabableLiveData.value = Resource.loading(null)

        val file = File(imagePath)
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        Log.e("BODY", "registerUser: $file")
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_picture", file.name, requestFile)
        val name: RequestBody = name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val email: RequestBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val password: RequestBody =
            password.toRequestBody("multipart/form-data".toMediaTypeOrNull())


        val hashMap: HashMap<String, RequestBody> = HashMap()
        hashMap.apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }

        Log.e("BODY", "$body")
        Log.e("BODY", "$name")
        Log.e("BODY", "$email")
        Log.e("BODY", "$password")
        Log.e("BODY", "$hashMap")

        mutabableLiveData.postValue(Resource.loading(null))

        RetrofitInstance.apiInterface.registerUser(body, hashMap)
            .enqueue(object : Callback<RegisterModal?> {
                override fun onResponse(
                    call: Call<RegisterModal?>, response: Response<RegisterModal?>
                ) {

                    val result = response.body()!!
                    Log.e("Result", result.message)

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

                override fun onFailure(call: Call<RegisterModal?>, t: Throwable) {
                    mutabableLiveData.postValue(
                        Resource.error(
                            t.message.toString(), null
                        )
                    )
                    makeText(getApplication(), t.message, LENGTH_SHORT).show()

                }
            })
    }
}