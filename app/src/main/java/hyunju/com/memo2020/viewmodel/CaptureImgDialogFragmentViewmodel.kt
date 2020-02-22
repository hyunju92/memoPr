package hyunju.com.memo2020.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import java.io.File
import java.io.FileOutputStream

class CaptureImgDialogFragmentViewmodel(application: Application) : AndroidViewModel(application) {

    // save capture view as file path uri
    fun saveCapture(context: Context, captureView: View): String {
        captureView.buildDrawingCache()
        val cacheBitmap = captureView.drawingCache

        val filePath = createFilePath(context)
        val photoUri = FileProvider.getUriForFile(context, "hyunju.com.memo2020.provider", filePath)
        Log.d("testPickAlbum", "onactivity saveCapture = photoUri " + photoUri)

        val fos = FileOutputStream(filePath)
        cacheBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)

        return photoUri.toString()
    }

    fun createFilePath(context: Context): File {
        val dirPath = context.getExternalFilesDir(null)?.absolutePath
        val dir = File(dirPath).apply { if (!this.exists()) this.mkdir() }

        val filePath = File.createTempFile("IMG", ".jpg", dir).apply {
            if (!this.exists()) this.createNewFile()
        }

        return filePath
    }


}


