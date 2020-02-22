package hyunju.com.memo2020.viewmodel

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo

class MainAcitivityViewmodel(application: Application) : AndroidViewModel(application) {

    val dao = MemoDatabase.get(application).memoDao()
    var allMemos: LiveData<PagedList<Memo>> = LivePagedListBuilder(
            dao.getAllMemo(),
            20
    ).build()

    val listViewModel: ListFragmentViewmodel = ListFragmentViewmodel(application)


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

//    fun testNav(view: View) {
//
//        val action =
//                ListFragmentDirections.actionListFragmentToItemFragment()
//        Navigation.findNavController(view).navigate(action)
//    }

    fun testViewmodelReUse() : Int{
        return listViewModel.getScreenWidth(getApplication())
    }

}


