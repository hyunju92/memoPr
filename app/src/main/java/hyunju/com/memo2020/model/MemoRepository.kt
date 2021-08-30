package hyunju.com.memo2020.model

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hyunju.com.memo2020.db.MemoDao
import hyunju.com.memo2020.db.MemoDatabase
import io.reactivex.rxjava3.core.Completable

class MemoRepository(memoDatabase: MemoDatabase) {
    private val memoDao: MemoDao = memoDatabase.memoDao()

    val allMemos: LiveData<PagedList<Memo>> = LivePagedListBuilder(
        memoDao.getAllMemoByDate(),
        8
    ).build()

    fun insert(memo: Memo) : Completable { return memoDao.insert(memo)}
    fun update(memo: Memo) : Completable { return memoDao.update(memo)}
    fun delete(memo: Memo) : Completable { return memoDao.delete(memo)}

}