package com.example.mvvmdemoexample.activity.retrofit

import com.example.mvvmdemoexample.login.LoginModal
import com.example.mvvmdemoexample.modal.RegisterModal
import com.example.mvvmdemoexample.profileModel.ProfileModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface Service {
    // request body will use form URL encoding. Fields should be declared as parameters and annotated with @Field.
    @Multipart
    @POST("register.php")
    fun registerUser(
        @Part profile_picture:MultipartBody.Part,
        @PartMap hashMap: HashMap<String,RequestBody>
    ): Call<RegisterModal>

   @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @FieldMap hashMap: HashMap<String, String>
    ): Call<LoginModal>

    @FormUrlEncoded
    @POST("profile.php")
    fun profileUser(
        @Field("user_id") user_id: Int
    ): Call<ProfileModel>
}
