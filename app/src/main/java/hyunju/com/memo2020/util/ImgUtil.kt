package hyunju.com.memo2020.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.core.content.FileProvider
import org.apache.commons.io.IOUtils
import java.io.*

class ImgUtil {
    companion object {
        fun getProviderUri(context: Context, filePath: File?): Uri {
            val providerImgUri = FileProvider.getUriForFile(context, "hyunju.com.memo2020.provider",
                    filePath ?: createNewFilePath(context))
            return providerImgUri
        }

        // create new empty file path
        fun createNewFilePath(context: Context): File {
            val dirPath = context.getExternalFilesDir(null)?.absolutePath
            val dir = File(dirPath).apply { if (!this.exists()) this.mkdir() }

            val filePath = File.createTempFile("IMG", ".jpg", dir).apply {
                if (!this.exists()) this.createNewFile()
            }

            return filePath
        }


        // save capture view as file path uri
        fun getCapturedImgUri(context: Context, capturedView: View): String {
            capturedView.buildDrawingCache()
            val cacheBitmap = capturedView.drawingCache

            val filePath = createNewFilePath(context)
            val providerImgUri = getProviderUri(context, filePath)

            val fos = FileOutputStream(filePath)
            cacheBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)

            return providerImgUri.toString()
        }


        fun copyUriIntoProvider(context: Context, originalUri: Uri): Uri? {
            val fileName = getFileNameOfUri(originalUri)

            if (!TextUtils.isEmpty(fileName)) {
                val copiedFilePath = createNewFilePath(context)
                val copiedUri = FileProvider.getUriForFile(context, "hyunju.com.memo2020.provider", copiedFilePath)

                copyUriIntoProviderProcess(context, originalUri, copiedFilePath)

                return copiedUri
            }
            return null
        }

        fun getFileNameOfUri(uri: Uri?): String? {
            if (uri == null) return null
            var fileName: String? = null
            val path = uri.path
            val cut = path!!.lastIndexOf('/')
            if (cut != -1) {
                fileName = path.substring(cut + 1)
            }
            return fileName
        }

        fun copyUriIntoProviderProcess(context: Context, imgUri: Uri, copiedFilePath: File) {
            try {
                val inputStream: InputStream = context.getContentResolver().openInputStream(imgUri)
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


    }
}