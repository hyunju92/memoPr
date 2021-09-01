package hyunju.com.memo2020.model

import android.content.Context
import android.content.SharedPreferences
import hyunju.com.memo2020.R

class PrefRepository(private val context: Context)  {
    private var pref : SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.pref_file_name), 0)

    fun setPref(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    fun getPref(key: String): String {
        return pref.getString(key, "") ?: ""
    }

    fun getStringFromResId(resId : Int) : String {
        return context.getString(resId)
    }


}

