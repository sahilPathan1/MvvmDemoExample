package com.example.mvvmdemoexample.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.mvvmdemoexample.R
import com.example.mvvmdemoexample.activity.MainActivity
import com.example.mvvmdemoexample.const.USER_LOGIN
import com.example.mvvmdemoexample.login.LoginActivity
import com.example.mvvmdemoexample.profileModel.ProfileActivity
import com.example.mvvmdemoexample.sharepreference.SharePreference
import com.google.android.material.dialog.*


@Suppress("DEPRECATION")
class Common(private val activity: Activity) {
    private lateinit var sharePreference: SharePreference

    fun dialog(
        context: Context, tittle: Boolean, message: String, check: Int, dialogTittle: String
    ) {
        sharePreference = SharePreference(activity)
        val materialDialogs = MaterialAlertDialogBuilder(context)

        materialDialogs.setTitle(dialogTittle)
        materialDialogs.setMessage(message)

        materialDialogs.setPositiveButton(R.string.okk) { _, _ ->
            if (tittle) {
                when (check) {
                    1 -> {
                        activity.startActivity(Intent(context, LoginActivity::class.java))
                        activity.finishAffinity()
                    }
                    2 -> {
                        sharePreference.setPrefBoolean(USER_LOGIN, true)
                        activity.startActivity(Intent(context, ProfileActivity::class.java))
                        activity.finishAffinity()
                    }
                    else -> {
                        activity.startActivity(Intent(context, MainActivity::class.java))
                        activity.finishAffinity()
                    }
                }
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
        materialDialogs.setCancelable(false)
        materialDialogs.show()
    }

    fun dialogWithTwoButton(context: Context, dialogTittle: String, message: String, check: Int) {
        sharePreference= SharePreference(context)
        val materialDialogs = MaterialAlertDialogBuilder(context)

        materialDialogs.setTitle(dialogTittle)
        materialDialogs.setMessage(message)

        materialDialogs.setPositiveButton(R.string.okk) { _, _ ->

            if (check == 1) {
                sharePreference.setPrefBoolean(USER_LOGIN, false)
                activity.startActivity(Intent(context, LoginActivity::class.java))
                activity.finish()
            }
        }
        materialDialogs.setNegativeButton(context.getString(R.string.dismiss)) { closeDialog, _ ->
            closeDialog.dismiss()
        }
        materialDialogs.setCancelable(false)
        materialDialogs.show()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun keyBordHide(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(activity.currentFocus!!.getWindowToken(), 0)
    }
}