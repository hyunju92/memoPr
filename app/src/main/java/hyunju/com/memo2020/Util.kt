package hyunju.com.memo2020

import android.content.Context
import android.content.SharedPreferences


class Util {
    companion object {
        private const val FILE_NAME = "memePref"

        fun savePref(context: Context, key: String, value: String) {
            val prefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, 0)
            prefs.edit().putString(key, value).apply()
        }

        fun getPref(context: Context, key: String): String {
            val prefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, 0)
            return prefs.getString(key, "") ?: ""
        }

        @JvmStatic
        fun getScreenWidth(context: Context) : Int{
            val pxWidth = context.getResources().getDisplayMetrics().widthPixels
            return pxWidth
        }

//        fun matchHeightToDeviceWidth(view: View, boolean: Boolean) {
//            val pxWidth = view.getResources().getDisplayMetrics().widthPixels
//            val layoutParams: ViewGroup.LayoutParams = view.layoutParams
//            layoutParams.height = pxWidth
//            Log.d("testBinding", "heigt " + pxWidth)
//            view.layoutParams = layoutParams
//        }


    }
}