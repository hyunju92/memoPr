package hyunju.com.memo2020.viewmodel

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo

class MainAcitivityViewmodel(application: Application) : AndroidViewModel(application) {

    val app = application
    val dao = MemoDatabase.get(app).memoDao()
    var memoList: LiveData<PagedList<Memo>> = MutableLiveData()


    fun getMemoList(context: Context) {
        memoList = LivePagedListBuilder(
                dao.getMemoById(),
                20
        ).build()

        Log.d("testsObserver", "viewmodel " + memoList.value?.size.toString())

    }

    fun insert(memo: Memo) {
        doAsync {
            dao.insert(memo)
        }.execute()

        Log.d("testsObserver", "after insert " + memoList.value?.size)

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
}


