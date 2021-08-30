package hyunju.com.memo2020.edit.vm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import hyunju.com.memo2020.R
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.model.MemoRepository
import hyunju.com.memo2020.util.ImgUtil
import hyunju.com.memo2020.util.ImgUtil.Companion.createNewUri
import hyunju.com.memo2020.util.Util
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class EditViewModel(private val repository: MemoRepository, private val context: Context, private val fragment: Fragment) {

    val uiEvent= PublishSubject.create<EditUiEvent>()
    private val disposable = CompositeDisposable()

    // LiveData
    private val _memoItem = MutableLiveData<Memo?>()
    val memoItem: LiveData<Memo?>
        get() = _memoItem

    private val _imgList = MutableLiveData<ArrayList<String>>()
    val imgList: LiveData<ArrayList<String>>
        get() = _imgList

    // * set memo item (received as argument)
    @RequiresApi(Build.VERSION_CODES.N)
    fun setMemoItem(memo: Memo?) {
        _memoItem.value = memo
        // 버그 수정 필요 (임시 방어 코드)
        val tempImgList = memo?.let { ArrayList(it.imageUriList)}?:ArrayList()
        tempImgList.removeIf { it.isNullOrEmpty() }
        _imgList.value = tempImgList

    }

    // * edit imgList of item view
    fun addImg(uriStr: String?) {
        if (TextUtils.isEmpty(uriStr)) return
        val tempList: ArrayList<String> = _imgList.value ?: ArrayList()
        tempList.add(uriStr!!)
        _imgList.value = tempList
    }


    // * save memo (need to access db)
    fun save(title: String, contents: String) {

        if (_memoItem.value == null) {
            // insert
            val newMemo = Memo(
                title = title,
                contents = contents,
                imageUriList = _imgList.value ?: ArrayList(),
                date = Date()
            )

            if (memoIsEmpty(newMemo)) return
            insert(newMemo)

        } else {
            // update
            val updateItem = _memoItem.value?.copy(
                title = title,
                contents = contents,
                date = Date(),
                imageUriList = _imgList.value ?: ArrayList()
            )
            _memoItem.value = updateItem
            update(_memoItem.value)

        }
    }

    private fun memoIsEmpty(memo: Memo): Boolean {
        return if (memo.imageUriList.isNullOrEmpty() && memo.title.isEmpty() && memo.contents.isEmpty()) {
            uiEvent.onNext(EditUiEvent.ShowToast(R.string.memo_empty))
            uiEvent.onNext(EditUiEvent.MoveListFragment)
            true
        } else {
            false
        }
    }

    private fun afterSave() {
        uiEvent.onNext(EditUiEvent.MoveListFragment)
    }

    // * access db
    private fun update(memo: Memo?) {
        disposable.add(repository.update(memo!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                uiEvent.onNext(EditUiEvent.ShowToast(R.string.memo_update))
                afterSave()
            }, {
                it.printStackTrace()
            })
        )
    }

    private fun insert(memo: Memo) {
        disposable.add(repository.insert(memo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    uiEvent.onNext(EditUiEvent.ShowToast(R.string.memo_insert))
                    afterSave()
                }, {
                    it.printStackTrace()
                })
        )
    }

    // delete img
    fun deleteImg(position: Int) {
        try {
            val tempList: ArrayList<String> = _imgList.value ?: ArrayList()
            uiEvent.onNext(EditUiEvent.DeleteImgUri(tempList[position]))
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


sealed class EditUiEvent {
    data class ShowToast(val msgResId: Int) : EditUiEvent()
    data class DeleteImgUri(val imgUri: String) : EditUiEvent()
    object MoveListFragment : EditUiEvent()
}