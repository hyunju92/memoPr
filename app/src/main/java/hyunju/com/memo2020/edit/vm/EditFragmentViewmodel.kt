package hyunju.com.memo2020.edit.vm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.Navigation
import hyunju.com.memo2020.R
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.util.ImgUtil
import hyunju.com.memo2020.util.ImgUtil.Companion.createNewUri
import hyunju.com.memo2020.util.Util
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class EditFragmentViewmodel(private val context: Context, private val fragment: Fragment) {

    private val disposable = CompositeDisposable()
    val dao = MemoDatabase.get(context).memoDao()

    // LiveData
    private val _memoItem = MutableLiveData<Memo?>()
    val memoItem: LiveData<Memo?> = _memoItem
    private val _imgList = MutableLiveData<ArrayList<String>>()
    val imgList: LiveData<ArrayList<String>> = _imgList

    var imgPosition: Int? = 0

    // * set memo item (received as argument)
    fun setMemoItem(memo: Memo?) {
        _memoItem.value = memo
        _imgList.value = memo?.let { ArrayList(it.imageUrlList)}?:ArrayList()
        imgPosition = _imgList.value?.size
    }


    // * edit imgList of item view
    fun addImg(uriStr: String?) {
        if (TextUtils.isEmpty(uriStr)) return

        val tempList: ArrayList<String> = _imgList.value?:ArrayList()
        tempList.add(uriStr!!)
        _imgList.value = tempList
    }


    // * save memo (need to access db)
    fun save(activity: Activity, title: String, contents: String) {

        if (_memoItem.value == null) {
            // insert
            val newMemo = Memo(title = title, contents = contents, imageUrlList = _imgList.value?:ArrayList(), date = Date())

            if (memoIsEmpty(activity, newMemo)) return
            insert(newMemo).let { afterSave(activity) }

        } else {
            // update
            _memoItem.value!!.let {
                it.title = title
                it.contents = contents
                it.date = Date()
                it.imageUrlList = _imgList.value?:ArrayList()
                update(it).let { afterSave(activity) }
            }
        }
    }

    private fun memoIsEmpty(activity: Activity, memo: Memo): Boolean {
        if (memo.imageUrlList.isNullOrEmpty() && memo.title.isEmpty() && memo.contents.isEmpty()) {
            Toast.makeText(activity, activity.getString(R.string.memo_empty), Toast.LENGTH_SHORT).show()
            Navigation.findNavController(activity, R.id.main_fragment).navigateUp()
            return true
        } else {
            return false
        }
    }

    private fun afterSave(activity: Activity) {
        Navigation.findNavController(activity, R.id.main_fragment).navigateUp()
    }

    // * access db
    private fun update(memo: Memo?) {
        disposable.add(Single.just(memo)
            .subscribeOn(Schedulers.io())
            .map { it?.let { dao.update(it) > 0L } ?: false }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isSuccess ->
                if (isSuccess) {
                    Toast.makeText(context, context.getString(R.string.memo_update), Toast.LENGTH_SHORT).show()
                }
            }, {
                it.printStackTrace()
            })
        )
    }

    private fun insert(memo: Memo) {
        disposable.add(Single.just(memo)
            .subscribeOn(Schedulers.io())
            .map { it?.let { dao.insert(memo) > 0 } ?: false }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isSuccess ->
                if (isSuccess) {
                    Toast.makeText(context, context.getString(R.string.memo_insert), Toast.LENGTH_SHORT).show()
                }
            }, {
                it.printStackTrace()
            })
        )
    }

    // * from EditModeImgAdapter's 4-event
    // (delete img / get img from uri / get img from camera / get img from album)
    fun onEditAdapterItemClickEvent(v: View, postion: Int, requestBtnId: Int) {
        imgPosition = postion

        when (requestBtnId) {
            R.id.delete_btn_edit_img -> deleteImg(postion)
            R.id.camera_btn_edit_img, R.id.album_btn_edit_img -> getImgByStartActivity(requestBtnId)
        }
    }


    // delete img
    fun deleteImg(position: Int) {
        try {
            val tempList: ArrayList<String> = _imgList.value?:ArrayList()
            Log.d("testUrl", "" + tempList[position])
            context.contentResolver.delete(Uri.parse(tempList[position]), null, null);
            tempList.removeAt(position)

            _imgList.value = tempList
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // get img from camera / get img from album
    private val REQ_PICK_FROM_ALBUM = 1000
    private val REQ_PICK_FROM_CAMERA = 1001
    private var reqCode = 0

    fun pickImgFromAlbum() {
        Intent().apply {
            reqCode = REQ_PICK_FROM_ALBUM
            this.action = Intent.ACTION_PICK
            this.type = MediaStore.Images.Media.CONTENT_TYPE

        }.let {
            fragment.startActivityForResult(it, reqCode)
        }
    }

    fun pickImgFromCamera() {
        Intent().apply {

            reqCode = REQ_PICK_FROM_CAMERA
            val providerImgUri = createNewUri(context)
            Util.setPref(
                context,
                context.getString(R.string.pref_key_uri_from_camera),
                providerImgUri.toString()
            )

            this.action = MediaStore.ACTION_IMAGE_CAPTURE
            this.putExtra(MediaStore.EXTRA_OUTPUT, providerImgUri)


        }.let {
            fragment.startActivityForResult(it, reqCode)
        }
    }

    fun getImgByStartActivity(requestBtnId: Int) {
        Intent().apply {
            when (requestBtnId) {
                R.id.album_btn_edit_img -> {
                    reqCode = REQ_PICK_FROM_ALBUM
                    this.action = Intent.ACTION_PICK
                    this.type = MediaStore.Images.Media.CONTENT_TYPE

                }

                R.id.camera_btn_edit_img -> {
                    reqCode = REQ_PICK_FROM_CAMERA
                    val providerImgUri = createNewUri(context)
                    Util.setPref(context, context.getString(R.string.pref_key_uri_from_camera), providerImgUri.toString())

                    this.action = MediaStore.ACTION_IMAGE_CAPTURE
                    this.putExtra(MediaStore.EXTRA_OUTPUT, providerImgUri)

                }
            }

        }.let {
            fragment.startActivityForResult(it, reqCode)
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

    fun onDestroyViewModel() {
        disposable.clear()
    }

}


