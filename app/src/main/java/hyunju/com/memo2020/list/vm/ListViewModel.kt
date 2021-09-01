package hyunju.com.memo2020.list.vm

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import hyunju.com.memo2020.R
import hyunju.com.memo2020.db.Memo
import hyunju.com.memo2020.model.Repository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class ListViewModel(private val repository: Repository) {

    private var disposable: Disposable? = null
    val uiEvent = PublishSubject.create<ListUiEvent>()

    // memo - LiveData
    val allMemos: LiveData<PagedList<Memo>> = repository.allMemos

    fun moveEditFragment(memo: Memo? = null) {
        uiEvent.onNext(ListUiEvent.MoveEditFragment(memo))
    }

    fun showSelectDialog(memo: Memo) {
        uiEvent.onNext(ListUiEvent.ShowSelectDialog(memo))
    }

    fun delete(memo: Memo) {
        disposable = repository.delete(memo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val msg = repository.getStringFromResId(R.string.memo_delete)
                uiEvent.onNext(ListUiEvent.ShowToast(msg))

            }, {
                it.printStackTrace()
            })
    }

    fun onDestroyViewModel() {
        disposable?.dispose()
    }

}

sealed class ListUiEvent {
    data class MoveEditFragment(val memoItem: Memo? = null) : ListUiEvent()
    data class ShowSelectDialog(val memoItem: Memo) : ListUiEvent()
    data class ShowToast(val msg: String) : ListUiEvent()
}


