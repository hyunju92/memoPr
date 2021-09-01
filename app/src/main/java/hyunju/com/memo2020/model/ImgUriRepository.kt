package hyunju.com.memo2020.model

import android.content.Context
import android.net.Uri
import hyunju.com.memo2020.util.ImgUtil
import javax.inject.Inject

class ImgUriRepository @Inject constructor(private val context: Context) {

    fun createNewUri(): Uri? {
        return ImgUtil.createNewUri(context)
    }

    fun createCopiedUri(uri: Uri): Uri? {
        return ImgUtil.createCopiedUri(context, uri)
    }

    fun deleteUri(uri: String) {
        context.contentResolver?.delete(Uri.parse(uri), null, null);
    }
}