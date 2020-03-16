package hyunju.com.memo2020.util

import android.content.Context
import android.content.SharedPreferences
import hyunju.com.memo2020.R


class Util(var context: Context) {
    companion object {

//        private const val FILE_NAME = "memePref"
        fun setPref(context: Context, key: String, value: String) {
            val prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file_name), 0)
            prefs.edit().putString(key, value).apply()
        }

        fun getPref(context: Context, key: String): String {
            val prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file_name), 0)
            return prefs.getString(key, "") ?: ""
        }



    }
}