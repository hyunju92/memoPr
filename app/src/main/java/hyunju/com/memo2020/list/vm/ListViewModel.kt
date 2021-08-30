package hyunju.com.memo2020.list.vm

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.paging.PagedList
import hyunju.com.memo2020.R
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.model.MemoRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class ListViewModel(private val repository: MemoRepository) {

    val uiEvent = PublishSubject.create<ListUiEvent>()
    val allMemos: LiveData<PagedList<Memo>> = repository.allMemos
    private var disposable: Disposable? = null

    fun moveEditFragment(memoItem: Memo? = null) {
        uiEvent.onNext(ListUiEvent.MoveEditFragment(memoItem))
    }

    fun showSelectDialog(memoItem: Memo?) {
        uiEvent.onNext(ListUiEvent.ShowSelectDialog(memoItem))
    }

    fun delete(activity: Activity, memo: Memo) {
        disposable = repository.delete(memo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(
                    activity,
                    activity.getString(R.string.memo_delete),
                    Toast.LENGTH_SHORT
                ).show()
                Navigation.findNavController(activity, R.id.main_fragment).navigateUp()
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
    data class ShowSelectDialog(val memoItem: Memo?) : ListUiEvent()
}


