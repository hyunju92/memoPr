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
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                createUriFromMediaStore(context)    // 예시) content://media/external_primary/file/43502
            } else {
                getUriFromFile(context, createNewFile(context)) // 예시) content://hyunju.com.memo2020.provider/images/IMG6399012916669163261.jpg
            }
        }

        fun copyUri(context: Context, uri: Uri): Uri? {
            return try {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val newUri = createUriFromMediaStore(context)   // 예시) content://media/external_primary/file/43504
                    val outputStream = newUri?.let { context.contentResolver.openOutputStream(it) } ?: return null
                    val inputStream = context.contentResolver.openInputStream(uri)

                    IOUtils.copy(inputStream, outputStream)
                    inputStream?.close()
                    outputStream.close()

                    newUri

                } else {
                    val file = createNewFile(context)    // 예시) /data/user/0/hyunju.com.memo2020/files/IMG5709912983664673507.jpg
                    val outputStream = FileOutputStream(file)
                    val inputStream = context.contentResolver.openInputStream(uri)

                    IOUtils.copy(inputStream, outputStream)
                    inputStream?.close()
                    outputStream.close()

                    return getUriFromFile(context, file)    // 예시) content://hyunju.com.memo2020.provider/images/IMG5709912983664673507.jpg
                }

            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        /**
         * 29이상일 때,
         * MedidaStore에서 uri 생성
         * 예) content://media/external_primary/file/43502
         */
        @RequiresApi(Build.VERSION_CODES.Q)
        private fun createUriFromMediaStore(context: Context): Uri? {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
            }
            return context.contentResolver.insert(
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                values
            )
        }

        /**
         * 29미만일 때,
         * 내부저장소(일반파일)에 파일 생성
         * 예) /data/user/0/hyunju.com.memo2020/files/IMG5709912983664673507.jpg
         */
        private fun createNewFile(context: Context): File {
            // 내부저장소(일반파일) 경로
            val dir = File(context.filesDir.absolutePath)   // 예시) /data/user/0/hyunju.com.memo2020/files
            if (!dir.exists()) dir.mkdirs()

            return File.createTempFile("IMG", ".jpg", dir).apply {
                if (!exists()) createNewFile()
            }   // 예시) /data/user/0/hyunju.com.memo2020/files/IMG5709912983664673507.jpg
        }

        /**
         *  File의 Uri가져오기
         *  예) file값 = /data/user/0/hyunju.com.memo2020/files/IMG5709912983664673507.jpg
         *  예) return uri값 = content://hyunju.com.memo2020.provider/images/IMG5709912983664673507.jpg
         */
        private fun getUriFromFile(context: Context, file: File): Uri {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Uri.fromFile(file)
            } else {
                FileProvider.getUriForFile(context, context.getString(R.string.provider_authority), file)
            }
        }

    }
}