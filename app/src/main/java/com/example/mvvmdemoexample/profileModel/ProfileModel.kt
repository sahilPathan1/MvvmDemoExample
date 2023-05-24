package com.example.mvvmdemoexample.profileModel

data class ProfileModel(
    val status: Boolean,
    val message: String,
    val data: ProfileData
) {
    inner class ProfileData(
        val id: String?,
        val name: String,
        val email: String,
        val profile_picture:String?
    )
}
