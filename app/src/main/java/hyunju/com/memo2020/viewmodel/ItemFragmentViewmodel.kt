package hyunju.com.memo2020.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import hyunju.com.memo2020.R
import hyunju.com.memo2020.Util
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.view.ItemFragmentDirections
import org.apache.commons.io.IOUtils
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class ItemFragmentViewmodel(application: Application) : AndroidViewModel(application) {


    val dao = MemoDatabase.get(application).memoDao()

    val currentMode: MutableLiveData<Int> = MutableLiveData()
    val memoItem: MutableLiveData<Memo?> = MutableLiveData()
    val imgList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    var isNeedToSaveIfFinished: Boolean = true


    // * 2 mode (detail/edit)
    val DETAIL_MODE = 1 // just view to show detail memo
    val EDIT_MODE = 2   // edit contents view

    fun isEditMode(): Boolean {
        return this.currentMode.value == EDIT_MODE
    }


    // * set memo item (received as argument)
    fun setMemoItem(memo: Memo?) {
        memoItem.value = memo
        imgList.value = memo?.getImageList() ?: ArrayList()

    }


    // * core action (need db)
    fun save(context: Context, title: String, contents: String) {
        if (memoItem.value != null) {
            memoItem.value!!.let {
                it.title = title
                it.contents = contents
                it.date = Date()
                it.setImgStr(imgList.value!!)
                Log.d("testSave", "size " + it.getImageList().size.toString())

                update(it)
                Toast.makeText(context, "수정되었습니다.", Toast.LENGTH_SHORT).show()
            }

        } else {
            val newMemo = Memo(title = title, contents = contents, images = "", date = Date())
            newMemo.setImgStr(imgList.value!!)
            insert(newMemo)
            Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    fun delete(context: Context) {
        if (memoItem.value != null) {
            delete(memoItem.value!!.id)
            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    // * access db
    private fun update(memo: Memo?) {
        doAsync {
            dao.update(memo)
        }.execute()
    }

    private fun insert(memo: Memo) {
        doAsync {
            dao.insert(memo)
        }.execute()
    }

    private fun delete(id: Long) {
        doAsync {
            dao.deleteById(id)
        }.execute()
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Log.d("testsObserver", "onPostExecute ")

        }
    }


    // nav
    fun moveCaptureImgDialogFrag(view: View, uriStr: String?) {
        if (TextUtils.isEmpty(uriStr)) return

        Log.d("moveItemFragment", "moveCaptureImgDialogFrag size = " + imgList.value?.size)

//        val testUrl = "https://postfiles.pstatic.net/MjAxNzA5MjVfMjcg/MDAxNTA2MzA0Mzc1MTM4.prbIrhy_KnEJAq0I4WGX0yHDuCRhQsbKcHU6RGDVVNog.5o4kxdfkv-bGtI4-gXRwXjh045Yz77L8WnFFOt6PCQIg.JPEG.rla2945/13402575_643830889104534_68262404_n.jpg?type=w580"
//        val testUrl = "http://blogfiles.naver.net/MjAxODEyMjBfMTI3/MDAxNTQ1Mjg4ODgzMTc3.o6BqoojNTIKMzP2SPe3Idpx_mo6bE1XaRh1OF7QGk-Ig.9eL4YyOfa1v_6aejx5gK1Bpoe78UmUGvz5AUQ2jr7YAg.JPEG.petgeek/20181220_110730.jpg"
//        val testUrl = "https://blogfiles.pstatic.net/MjAxOTA5MDRfNiAg/MDAxNTY3NTc5NTQ2ODI0.J_Hb7GFGIp5X1aosH-Mo9p73gMul6MznKEL-yrjpw1Mg.mgRAWlZU5_3XRcWudMrCJQezo_c2KcvU2rjZ6slyT3Mg.JPEG.cgdong10/%ED%81%AC%EA%B8%B0%EB%B3%80%ED%99%98_%ED%81%AC%EB%9F%AC%EC%89%AC-%EB%82%98%EB%B9%A0.jpg?type=w1"
//        val testUrl = "http://imgnews.naver.net/image/215/2019/12/03/A201912030013_1_20191203071403938.jpg"
//        val testUrl = "https://search.pstatic.net/common/?src=http%3A%2F%2Fmusicmeta.phinf.naver.net%2Fphoto%2F000%2F079%2F79132.jpg&type=b400"
//        val testUrl = "http://blogfiles.naver.net/20140605_23/qwerz00_14018944939128amlJ_JPEG/Teaser_Crush%28_%AC%EB%9F%AC_____%27Crush_On_You%27_Preview_%281080p%29.mp4_20140605_000624.135.jpg"
//        val testUrl = "http://image.nmv.naver.net/blog_2019_09_20_2925/ba9b42c1-db4e-11e9-bc16-0000000049a8_02.jpg"

        val testUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTQlHniwP20JZYMssUzLYRpPfGTndcxsPNkwRK9S3aKADifMEel"

        val action = ItemFragmentDirections.actionItemFragmentToCaptureImgDialogFragment()
        Navigation.findNavController(view).navigate(action)
    }

    // nav
    fun moveItemFragFromCaptureFrag(view: View, uriStr: String?) {
        if (TextUtils.isEmpty(uriStr)) return
//        val action = CaptureImgDialogFragmentDirections.actionCaptureImgDialogFragmentToItemFragment(null, uriStr!!)
//        Navigation.findNavController(view).navigate(action)

        Log.d("moveItemFragment", "size = " + imgList.value?.size)
        imgList.value!!.add(uriStr!!)

        Log.d("moveItemFragment", "size = " + imgList.value?.size)
        Navigation.findNavController(view).popBackStack()
    }


    // edit imgList
    fun addImgList(uriStr: String?) {
        if (TextUtils.isEmpty(uriStr)) return

        val tempList: ArrayList<String> = imgList.value!!
        tempList.add(uriStr!!)
        imgList.value = tempList
    }


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

    private fun createFilePath(context: Context): File {
        val dirPath = context.getExternalFilesDir(null)?.absolutePath
        val dir = File(dirPath).apply { if (!this.exists()) this.mkdir() }

        val filePath = File.createTempFile("IMG", ".jpg", dir).apply {
            if (!this.exists()) this.createNewFile()
        }

        return filePath
    }


    fun getFilePathFromContentUri(context: Context, contentUri: Uri): Uri? {
        val fileName = getFileName(contentUri)

        if (!TextUtils.isEmpty(fileName)) {
            val filePath = createFilePath(context)
            val photoUri = FileProvider.getUriForFile(context, "hyunju.com.memo2020.provider", filePath)

            copy(context, contentUri, filePath)
            Log.d("testPickAlbum", "saveFile = copyFile.absolutePath " + filePath.absolutePath)
            Log.d("testPickAlbum", "saveFile = copyFile. photoUri " + photoUri)

            return photoUri
        }
        return null
    }

    fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    fun copy(context: Context, srcUri: Uri, dstFile: File) {
        try {
            val inputStream: InputStream = context.getContentResolver().openInputStream(srcUri)
                    ?: return
            val outputStream: OutputStream = FileOutputStream(dstFile)
            IOUtils.copy(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    val REQ_PICK_FROM_ALBUM = 1000
    val REQ_PICK_FROM_CAMERA = 1001
    val REQ_PICK_FROM_URL = 1002

    fun getImgByReqcode(activity: Activity, context: Context, request: Int) {
        var reqCode = 0

        Intent().apply {
            when (request) {
                R.id.album_btn_edit_img -> {
                    reqCode = REQ_PICK_FROM_ALBUM
                    this.action = Intent.ACTION_PICK
                    this.type = MediaStore.Images.Media.CONTENT_TYPE

                }
                R.id.camera_btn_edit_img -> {
                    reqCode = REQ_PICK_FROM_CAMERA
                    // 1
                    val dirPath = context.getExternalFilesDir(null)?.absolutePath
                    val dir = File(dirPath).apply { if (!this.exists()) this.mkdir() }

                    val filePath = File.createTempFile("IMG", ".jpg", dir).apply {
                        if (!this.exists()) this.createNewFile()
                    }

                    val photoUri = FileProvider.getUriForFile(context, "hyunju.com.memo2020.provider", filePath)
                    Util.setPref(context, "uriFromCamera", photoUri.toString())


                    this.action = MediaStore.ACTION_IMAGE_CAPTURE
                    this.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)


                }
                else -> {
                }
            }

        }.let {
            activity.startActivityForResult(it, reqCode)
        }

    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            when (requestCode) {
                REQ_PICK_FROM_ALBUM -> {
                    getFilePathFromContentUri(getApplication(), data!!.data!!)?.let {
                        addImgList(it.toString())
                    }
                }
                REQ_PICK_FROM_CAMERA -> {
                    Util.getPref(getApplication(), "uriFromCamera").let {
                        if (it.isNotEmpty()) addImgList(it)
                    }
                }

            }
        }
    }


}


