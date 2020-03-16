package hyunju.com.memo2020.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import androidx.core.content.FileProvider
import hyunju.com.memo2020.R
import org.apache.commons.io.IOUtils
import java.io.*

class ImgUtil {
    companion object {


        fun getProviderUri(context: Context, filePath: File?): Uri {
            return FileProvider.getUriForFile(context, context.getString(R.string.provider_authority),
                    filePath ?: createNewFilePath(context))
        }

        // save capture view as file path uri
        fun getCapturedImgUri(context: Context, capturedView: View): String {
            val cacheBitmap = getBitmapFromView(capturedView)

            val filePath = createNewFilePath(context)
            val providerImgUri = getProviderUri(context, filePath)

            val fos = FileOutputStream(filePath)
            cacheBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)

            return providerImgUri.toString()
        }

        fun copyUri(context: Context, originalUri: Uri): Uri? {
            val copiedFilePath = createNewFilePath(context)
            val copiedUri = FileProvider.getUriForFile(context, context.getString(R.string.provider_authority), copiedFilePath)

            copyUri(context, originalUri, copiedFilePath)

            return copiedUri
        }

        private fun copyUri(context: Context, originalUri: Uri, copiedFilePath: File) {
            try {
                val inputStream: InputStream = context.contentResolver.openInputStream(originalUri)
                        ?: return
                val outputStream: OutputStream = FileOutputStream(copiedFilePath)

                // * libarary - Apache Commons IO
                IOUtils.copy(inputStream, outputStream)
                inputStream.close()
                outputStream.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


        // create new empty file path
        private fun createNewFilePath(context: Context): File {
            val dirPath = context.getExternalFilesDir(null)?.absolutePath
            val dir = File(dirPath).apply { if (!this.exists()) this.mkdir() }

            return File.createTempFile("IMG", ".jpg", dir).apply {
                if (!exists()) createNewFile()
            }
        }


        private fun getBitmapFromView(view: View): Bitmap? {
            val bitmap =
                    Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }


    }
}