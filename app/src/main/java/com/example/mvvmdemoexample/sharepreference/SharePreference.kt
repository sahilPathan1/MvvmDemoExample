package com.example.mvvmdemoexample.sharepreference

import android.content.Context
import android.content.SharedPreferences
import com.example.mvvmdemoexample.R
import com.example.mvvmdemoexample.profileModel.ProfileActivity

class SharePreference(context: Context) {
    private var sharePref: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.loginPref), Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = sharePref.edit()

    fun setPrefString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun setPrefInt(key: String, value: Int?) {
        if (value != null) {
            editor.putInt(key, value)
        }
        editor.apply()
    }

    fun getPrefString(key: String?, value: String): String? {
        return sharePref.getString(key, "")
    }

    fun getPrefInteger(key: String?, value: String): Int? {
        return sharePref.getInt(key, 0)
    }

    fun setPrefBoolean(key: String?, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()

    }

    fun getPrefBoolean(key: String?): Boolean {
        return sharePref.getBoolean(key, false)
    }

}
