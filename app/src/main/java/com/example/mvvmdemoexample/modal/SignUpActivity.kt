package com.example.mvvmdemoexample.modal

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmdemoexample.R
import com.example.mvvmdemoexample.databinding.ActivitySignUpBinding
import com.example.mvvmdemoexample.login.LoginActivity
import com.example.mvvmdemoexample.modal.signup.SignUpViewModal
import com.example.mvvmdemoexample.utils.Common
import com.example.mvvmdemoexample.utils.PathUtil
import com.example.mvvmdemoexample.utils.Status

@Suppress("DEPRECATION")
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModal
    private lateinit var commonFun: Common
    private lateinit var progressDialog: ProgressDialog
    private var GALLARY_PERMISSION_CODE = 100
    private var imagePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this)[SignUpViewModal::class.java]
        commonFun = Common(this)
        progressDialog = ProgressDialog(this)
        viewModel = SignUpViewModal(application)
        binding.signUpViewModal = viewModel

        viewModel.liveData.observe(this@SignUpActivity) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    if (it.data!!.status) {
                        Log.e("Data Register", "status Success${it.data.message}")
                        commonFun.keyBordHide(this@SignUpActivity)
                        commonFun.dialog(
                            this@SignUpActivity,
                            it.data.status,
                            it.data.message,
                            1,
                            getString(R.string.registerDialog)
                        )
                    } else {
                        Log.e("Data Register", "status error${it.data.message}")
                        commonFun.dialog(
                            this@SignUpActivity,
                            it.data.status,
                            it.data.message,
                            0,
                            getString(R.string.registerDialog)
                        )
                    }

                }
                Status.LOADING -> {
                    progressDialog.setMessage(getString(R.string.signLoading))
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    Log.e("Data Register", "status Loading")
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    Log.e("Data Register", "status Error")
                }
            }
        }
        binding.TvLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {

            binding.TfFullName.error = ""
            binding.Tfemail.error = ""
            binding.TfPassword.error = ""

            if (viewModel.validation(imagePath, this@SignUpActivity)) {
                if (commonFun.isNetworkAvailable(this)) {
                    viewModel.registerUser(imagePath)
                } else {
                    Toast.makeText(this, getString(R.string.internet_error), Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                binding.TfFullName.error = viewModel.nameError
                binding.Tfemail.error = viewModel.emailError
                binding.TfPassword.error = viewModel.passwordError

            }
        }

        binding.btnImageLoad.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    GALLARY_PERMISSION_CODE
                )

            }
        }
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityIfNeeded(gallery, GALLARY_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == GALLARY_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.Permissiongallary, Toast.LENGTH_SHORT).show()
            }
            val showRationale = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

            Toast.makeText(this, R.string.CameraDenied, Toast.LENGTH_SHORT).show()

            if (!showRationale) {
                val deniedDialog = AlertDialog.Builder(this)
                deniedDialog.setTitle(R.string.deniedpermission)
                deniedDialog.setMessage(R.string.settinh)
                deniedDialog.setPositiveButton(
                    R.string.settinh,
                    DialogInterface.OnClickListener { _, _ ->
                        permissionDeniedPopup()
                    })
                deniedDialog.setNegativeButton(R.string.cancel, null)
                deniedDialog.show()
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun permissionDeniedPopup() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package", packageName, null
        )
        intent.data = uri
        startActivity(intent)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLARY_PERMISSION_CODE) {
            if (data != null) {
                val uri = data.data
                Log.d("data_information", uri.toString())
                Log.d("data_information", PathUtil.getPath(this, uri!!).toString())
                binding.getImage.setImageURI(uri)
                imagePath = PathUtil.getPath(this, uri).toString()
                Glide.with(binding.btnImageLoad)
                    .load(uri)
                    .placeholder(R.drawable.image_not_found)
                    .into(binding.getImage)
            } else {
                Toast.makeText(this, getString(R.string.open), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, getString(R.string.notMatchCode), Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}




