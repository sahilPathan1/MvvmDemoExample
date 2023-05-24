package com.example.mvvmdemoexample.profileModel

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmdemoexample.R
import com.example.mvvmdemoexample.const.ID_KEY
import com.example.mvvmdemoexample.databinding.ActivityProfileBinding
import com.example.mvvmdemoexample.profileModel.profileViewModel.ProfileViewModel
import com.example.mvvmdemoexample.sharepreference.SharePreference
import com.example.mvvmdemoexample.utils.Common
import com.example.mvvmdemoexample.utils.Status

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profilevModel: ProfileViewModel
    private lateinit var commonfun: Common
    private lateinit var sharePreference: SharePreference
    private lateinit var commonFun: Common
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        profilevModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        sharePreference = SharePreference(this@ProfileActivity)
        commonFun = Common(this)
        profilevModel = ProfileViewModel(application)
        binding.profileViewModal = profilevModel
        val id = sharePreference.getPrefInteger(ID_KEY, "")
        profilevModel.profileUser(id)


        profilevModel.liveData.observe(this@ProfileActivity) {

            when (it.status) {

                Status.SUCCESS -> {

                    if (it.data!!.status) {

                        Log.e("Data Register", "status Success${it.data.data.id}")
                        Log.d(
                            "data_information",
                            "http://13.233.39.120/trainee/uploads/${it.data.data.profile_picture}"
                        )
                        Glide.with(this).load(it.data.data.profile_picture)
                            .placeholder(R.drawable.logo).into(binding.profileImage)
                        binding.tvId.text = it.data.data.id
                        binding.tvEmail.text = it.data.data.email
                        binding.tvPassword.text = it.data.data.name


                    } else {
                        Log.e("Error", "status error${it.data.message}")
                    }
                }
                Status.LOADING -> {
                    Log.e("Data Register", "status Loading")
                }
                Status.ERROR -> {
                    Log.e("Data Register", "status Error")
                }
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.LogOut -> {

                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()

                commonFun.dialogWithTwoButton(
                    this@ProfileActivity,
                    getString(R.string.dataClear),
                    getString(R.string.areyouwanttologout),
                    1
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}