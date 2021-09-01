package hyunju.com.memo2020.edit.vm

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import hyunju.com.memo2020.R
import hyunju.com.memo2020.db.Memo
import hyunju.com.memo2020.model.Repository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class EditViewModel(private val repository: Repository) {

    private val disposable = CompositeDisposable()
    val uiEvent = PublishSubject.create<EditUiEvent>()

    // memo - LiveData
    private val _memoItem = MutableLiveData<Memo?>()
    val memoItem: LiveData<Memo?>
        get() = _memoItem

    private val _imgList = MutableLiveData<ArrayList<String>>()
    val imgList: LiveData<ArrayList<String>>
        get() = _imgList

    companion object {
        private const val REQ_PICK_FROM_ALBUM = 1000
        private const val REQ_PICK_FROM_CAMERA = 1001

        private const val URI_FROM_CAMERA = "URI_FROM_CAMERA"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setMemoItem(memo: Memo?) {
        _memoItem.value = memo

        val imgList = memo?.let { ArrayList(it.imageUriList) } ?: ArrayList()
        _imgList.value = imgList.apply { removeIf { it.isNullOrEmpty() } }  // 버그수정중-방어코드

    }

    fun save(title: String, contents: String) {
        if (_memoItem.value == null) {  // create
            Memo(title = title, contents = contents, imageUriList = _imgList.value ?: ArrayList(), date = Date()).let { newMemo ->
                if (isEmptyMemo(newMemo)) {
                    val msg = repository.getStringFromResId(R.string.memo_empty)
                    uiEvent.onNext(EditUiEvent.ShowToast(msg))
                    uiEvent.onNext(EditUiEvent.MoveListFragment)

                } else {
                    createMemo(newMemo)
                }
            }

        } else {    // update
            _memoItem.value?.copy(title = title, contents = contents, imageUriList = _imgList.value ?: ArrayList(), date = Date())?.let { newMemo ->
                updateMemo(newMemo)
            }

        }
    }

    private fun isEmptyMemo(memo: Memo): Boolean {
        return memo.imageUriList.isNullOrEmpty() && memo.title.isEmpty() && memo.contents.isEmpty()
    }

    private fun createMemo(newMemo: Memo) {
        insertDb(newMemo)
    }

    private fun updateMemo(newMemo: Memo) {
        updateDb(newMemo)
    }

    // * access db
    private fun insertDb(memo: Memo) {
        disposable.add(
            repository.insert(memo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _memoItem.value = memo
                    setViewEventAfterDbProcess(toastMsgResId = R.string.memo_insert)
                }, {
                    it.printStackTrace()
                })
        )
    }

    private fun updateDb(memo: Memo) {
        disposable.add(
            repository.update(memo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _memoItem.value = memo
                    setViewEventAfterDbProcess(toastMsgResId = R.string.memo_update)
                }, {
                    it.printStackTrace()
                })
        )
    }

    private fun setViewEventAfterDbProcess(toastMsgResId: Int) {
        val msg = repository.getStringFromResId(toastMsgResId)
        uiEvent.onNext(EditUiEvent.ShowToast(msg))
        uiEvent.onNext(EditUiEvent.MoveListFragment)
    }

    // delete img
    fun deleteImg(position: Int) {
        try {
            // DB 삭제
            _imgList.value?.get(position)?.let { targetDeleteUri ->
                repository.deleteUri(targetDeleteUri) } ?: return
            // livedata value 갱신
            _imgList.value?.toMutableList()?.apply { removeAt(position) }?.let { newList ->
                _imgList.value = ArrayList(newList) } ?: return

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pickImgFromAlbum() {
        Intent().apply {
            this.action = Intent.ACTION_PICK
            this.type = MediaStore.Images.Media.CONTENT_TYPE

        }.let {
            uiEvent.onNext(EditUiEvent.StartActivityForImgUri(REQ_PICK_FROM_ALBUM, it))
        }
    }

    fun pickImgFromCamera() {
        Intent().apply {
            val newUri = repository.createNewUri()
            repository.setPref(URI_FROM_CAMERA, newUri.toString())

            this.action = MediaStore.ACTION_IMAGE_CAPTURE
            this.putExtra(MediaStore.EXTRA_OUTPUT, newUri)

        }.let {
            uiEvent.onNext(EditUiEvent.StartActivityForImgUri(REQ_PICK_FROM_CAMERA, it))
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_PICK_FROM_ALBUM -> {
                    if (data?.data != null) {
                        repository.createCopiedUri(data.data!!)?.let { newUri ->
                            addImg(newUri.toString()) }
                    }
                }
                REQ_PICK_FROM_CAMERA -> {
                    repository.getPref(URI_FROM_CAMERA).let { newUri ->
                        addImg(newUri) }
                }

            }
        }
    }

    private fun addImg(uriStr: String) {
        if (uriStr.isNotEmpty()) {
            val newList: ArrayList<String> = _imgList.value ?: ArrayList()
            newList.add(uriStr)
            _imgList.value = newList
        }
    }

    fun onDestroyViewModel() {
        disposable.clear()
    }

}


sealed class EditUiEvent {
    data class ShowToast(val msg: String) : EditUiEvent()
    data class StartActivityForImgUri(val requestCode: Int, val intent: Intent) : EditUiEvent()
    object MoveListFragment : EditUiEvent()
}
