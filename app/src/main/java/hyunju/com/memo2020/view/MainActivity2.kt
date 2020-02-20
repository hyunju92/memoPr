package hyunju.com.memo2020.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import hyunju.com.memo2020.R
import hyunju.com.memo2020.Util
import hyunju.com.memo2020.databinding.ActivityMain2Binding
import hyunju.com.memo2020.db.MemoAdapter
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.viewmodel.MainAcitivityViewmodel
import org.apache.commons.io.IOUtils
import java.io.*
import java.util.*


class MainActivity2 : AppCompatActivity(), MemoAdapter.OnItemClickListener {

    protected lateinit var binding: ActivityMain2Binding
    protected val mainViewModel: MainAcitivityViewmodel by lazy {
        ViewModelProvider(this).get(MainAcitivityViewmodel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)
        binding.rv.setLayoutManager(LinearLayoutManager(this))
        binding.rv.setHasFixedSize(true)

        val adapter = MemoAdapter()
        adapter.setOnItemClickListener(this)
        binding.rv.adapter = adapter

        mainViewModel.allMemos.observe(this,
                androidx.lifecycle.Observer {
                    Log.d("testsObserver", "in observe " + it.size)
                    adapter.submitList(it)
                    adapter.notifyDataSetChanged()
                }
        )

//        val testImgUrl = "content://hyunju.com.memo2020.provider/images/Android/data/hyunju.com.memo2020/files/IMG8784800242698310023.jpg"
//
//        Glide.with(this)
//                .load(testImgUrl)
//                .into(binding.testIv)


        binding.btn.setOnClickListener {

            test2()
        }

    }

    val REQ_PICK_FROM_ALBUM = 1000
    val REQ_PICK_FROM_CAMERA = 1001
    val REQ_PICK_FROM_URL = 1002


    fun test() {
//        getImgFromReqCode(REQ_PICK_FROM_ALBUM)

//        Glide.with(this)
//                .load("file:///storage/emulated/0/Android/data/hyunju.com.memo2020/files/IMG5275005868405655335.jpg")
//                .into(binding.iv)

//        getImgFromReqCode(REQ_PICK_FROM_CAMERA)

    }

    fun test2() {
        // set memo list

//        mainViewModel.allMemos.observe(this,
//                Observer(adapter::submitList))

//        mainViewModel.getMemoList(this)


        val memo = Memo(title = Date().time.toString(), contents = "contents", date = Date(),
                images = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png"
        )
        mainViewModel.insert(memo)

//        mainViewModel.deleteAll()
        Log.d("testsObserver", "test3 in")

    }

    fun test3() {


        Glide.with(this)
                .load("content://hyunju.com.memo2020.provider/images/Android/data/hyunju.com.memo2020/files/IMG8020930958117971683.jpg")
                .into(binding.testIv)

//        getImgFromReqCode(REQ_PICK_FROM_ALBUM)

    }

    fun testCapture() {


        binding.testIv.buildDrawingCache()
        val btm = binding.testIv.drawingCache
        saveCapture(this, btm)
    }


