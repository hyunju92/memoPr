package hyunju.com.memo2020

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream


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


        fun getImageUriFromUri(inContext: Context, inImage: Bitmap): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
            return Uri.parse(path)
        }

    }
}