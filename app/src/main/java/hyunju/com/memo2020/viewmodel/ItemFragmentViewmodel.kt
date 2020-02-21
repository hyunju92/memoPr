package hyunju.com.memo2020.viewmodel

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.model.Memo
import java.util.*
import kotlin.collections.ArrayList

class ItemFragmentViewmodel(application: Application) : AndroidViewModel(application) {

    val dao = MemoDatabase.get(application).memoDao()
    var allMemos: LiveData<PagedList<Memo>> = LivePagedListBuilder(
            dao.getAllMemo(),
            20
    ).build()

    private var initType: Int = 0
    val type: MutableLiveData<Int> = MutableLiveData()
    var memoItem: MutableLiveData<Memo?> = MutableLiveData()


    // item 화면 Type
    val TYPE_DETAIL = 1
    val TYPE_EDIT = 2

    fun setType(type: Int) {
        if (initType == 0) initType = type
        this.type.value = type
    }

    fun isEdit(): Boolean {
        return this.type.value == TYPE_EDIT
    }

    //  img list
    fun getImgList(): ArrayList<String> {
        return if (memoItem.value != null) memoItem.value!!.getImageList() else ArrayList()
    }

    // 저장
    fun save(context: Context, title: String, contents: String) {

        var newMemo: Memo = if (memoItem.value != null) {
            memoItem.value.apply {
                if (!TextUtils.equals(this!!.title, title)) this.title = title
                if (!TextUtils.equals(this.contents, contents)) this.contents = contents
                this.date = Date()
            }!!

        } else {
            Memo(title = title, contents = contents, images = "", date = Date())
        }

        if (initType == TYPE_EDIT) {
            insert(newMemo)
            initType = TYPE_DETAIL
            Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()

        } else {
            update(newMemo)
            Toast.makeText(context, "수정되었습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    // 삭제
    fun delete(context: Context) {
        if (memoItem.value != null) {
            delete(memoItem.value!!.id)
            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun update(memo: Memo?) {
        doAsync {
            dao.update(memo)
        }.execute()
    }

    private fun insert(memo: Memo) {
        doAsync {
            dao.insert(memo)
        }.execute()
    }

    private fun delete(id: Long) {
        doAsync {
            dao.deleteById(id)
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


}


