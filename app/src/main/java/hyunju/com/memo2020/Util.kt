package hyunju.com.memo2020

import android.content.Context
import android.content.SharedPreferences
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.util.*


class Util {
    companion object {

        // shared prefrence set/get
        private const val FILE_NAME = "memePref"

        fun setPref(context: Context, key: String, value: String) {
            val prefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, 0)
            prefs.edit().putString(key, value).apply()
        }

        fun getPref(context: Context, key: String): String {
            val prefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, 0)
            return prefs.getString(key, "") ?: ""
        }



    }
}