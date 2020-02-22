package hyunju.com.memo2020.viewmodel

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
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
            dao.getAllMemo(),
            20
    ).build()


    fun insert(memo: Memo) {
        doAsync {
            dao.insert(memo)
        }.execute()
    }

    fun delete(id: Long) {
        doAsync {
            dao.deleteById(id)
        }.execute()
    }

    fun deleteAll() {
        doAsync {
            dao.deleteAll()
        }.execute()
    }

    fun update(memo: Memo) {
        doAsync {
            dao.update(memo)
        }.execute()
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Log.d("testsObserver", "onPostExecute ")

        }
    }

    fun moveDetailFrag(view: View, memoItem: Memo?) {
        val action =
                ListFragmentDirections.actionListFragmentToItemFragment(memoItem)

        Navigation.findNavController(view).navigate(action)
    }


    fun getScreenWidth(context: Context): Int {
        Log.d("testBinding", "getScreenWidth ")
        return 40
    }


}


