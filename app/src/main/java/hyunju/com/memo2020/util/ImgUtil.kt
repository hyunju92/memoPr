package hyunju.com.memo2020.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import hyunju.com.memo2020.R
import org.apache.commons.io.IOUtils
import java.io.*

class ImgUtil {
    companion object {
        fun createNewUri(context: Context): Uri? {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                getUriFromMediaStore(context)
            } else {
                getUriFromFile(context, createNewFile(context))
            }
        }

        fun copyUri(context: Context, originalUri: Uri): Uri? {
            return copyUriToFile(context, originalUri)
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        private fun getUriFromMediaStore(context: Context): Uri? {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
            }

            return context.contentResolver.insert(
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                values
            )
        }

        private fun getUriFromFile(context: Context, file: File): Uri {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Uri.fromFile(file)
            } else {
                FileProvider.getUriForFile(context, context.getString(R.string.provider_authority), file)
            }
        }

        // TODO 수정 필요
        // 갤러리 이미지 복사
        // 10 이상 : 외부 저장소 -> 외부 저장소 샌드박스로 복사
        // 10 미만 : 외부 저장소 -> 내부 저장소로 복사
        private fun copyUriToFile(context: Context, uri: Uri) : Uri? {
            return try {

                return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    val newUri = getUriFromMediaStore(context)
                    val outputStream = context.contentResolver.openOutputStream(newUri!!) ?: return null
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

                    IOUtils.copy(inputStream, outputStream)
                    inputStream?.close()
                    outputStream.close()

                    newUri

                } else {
                    val file = createNewFile(context)
                    val outputStream: OutputStream = FileOutputStream(file)
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

                    IOUtils.copy(inputStream, outputStream)
                    inputStream?.close()
                    outputStream.close()

                    getUriFromFile(context, file)
                }

            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }


        // create new empty file path
        private fun createNewFile(context: Context): File {
            val dir = File(context.filesDir.absolutePath)
            if (!dir.exists()) dir.mkdirs()

            return File.createTempFile("IMG", ".jpg", dir).apply {
                if (!exists()) createNewFile()
            }
        }



    }
}