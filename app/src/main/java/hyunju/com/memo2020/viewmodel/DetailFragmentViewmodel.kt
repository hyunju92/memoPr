package hyunju.com.memo2020.viewmodel

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo
import hyunju.com.memo2020.view.ListFragmentDirections

class DetailFragmentViewmodel(application: Application) : AndroidViewModel(application) {

    val dao = MemoDatabase.get(application).memoDao()
    var allMemos: LiveData<PagedList<Memo>> = LivePagedListBuilder(
            dao.getAllMemo(),
            20
    ).build()

    var imgList: LiveData<ArrayList<String>> = MutableLiveData()
    val memoItem: MutableLiveData<Memo> = MutableLiveData()


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

    fun moveDetailFrag(view: View) {
        val action =
                ListFragmentDirections.actionListFragmentToDetailFragment()
        Navigation.findNavController(view).navigate(action)
    }

    fun getScreenWidth(): Int {
        Log.d("testBinding", "getScreenWidth ")
//        val pxWidth = view.getResources().getDisplayMetrics().widthPixels
//        Log.d("testBinding", "getScreenWidth ")

        return 400
    }


    fun getMemoItemById(id: Long) {
//        memoItem.value = dao.getMemoById(id)
//
//        doAsync {
//            dao.getMemoById(id)
//        }.execute()
    }


}


