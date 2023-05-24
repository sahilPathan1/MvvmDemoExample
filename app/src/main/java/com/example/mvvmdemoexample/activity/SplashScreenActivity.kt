package com.example.mvvmdemoexample.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.example.mvvmdemoexample.R
import com.example.mvvmdemoexample.const.USER_LOGIN
import com.example.mvvmdemoexample.login.LoginActivity
import com.example.mvvmdemoexample.modal.SignUpActivity
import com.example.mvvmdemoexample.profileModel.ProfileActivity
import com.example.mvvmdemoexample.sharepreference.SharePreference

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var sharePreference: SharePreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharePreference = SharePreference(this)
        setContentView(R.layout.activity_splash_screen)
        fullScreen()
        timeForScreen()
    }
    private fun timeForScreen() {
        Handler(Looper.getMainLooper()).postDelayed({

            val hasLoggedIn = sharePreference.getPrefBoolean(USER_LOGIN)
            if (!hasLoggedIn) {
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@SplashScreenActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 1000)
    }

    private fun fullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.statusBarColor = Color.WHITE
        supportActionBar?.hide()
    }
}