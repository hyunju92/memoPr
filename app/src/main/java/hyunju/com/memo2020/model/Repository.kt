package hyunju.com.memo2020.model

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hyunju.com.memo2020.db.MemoDao
import hyunju.com.memo2020.db.MemoDatabase
import hyunju.com.memo2020.util.ImgUtil
import hyunju.com.memo2020.util.Util
import io.reactivex.rxjava3.core.Completable

class Repository(memoDatabase: MemoDatabase, private val context: Context) {
    private val memoDao: MemoDao = memoDatabase.memoDao()

    val allMemos: LiveData<PagedList<Memo>> = LivePagedListBuilder(
        memoDao.getAllMemoByDate(),
        8
    ).build()

    fun insert(memo: Memo): Completable {
        return memoDao.insert(memo)
    }

    fun update(memo: Memo): Completable {
        return memoDao.update(memo)
    }

    fun delete(memo: Memo): Completable {
        return memoDao.delete(memo)
    }

    fun createNewUri(): Uri? {
        return ImgUtil.createNewUri(context)
    }

    fun createCopiedUri(uri: Uri): Uri? {
        return ImgUtil.createCopiedUri(context, uri)
    }

    fun setPref(key: String, value: String) {
        Util.setPref(context, key, value)
    }

    fun getPref(key: String): String {
        return Util.getPref(context, key)
    }

    fun getStringFromResId(resId : Int) : String {
        return context.getString(resId)
    }
}