    fun getImgFromReqCode(reqCode: Int) {
        when (reqCode) {
            REQ_PICK_FROM_URL -> {

            }

            else -> {
                Intent().apply {
                    when (reqCode) {
                        REQ_PICK_FROM_ALBUM -> {
                            this.action = Intent.ACTION_PICK
                            this.type = MediaStore.Images.Media.CONTENT_TYPE

                        }
                        REQ_PICK_FROM_CAMERA -> {
                            // 1
                            val dirPath = applicationContext.getExternalFilesDir(null)?.absolutePath
                            val dir = File(dirPath).apply { if (!this.exists()) this.mkdir() }

                            val filePath = File.createTempFile("IMG", ".jpg", dir).apply {
                                if (!this.exists()) this.createNewFile()
                            }

                            val photoUri = FileProvider.getUriForFile(this@MainActivity2, "hyunju.com.memo2020.provider", filePath)
                            Util.savePref(this@MainActivity2, "uriFromCamera", photoUri.toString())


                            this.action = MediaStore.ACTION_IMAGE_CAPTURE
                            this.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

//                            this.action = MediaStore.ACTION_IMAGE_CAPTURE


                        }
                        else -> {
                        }
                    }

                }.let {
                    startActivityForResult(it, reqCode)
                }
            }
        }


    }


    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)
        Log.d("testPickAlbum", "resultCode = " + resultCode)


        if (resultCode == Activity.RESULT_OK) {

            when (reqCode) {
                REQ_PICK_FROM_ALBUM -> {
                    if (resultCode == Activity.RESULT_OK) {
//                    val imgBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data?.data)
//                    Log.d("testPickAlbum", "imgBitmap = " + imgBitmap)

//                        val imgUri = saveImgFilepath(btm = null, uri = data!!.data!!)

                        val imgUri = getFilePathFromURI(this, data!!.data!!)

                        Log.d("testPickAlbum", "onactivity saveFile = data!!.data! " + data!!.data!!)
                        Log.d("testPickAlbum", "onactivity saveFile = imgUri " + imgUri)

                        Glide.with(this)
                                .load(imgUri)
                                .into(binding.testIv)

                    }
                }
                REQ_PICK_FROM_CAMERA -> {
                    Util.getPref(this, "uriFromCamera").let {
                        if (it.isNotEmpty()) Log.d("testPickAlbum", "uri e= " + it)
//                    getFilePathFromURI(this, Uri.parse(it!!))

                    }

//                val imgUri = saveImgFilepath(btm = data!!.extras?.get("data") as Bitmap, uri = null)
//                Glide.with(this)
//                        .load(imgUri)
//                        .into(binding.testIv)


                }
            }


        }

        if (reqCode == REQ_PICK_FROM_CAMERA) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                if (contentUriForAndroidQ != null) {
                //uri로 부터 이미지를 받아와 화면에 보여주면 됩니다.
//                    val source = ImageDecoder.createSource(contentResolver, contentUriForAndroidQ)
//                    setBitmap(ImageDecoder.decodeBitmap(source))

//                }

            }
            Log.d("testCR", "onActivityResult")
        }
    }

    // TODO listener 문법 바꾸기
    override fun onItemClick(v: View, memo: Memo) {
        Log.d("testsObserver", "in onItemClick id = " + memo.id)

//        mainViewModel.delete(memo.id)

        val newContents = memo.contents + " 수정"
        memo.contents = newContents
        memo.images += " " + "https://postfiles.pstatic.net/MjAxODEwMjVfMTc3/MDAxNTQwNDY2MjY2MDM1.TovTMgYAZn8WJggpdZvlHWqBxsVCCf_Z6897OJ0WNTgg.o-KiNShXVmEh8ZJxdrVNELLzCe1XRh-T1ZfP84xSDq8g.JPEG.hyelim5012/IMG_20181023_061859.jpg?type=w966"
        mainViewModel.update(memo)

    }


//    private fun saveImgFilepath(btm: Bitmap?, uri: Uri?): Uri {
//        var bitmap = btm
//
//        if (bitmap == null) {
//            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
//        }
//
////        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
//
//        Log.d("testDir", "t " + applicationContext.getExternalFilesDir(null)?.absolutePath)
//        val dirPath = applicationContext.getExternalFilesDir(null)?.absolutePath
//        val dir = File(dirPath).apply { if (!this.exists()) this.mkdir() }
//
//        val filePath = File.createTempFile("IMG", ".jpg", dir).apply {
//            if (!this.exists()) this.createNewFile()
//        }
//
//        Log.d("testPickAlbum", "saveFile = filePath " + filePath)
//
//        var outputStream: OutputStream? = null
//
//        try {
//            outputStream = FileOutputStream(filePath)
//
//        } catch (e: Exception) {
//            Log.d("testPickAlbum", "saveFile = e " + e)
//
//            e.printStackTrace()
//        }
//
//        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//        outputStream?.flush()
//        outputStream?.close()
//        Log.d("testPickAlbum", "saveFile = uri " + Uri.fromFile(filePath))
//
//        return Uri.fromFile(filePath)
//
//
//    }

    fun createFilePath(context: Context): File {
        val dirPath = context.getExternalFilesDir(null)?.absolutePath
        val dir = File(dirPath).apply { if (!this.exists()) this.mkdir() }

        val filePath = File.createTempFile("IMG", ".jpg", dir).apply {
            if (!this.exists()) this.createNewFile()
        }

        return filePath
    }

    fun getFilePathFromURI(context: Context, contentUri: Uri): Uri? {
        val fileName = getFileName(contentUri)

        if (!TextUtils.isEmpty(fileName)) {
//            val dirPath = applicationContext.getExternalFilesDir(null)?.absolutePath
//            val dir = File(dirPath).apply { if (!this.exists()) this.mkdir() }
//
//            val filePath = File.createTempFile("IMG", ".jpg", dir).apply {
//                if (!this.exists()) this.createNewFile()
//            }
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

    fun saveCapture(context: Context, bitmap: Bitmap) {
        val filePath = createFilePath(context)
        val photoUri = FileProvider.getUriForFile(context, "hyunju.com.memo2020.provider", filePath)
        Log.d("testPickAlbum", "onactivity saveCapture = photoUri " + photoUri)

        val fos = FileOutputStream(filePath)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)

    }


}