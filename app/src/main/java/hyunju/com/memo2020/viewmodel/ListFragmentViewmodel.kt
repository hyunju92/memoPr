package hyunju.com.memo2020.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.view.ListFragmentDirections

class ListFragmentViewmodel(application: Application) : AndroidViewModel(application) {

    val dao = MemoDatabase.get(application).memoDao()
    var allMemos: LiveData<PagedList<Memo>> = LivePagedListBuilder(
            dao.getAllMemoByDate(),
            8
    ).build()

    fun moveItemFragment(view: View, memoItem: Memo?, mode: Int) {
        val action =
                ListFragmentDirections.actionListFragmentToItemFragment(memoItem, mode)

        Navigation.findNavController(view).navigate(action)
    }

}


