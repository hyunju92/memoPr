package hyunju.com.memo2020.edit.vm

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import hyunju.com.memo2020.R
import hyunju.com.memo2020.db.Memo
import hyunju.com.memo2020.model.ImgUriRepository
import hyunju.com.memo2020.model.MemoRepository
import hyunju.com.memo2020.model.PrefRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class EditViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    private val imgUriRepository: ImgUriRepository,
    private val prefRepository: PrefRepository
) {

    private val disposable = CompositeDisposable()
    val uiEvent = PublishSubject.create<EditUiEvent>()

    // memo - LiveData
    private val _memoItem = MutableLiveData<Memo?>()
    val memoItem: LiveData<Memo?>
        get() = _memoItem

    private val _imgList = ObservableField<ArrayList<String>>()
//    val imgList: LiveData<ArrayList<String>>
//        get() = _imgList

    companion object {
        private const val REQ_PICK_FROM_ALBUM = 1000
        private const val REQ_PICK_FROM_CAMERA = 1001

        private const val URI_FROM_CAMERA = "URI_FROM_CAMERA"
    }

    fun setMemoItem(memo: Memo?) {
        _memoItem.value = memo
    }

    fun save(title: String, contents: String) {
        if (_memoItem.value == null) {  // create
            Memo(title = title, contents = contents, imageUriList = ObservableField<List<String>>(), date = Date()).let { newMemo ->
                if (isEmptyMemo(newMemo)) {
                    val msg = prefRepository.getStringFromResId(R.string.memo_empty)
                    uiEvent.onNext(EditUiEvent.ShowToast(msg))
                    uiEvent.onNext(EditUiEvent.MoveListFragment)

                } else {
                    createMemo(newMemo)
                }
            }

        } else {    // update
            _memoItem.value?.copy(title = title, contents = contents, imageUriList =ObservableField<List<String>>(), date = Date())?.let { newMemo ->
                updateMemo(newMemo)
            }

        }
    }

    private fun isEmptyMemo(memo: Memo): Boolean {
        return memo.title.isEmpty() && memo.contents.isEmpty() // imgurl null check
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
            memoRepository.insert(memo)
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
            memoRepository.update(memo)
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
        val msg = prefRepository.getStringFromResId(toastMsgResId)
        uiEvent.onNext(EditUiEvent.ShowToast(msg))
        uiEvent.onNext(EditUiEvent.MoveListFragment)
    }

    // delete img
    fun deleteImg(position: Int) {
        try {
            // DB 삭제
//            _imgList.value?.get(position)?.let { targetDeleteUri ->
//                imgUriRepository.deleteUri(targetDeleteUri) } ?: return
//            // livedata value 갱신
//            _imgList.value?.toMutableList()?.apply { removeAt(position) }?.let { newList ->
//                _imgList.value = ArrayList(newList) } ?: return

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
            val newUri = imgUriRepository.createNewUri()
            prefRepository.setPref(URI_FROM_CAMERA, newUri.toString())

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
                        imgUriRepository.createCopiedUri(data.data!!)?.let { newUri ->
                            addImg(newUri.toString()) }
                    }
                }
                REQ_PICK_FROM_CAMERA -> {
                    addImg(prefRepository.getPref(URI_FROM_CAMERA))
                }

            }
        }
    }

    private fun addImg(uriStr: String) {
        if (uriStr.isNotEmpty()) {
//            val newList: ArrayList<String> = _imgList.value ?: ArrayList()
//            newList.add(uriStr)
//            _imgList.value = newList
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
