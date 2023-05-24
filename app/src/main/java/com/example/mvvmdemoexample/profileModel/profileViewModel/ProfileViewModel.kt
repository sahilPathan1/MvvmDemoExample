package com.example.mvvmdemoexample.profileModel.profileViewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mvvmdemoexample.const.ID_KEY
import com.example.mvvmdemoexample.profileModel.ProfileModel
import com.example.mvvmdemoexample.sharepreference.SharePreference
import com.example.mvvmdemoexample.utils.Resource
import com.example.mvvmdemoexample.utils.ResponseCodeCheck
import retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    var responseCodeCheck: ResponseCodeCheck = ResponseCodeCheck()
    private var mutabableLiveData: MutableLiveData<Resource<ProfileModel>> = MutableLiveData()
    var liveData: MutableLiveData<Resource<ProfileModel>> = mutabableLiveData
    private lateinit var sharePreference: SharePreference


    fun profileUser(id: Int?) {

        mutabableLiveData.postValue(Resource.loading(null))

        if (id != null) {
            RetrofitInstance.apiInterface.profileUser(id)
                .enqueue(object : Callback<ProfileModel?> {
                    override fun onResponse(
                        call: Call<ProfileModel?>, response: Response<ProfileModel?>
                    ) {

                        val result = response.body()!!
                        Log.e("Result", result.data.id!!)

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

                    override fun onFailure(call: Call<ProfileModel?>, t: Throwable) {
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
}
