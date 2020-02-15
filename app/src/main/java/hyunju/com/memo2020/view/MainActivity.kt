package hyunju.com.memo2020.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hyunju.com.memo2020.R
import hyunju.com.memo2020.Util
import hyunju.com.memo2020.databinding.ActivityMainBinding
import hyunju.com.memo2020.db.MemoAdapter
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.viewmodel.MainAcitivityViewmodel
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    protected lateinit var binding: ActivityMainBinding
    protected val mainViewModel: MainAcitivityViewmodel by lazy {
        ViewModelProvider(this).get(MainAcitivityViewmodel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.btn.setOnClickListener { test3() }

        test2()

    }

    val REQ_PICK_FROM_ALBUM = 1000
    val REQ_PICK_FROM_CAMERA = 1001
    val REQ_PICK_FROM_URL = 1002


    fun test() {
//        getImgFromReqCode(REQ_PICK_FROM_ALBUM)

//        Glide.with(this)
//                .load("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png")
//                .into(binding.iv)

        getImgFromReqCode(REQ_PICK_FROM_CAMERA)

    }

    fun test2() {
        // set memo list
        val adapter = MemoAdapter()
        mainViewModel.memoList.observe(this, Observer {
            Log.d("testsObserver", it.size.toString())
            adapter.submitList(it)
        })
        binding.rv.adapter = adapter
        mainViewModel.getMemoList(this)

    }

    fun test3() {

        val id = Date().time.toInt()

        val memo = Memo(id = id, title = "test", contents = "contents", date = Date(),
                images = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory&fname=https://k.kakaocdn.net/dn/EShJF/btquPLT192D/SRxSvXqcWjHRTju3kHcOQK/img.png"
        )
        mainViewModel.insert(memo)

        Log.d("testsObserver", "test3 in")
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
                            val dirPath = applicationContext.getExternalFilesDir(null)?.absolutePath + "/meme2020"
                            val dir = File(dirPath).apply { if (!this.exists()) this.mkdir() }

                            val filePath = File.createTempFile("IMG", ".jpg", dir).apply {
                                if (!this.exists()) this.createNewFile()
                            }

                            val photoUri = FileProvider.getUriForFile(this@MainActivity, "hyunju.com.memo2020.provider", filePath)
                            Util.savePref(this@MainActivity, "uriFromCamera", photoUri.toString())

                            this.action = MediaStore.ACTION_IMAGE_CAPTURE
                            this.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

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
            Log.d("testPickAlbum", "uri = " + data?.data)
        }
        when (reqCode) {
            REQ_PICK_FROM_ALBUM -> {
                if (resultCode == Activity.RESULT_OK) {
                }
            }
            REQ_PICK_FROM_CAMERA -> {
                Util.getPref(this, "uriFromCamera").let {
                    if (it.isNotEmpty()) Log.d("testPickAlbum", "uri e= " + it)
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

}