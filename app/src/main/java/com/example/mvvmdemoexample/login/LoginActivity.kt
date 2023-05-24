@file:Suppress("DEPRECATION")

package com.example.mvvmdemoexample.login
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmdemoexample.R
import com.example.mvvmdemoexample.const.ID_KEY
import com.example.mvvmdemoexample.databinding.ActivityLoginBinding
import com.example.mvvmdemoexample.login.viewmodal.LoginViewModal
import com.example.mvvmdemoexample.modal.SignUpActivity
import com.example.mvvmdemoexample.sharepreference.SharePreference
import com.example.mvvmdemoexample.utils.Common
import com.example.mvvmdemoexample.utils.Status

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginVmodal: LoginViewModal
    private lateinit var sharePreference: SharePreference
    private lateinit var commonFun: Common
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginVmodal = ViewModelProvider(this)[LoginViewModal::class.java]
        commonFun = Common(this)
        progressDialog= ProgressDialog(this)
        sharePreference= SharePreference(this@LoginActivity)
        binding.loginViewModal = loginVmodal

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }
        binding.btnLogin.setOnClickListener { onLoginClick() }
    }

    private fun onLoginClick() {

        binding.TfLoginEmail.error = ""
        binding.TfPassword.error = ""

        if (loginVmodal.validation()) {

            if (commonFun.isNetworkAvailable(this)) {
                loginVmodal.loginUser()
            } else {
                Toast.makeText(this, getString(R.string.internet_error), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {

            binding.TfLoginEmail.error = loginVmodal.emailError
            binding.TfPassword.error = loginVmodal.passwordError

        }


        loginVmodal.liveData.observe(this@LoginActivity) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    if (it.data!!.status) {
                        Log.e("Data Register", "status Success${it.data.message}")
                        commonFun.keyBordHide(this@LoginActivity)
                        val shareId = it.data.data.id
                        sharePreference.setPrefInt(ID_KEY,shareId)
                        commonFun.dialog(
                            this@LoginActivity,
                            it.data.status,
                            it.data.message,
                            2,
                            getString(R.string.loginDialog)
                        )

                    } else {
                        Log.e("Data Register", "status error${it.data.message}")
                        commonFun.dialog(
                            this@LoginActivity,
                            it.data.status,
                            it.data.message,
                            0,
                            getString(R.string.loginDialog)
                        )
                    }


                }
                Status.LOADING -> {
                    progressDialog.setMessage(getString(R.string.DialogLoading))
                    progressDialog.setCancelable(false)
                    Log.e("Data Register", "status Loading")
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    Log.e("Data Register", "status Error")
                    commonFun.dialog(this, it.data!!.status, it.data.message, 1, it.data.message)
                }
            }
        }
    }



}