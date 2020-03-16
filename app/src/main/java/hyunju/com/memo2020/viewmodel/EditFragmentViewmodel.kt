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
import androidx.lifecycle.*
import androidx.navigation.Navigation
import hyunju.com.memo2020.R
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.util.ImgUtil
import hyunju.com.memo2020.util.ImgUtil.Companion.getProviderUri
import hyunju.com.memo2020.util.Util
import hyunju.com.memo2020.view.EditFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class EditFragmentViewmodel(application: Application) : AndroidViewModel(application) {

    val dao = MemoDatabase.get(application).memoDao()

    val memoItem: MutableLiveData<Memo?> = MutableLiveData()
    val imgList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var imgPosition: Int? = 0


    // * set memo item (received as argument)
    fun setMemoItem(memo: Memo?) {
        memoItem.value = memo
        imgList.value = memo?.getImageList() ?: ArrayList()
        imgPosition = imgList.value?.size
    }


    // * edit imgList of item view
    fun addImg(uriStr: String?) {
        if (TextUtils.isEmpty(uriStr)) return

        val tempList: ArrayList<String> = imgList.value!!
        tempList.add(uriStr!!)
        imgList.value = tempList
    }


    // * save memo (need to access db)
    fun save(activity: Activity, title: String, contents: String) {

        if (memoItem.value == null) {
            // insert
            val newMemo = Memo(title = title, contents = contents, images = "", date = Date())
            newMemo.setImgStr(imgList.value!!)

            if (memoIsEmpty(activity, newMemo)) return
            insert(newMemo).let { afterSave(activity, activity.getString(R.string.memo_insert)) }

        } else {
            // update
            memoItem.value!!.let {
                it.title = title
                it.contents = contents
                it.date = Date()
                it.setImgStr(imgList.value!!)

                update(it).let { afterSave(activity, activity.getString(R.string.memo_update)) }
            }
        }

    }

    private fun memoIsEmpty(activity: Activity, memo: Memo): Boolean {
        if (memo.getImageList().isNullOrEmpty() && memo.title.isEmpty() && memo.contents.isEmpty()) {
            Toast.makeText(activity, activity.getString(R.string.memo_empty), Toast.LENGTH_SHORT).show()
            Navigation.findNavController(activity, R.id.main_fragment).navigateUp()
            return true
        } else {
            return false
        }
    }

    private fun afterSave(activity: Activity, msg: String?) {
        val viewFragVm: ViewFragmentViewmodel by lazy {
            ViewModelProvider(activity as ViewModelStoreOwner).get(ViewFragmentViewmodel::class.java)
        }

        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
        viewFragVm.setMemoItem(memo = memoItem.value)
        Navigation.findNavController(activity, R.id.main_fragment).navigateUp()
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


    // * from EditModeImgAdapter's 4-event
    // (delete img / get img from uri / get img from camera / get img from album)
    fun onEditAdapterItemClickEvent(context: Context, activity: Activity, v: View, postion: Int, requestBtnId: Int) {
        imgPosition = postion

        when (requestBtnId) {
            R.id.delete_btn_edit_img -> deleteImg(context, postion)

            R.id.uri_btn_edit_img -> getImgByUri(v)

            R.id.camera_btn_edit_img, R.id.album_btn_edit_img -> getImgByStartActivity(context, activity, requestBtnId)
        }
    }


    // delete img
    private fun deleteImg(context: Context, position: Int) {
        val tempList: ArrayList<String> = imgList.value!!
        Log.d("testUrl", "" + tempList[position])
        context.contentResolver.delete(Uri.parse(tempList[position]), null, null);
        tempList.removeAt(position)

        imgList.value = tempList
    }


    // get img from uri - move to CaptureImgDialogFragment to get external url
    private fun getImgByUri(view: View) {
        val action = EditFragmentDirections.actionEditFragmentToCaptureImgDialogFragment()
        Navigation.findNavController(view).navigate(action)
    }


    // get img from camera / get img from album
    private val REQ_PICK_FROM_ALBUM = 1000
    private val REQ_PICK_FROM_CAMERA = 1001
    private var reqCode = 0

    private fun getImgByStartActivity(context: Context, activity: Activity, requestBtnId: Int) {
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
                    Util.setPref(context, context.getString(R.string.pref_key_uri_from_camera), providerImgUri.toString())

                    this.action = MediaStore.ACTION_IMAGE_CAPTURE
                    this.putExtra(MediaStore.EXTRA_OUTPUT, providerImgUri)

                }
            }

        }.let {
            activity.startActivityForResult(it, reqCode)
        }

    }


    // result of startactivityforresult to get image
    fun onActivityResult(context: Context, requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            REQ_PICK_FROM_ALBUM -> {
                if (data?.data == null) return
                val providerUri = ImgUtil.copyUri(context, data.data!!)
                addImg(providerUri?.toString())
            }
            REQ_PICK_FROM_CAMERA -> {
                Util.getPref(context, context.getString(R.string.pref_key_uri_from_camera)).let {
                    if (it.isNotEmpty()) addImg(it)
                }
            }

        }
    }


}


