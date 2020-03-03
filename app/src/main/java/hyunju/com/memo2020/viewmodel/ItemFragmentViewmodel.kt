package hyunju.com.memo2020.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import hyunju.com.memo2020.R
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.util.ImgUtil
import hyunju.com.memo2020.util.ImgUtil.Companion.getProviderUri
import hyunju.com.memo2020.util.Util
import hyunju.com.memo2020.view.ItemFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ItemFragmentViewmodel(application: Application) : AndroidViewModel(application) {

    val dao = MemoDatabase.get(application).memoDao()

    val currentMode: MutableLiveData<Int> = MutableLiveData()

    val memoItem: MutableLiveData<Memo?> = MutableLiveData()
    val imgList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    var isNeedToSaveIfFinished: Boolean = true
    var isEdittedAtLeastOnce: Boolean = false


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


    // * data modify (need to access db)
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
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(memo)
        }
    }

    private fun insert(memo: Memo) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(memo)
        }
    }

    private fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }


    // * edit imgList of item view
    fun addImgList(uriStr: String?) {
        if (TextUtils.isEmpty(uriStr)) return

        val tempList: ArrayList<String> = imgList.value!!
        tempList.add(uriStr!!)
        imgList.value = tempList
    }

    fun deleteImgList(context: Context, position: Int) {
        val tempList: ArrayList<String> = imgList.value!!
        Log.d("testUrl", "" + tempList[position])
        context.contentResolver.delete(Uri.parse(tempList[position]), null, null);
        tempList.removeAt(position)

        imgList.value = tempList
    }


    // * from EditModeImgAdapter's 4-event
    // (delete img / get img from uri / get img from camera / get img from album)
    fun onEditAdapterItemClickEvent(context: Context, activity: Activity, v: View, postion: Int, requestBtnId: Int) {
        when (requestBtnId) {
            R.id.delete_btn_edit_img -> deleteImgList(context, postion)

            R.id.uri_btn_edit_img -> moveCaptureImgDialogFrag(v)

            R.id.camera_btn_edit_img, R.id.album_btn_edit_img -> getImgByReqcode(context, activity, requestBtnId)
        }
    }

    private val REQ_PICK_FROM_ALBUM = 1000
    private val REQ_PICK_FROM_CAMERA = 1001
    private var reqCode = 0

    private fun getImgByReqcode(context: Context, activity: Activity, requestBtnId: Int) {
        Intent().apply {
            when (requestBtnId) {
                R.id.album_btn_edit_img -> {
                    reqCode = REQ_PICK_FROM_ALBUM
                    this.action = Intent.ACTION_PICK
                    this.type = MediaStore.Images.Media.CONTENT_TYPE

                }

                R.id.camera_btn_edit_img -> {
                    reqCode = REQ_PICK_FROM_CAMERA
                    val providerImgUri = getProviderUri(context = context, filePath = null)
                    Util.setPref(context, "uriFromCamera", providerImgUri.toString())

                    this.action = MediaStore.ACTION_IMAGE_CAPTURE
                    this.putExtra(MediaStore.EXTRA_OUTPUT, providerImgUri)

                }
            }

        }.let {
            activity.startActivityForResult(it, reqCode)
        }

    }

    // move to CaptureImgDialogFragment
    // to get external url
    fun moveCaptureImgDialogFrag(view: View) {
        val action = ItemFragmentDirections.actionItemFragmentToCaptureImgDialogFragment()
        Navigation.findNavController(view).navigate(action)
    }


    // * result of startactivityforresult to get image
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_PICK_FROM_ALBUM -> {
                    if (data?.data != null) {
                        val providerUri = ImgUtil.copyUri(getApplication(), data.data!!)
                        addImgList(providerUri?.toString())
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


